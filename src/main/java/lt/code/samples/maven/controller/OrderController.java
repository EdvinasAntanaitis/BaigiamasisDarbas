package lt.code.samples.maven.controller;

import lombok.RequiredArgsConstructor;
import lt.code.samples.maven.dto.*;
import lt.code.samples.maven.model.WorkLogEntity;
import lt.code.samples.maven.order.*;
import lt.code.samples.maven.repository.OrderRepository;
import lt.code.samples.maven.service.OrderService;
import lt.code.samples.maven.service.WorkLogService;
import lt.code.samples.maven.user.model.UserEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService   orderService;
    private final OrderRepository orderRepository;
    private final WorkLogService workLogService;

    @PostMapping("/new")
    public String submitOrder(@ModelAttribute OrderFormDTO orderForm,
                              RedirectAttributes redirectAttributes) {

        Order order = convertDtoToEntity(orderForm);
        orderRepository.save(order);

        redirectAttributes.addFlashAttribute("successMessage",
                "Order created successfully!");
        return "redirect:/dashboard";
    }

    private Order convertDtoToEntity(OrderFormDTO dto) {
        Order order = new Order();
        order.setOrderName(dto.getOrderName());

        List<PartGroup> groups = new ArrayList<>();
        for (PartGroupDTO gDto : dto.getPartGroups()) {
            PartGroup g = new PartGroup();
            g.setMaterial(gDto.getMaterial());
            g.setPaintColor(gDto.getPaintColor());
            g.setOrder(order);

            List<Part> parts = new ArrayList<>();
            for (PartDTO pDto : gDto.getParts()) {
                Part p = new Part();
                p.setLength(pDto.getLength());
                p.setWidth(pDto.getWidth());
                p.setThickness(pDto.getThickness());
                p.setAmount(pDto.getAmount());
                p.setPaintedArea(pDto.getPaintedArea());
                p.setPartGroup(g);
                parts.add(p);
            }
            g.setParts(parts);
            groups.add(g);
        }
        order.setPartGroups(groups);
        return order;
    }

    @GetMapping("/{id}")
    public String viewOrder(@PathVariable Long id, Model model) {
        Optional<Order> optional = orderService.getOrderById(id);

        if (optional.isEmpty()) {
            return "redirect:/dashboard";
        }

        Order order = optional.get();

        double totalArea = order.getPartGroups().stream()
                .flatMap(g -> g.getParts().stream())
                .mapToDouble(this::calculatePaintedArea)
                .sum();

        Map<String, Double> areaPerGroup = order.getPartGroups().stream()
                .collect(Collectors.toMap(
                        g -> g.getMaterial() + "-" + g.getPaintColor(),
                        g -> g.getParts().stream()
                                .mapToDouble(this::calculatePaintedArea)
                                .sum()
                ));

        model.addAttribute("order",        order);
        model.addAttribute("totalArea",    totalArea);
        model.addAttribute("areaPerGroup", areaPerGroup);
        return "orders/view";
    }

    private double calculatePaintedArea(Part part) {
        double L = part.getLength()   / 1000d;
        double W = part.getWidth()    / 1000d;
        double T = part.getThickness()/ 1000d;

        return switch (part.getPaintedArea()) {
            case ALL_SIDES            -> 2 * (L*W + L*T + W*T);
            case ONE_EDGE             -> T * W;
            case TWO_EDGES            -> 2 * T * W;
            case THREE_EDGES          -> 2 * (L*T + W*T) + T*W;
            case ONE_SIDE_NO_EDGES    -> L * W;
            case ONE_SIDE_WITH_EDGES  -> L*W + 2*(L*T + W*T);
        };
    }

    @GetMapping(value = "/worklog", params = "!orderName")
    public String showWorkLogForm() {
        return "orders/worklog";
    }

    @GetMapping(value = "/worklog", params = "orderName")
    public String showWorkLog(@RequestParam String orderName, Model model) {
        workLogService.findOrderByName(orderName)
                .ifPresent(o -> model.addAttribute("order", o));
        return "orders/worklog";
    }

    @GetMapping("/worklog/search")
    public String findOrderById(@RequestParam Long orderId,
                                Model model,
                                RedirectAttributes ra) {
        return workLogService.findOrderById(orderId)
                .map(o -> { model.addAttribute("order", o); return "orders/worklog"; })
                .orElseGet(() -> {
                    ra.addFlashAttribute("message",
                            "Order with ID " + orderId + " not found.");
                    return "redirect:/orders/worklog";
                });
    }

    @PostMapping("/worklog/start")
    public String startWork(@RequestParam Long orderId,
                            @RequestParam String operation,
                            RedirectAttributes ra) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = (UserEntity) auth.getPrincipal();
        String fullName = user.getFirstName() + " " + user.getLastName();

        Order order = workLogService.startWork(orderId, fullName, operation);

        ra.addFlashAttribute("message", "Work started.");
        return "redirect:/orders/worklog?orderName=" + order.getOrderName();
    }

    @PostMapping("/worklog/end")
    public String endWork(@RequestParam Long workLogId,
                          RedirectAttributes redirectAttributes) {

        Optional<WorkLogEntity> opt = workLogService.findById(workLogId);
        if (opt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Work-log not found.");
            return "redirect:/dashboard";
        }

        WorkLogEntity log = opt.get();

        if (log.isFaulty() && !log.isFaultFixed()) {
            redirectAttributes.addFlashAttribute("error",
                    "Cannot finish a faulty operation.");
            return "redirect:/orders/worklog?orderName=" +
                    log.getOrder().getOrderName();
        }

        if (log.getEndTime() != null) {
            redirectAttributes.addFlashAttribute("message", "This operation is already finished.");
            return "redirect:/orders/worklog?orderName=" + log.getOrder().getOrderName();
        }

        log.setEndTime(LocalDateTime.now());
        workLogService.save(log);

        redirectAttributes.addFlashAttribute("message", "Work finished successfully.");
        return "redirect:/orders/worklog?orderName=" + log.getOrder().getOrderName();
    }

    @PostMapping("/worklog/fault")
    public String markAsFaulty(@RequestParam Long   workLogId,
                               @RequestParam String faultDescription,
                               @RequestParam String operationName,
                               RedirectAttributes ra) {

        workLogService.markAsFaulty(workLogId, faultDescription, operationName);
        ra.addFlashAttribute("message", "Marked as faulty.");

        return "redirect:/orders/worklog?orderName=" +
                workLogService.findById(workLogId)
                        .map(l -> l.getOrder().getOrderName())
                        .orElse("");
    }

    @PostMapping("/end")
    public String endOrder(@RequestParam Long orderId,
                           RedirectAttributes ra) {

        Optional<Order> optOrder = orderService.getOrderById(orderId);
        if (optOrder.isEmpty()) {
            ra.addFlashAttribute("error", "Order not found.");
            return "redirect:/orders";
        }

        Order order = optOrder.get();

        boolean allFinished = order.getWorkLogs().stream()
                .allMatch(log ->
                        log.getEndTime() != null &&
                                (!log.isFaulty() || (log.isFaulty() && log.isFaultFixed()))
                );
        if (!allFinished) {
            ra.addFlashAttribute("error",
                    "Cannot end order – some tasks are unfinished or faulty.");
            return "redirect:/orders/worklog?orderName=" + order.getOrderName();
        }

        order.setCompleted(true);
        orderRepository.save(order);

        ra.addFlashAttribute("message", "Order ended successfully.");
        return "redirect:/orders";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/delete")
    public String deleteOrder(@PathVariable Long id,
                              RedirectAttributes ra) {
        orderRepository.deleteById(id);
        ra.addFlashAttribute("message", "Order deleted.");
        return "redirect:/orders";
    }

    @GetMapping("/{id}/edit")
    @PreAuthorize("hasRole('ADMIN')")
    public String editOrderForm(@PathVariable Long id, Model model) {
        Order order = orderService.getOrderById(id).orElseThrow();
        model.addAttribute("order", order);
        return "orders/editOrder";
    }

    @PostMapping("/worklog/fix/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String fixFault(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        WorkLogEntity log = workLogService.findById(id).orElseThrow();
        if (!log.isFaulty()) {
            redirectAttributes.addFlashAttribute("error", "This entry is not marked as faulty.");
            return "redirect:/orders/worklog?orderName=" + log.getOrder().getOrderName();
        }

        log.setFaultFixed(true);
        workLogService.save(log);

        redirectAttributes.addFlashAttribute("message", "Fault has been marked as fixed.");
        return "redirect:/orders/worklog?orderName=" + log.getOrder().getOrderName();
    }
}
