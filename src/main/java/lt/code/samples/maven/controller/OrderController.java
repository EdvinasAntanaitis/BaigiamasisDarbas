package lt.code.samples.maven.controller;

import lombok.RequiredArgsConstructor;
import lt.code.samples.maven.dto.OrderFormDTO;
import lt.code.samples.maven.dto.PartDTO;
import lt.code.samples.maven.dto.PartGroupDTO;
import lt.code.samples.maven.order.Order;
import lt.code.samples.maven.order.Part;
import lt.code.samples.maven.order.PartGroup;
import lt.code.samples.maven.repository.OrderRepository;
import lt.code.samples.maven.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final OrderRepository orderRepository;

    @PostMapping("/new")
    public String submitOrder(@ModelAttribute OrderFormDTO orderForm,
                              RedirectAttributes redirectAttributes) {

        System.out.println("Gautas užsakymas: " + orderForm.getOrderName());
        Order orderEntity = convertDtoToEntity(orderForm);
        orderRepository.save(orderEntity);
        redirectAttributes.addFlashAttribute("successMessage", "Užsakymas sukurtas sėkmingai!");
        return "redirect:/dashboard";
    }

    private Order convertDtoToEntity(OrderFormDTO dto) {
        Order order = new Order();
        order.setOrderName(dto.getOrderName());

        List<PartGroup> groupEntities = new ArrayList<>();
        for (PartGroupDTO groupDTO : dto.getPartGroups()) {
            PartGroup group = new PartGroup();
            group.setMaterial(groupDTO.getMaterial());
            group.setPaintColor(groupDTO.getPaintColor());
            group.setOrder(order);

            List<Part> parts = new ArrayList<>();
            for (PartDTO partDTO : groupDTO.getParts()) {
                Part part = new Part();
                part.setLength(partDTO.getLength());
                part.setWidth(partDTO.getWidth());
                part.setThickness(partDTO.getThickness());
                part.setAmount(partDTO.getAmount());
                part.setPaintedArea(partDTO.getPaintedArea());
                part.setPartGroup(group);
                parts.add(part);
            }

            group.setParts(parts);
            groupEntities.add(group);
        }

        order.setPartGroups(groupEntities);
        return order;
    }

    @GetMapping("/{id}")
    public String viewOrder(@PathVariable Long id, Model model) {
        Order order = orderService.getOrderById(id).orElse(null);

        if (order == null) {
            return "redirect:/dashboard";
        }

        double totalArea = order.getPartGroups().stream()
                .flatMap(group -> group.getParts().stream())
                .mapToDouble(this::calculatePaintedArea)
                .sum();

        Map<String, Double> areaByColor = order.getPartGroups().stream()
                .collect(Collectors.groupingBy(
                        PartGroup::getPaintColor,
                        Collectors.summingDouble(group ->
                                group.getParts().stream()
                                        .mapToDouble(this::calculatePaintedArea)
                                        .sum()
                        )
                ));

        Set<Double> thicknesses = order.getPartGroups().stream()
                .flatMap(group -> group.getParts().stream())
                .map(Part::getThickness)
                .collect(Collectors.toSet());

        Set<String> paintedAreas = order.getPartGroups().stream()
                .flatMap(group -> group.getParts().stream())
                .map(part -> part.getPaintedArea().name())
                .collect(Collectors.toSet());

        model.addAttribute("order", order);
        model.addAttribute("totalArea", totalArea);
        model.addAttribute("areaByColor", areaByColor);
        model.addAttribute("thicknesses", thicknesses);
        model.addAttribute("paintedAreas", paintedAreas);

        return "orders/view";
    }

    private double calculatePaintedArea(Part part) {
        double length = part.getLength() / 1000.0;
        double width = part.getWidth() / 1000.0;
        double thickness = part.getThickness() / 1000.0;

        return switch (part.getPaintedArea()) {
            case ALL_SIDES -> 2 * (length * width + length * thickness + width * thickness);
            case ONE_EDGE -> thickness * width;
            case TWO_EDGES -> 2 * thickness * width;
            case THREE_EDGES -> 2 * (length * thickness + width * thickness) + thickness * width;
            case ONE_SIDE_NO_EDGES -> length * width;
            case ONE_SIDE_WITH_EDGES -> length * width + 2 * (length * thickness + width * thickness);
            default -> 0;
        };
    }
}
