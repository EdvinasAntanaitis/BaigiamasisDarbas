package lt.code.samples.maven.order.controller;

import lombok.RequiredArgsConstructor;
import lt.code.samples.maven.order.model.OrderEntity;
import lt.code.samples.maven.order.service.OrderPageService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@Controller
@RequestMapping("/orders")
@RequiredArgsConstructor
public class ViewOrderController {

    private final OrderPageService orderPageService;

    @GetMapping("/{id}")
    public String viewOrder(@PathVariable UUID id, Model model) {

        var v = orderPageService.getViewData(id);

        model.addAttribute("order", v.order());
        model.addAttribute("totalArea", v.totalArea());
        model.addAttribute("areaPerGroup", v.areaPerGroup());

        return "orders/view";
    }

    @PostMapping("/end")
    public String endOrder(@RequestParam UUID orderId,
                           RedirectAttributes ra) {

        try {
            orderPageService.endOrder(orderId);

            ra.addFlashAttribute("message",
                    "Order successfully completed.");

        } catch (lt.code.samples.maven.common.exception.ConflictException e) {

            ra.addFlashAttribute("error",
                    "Can't complete this order. Some tasks are still in progress or there are unresolved faults.");
        }

        return "redirect:/orders/worklog?orderId=" + orderId;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/delete")
    public String deleteOrder(@PathVariable UUID id,
                              RedirectAttributes ra) {

        orderPageService.deleteOrder(id);

        ra.addFlashAttribute("message", "Order deleted.");

        return "redirect:/orders";
    }

    @GetMapping("/{id}/edit")
    @PreAuthorize("hasRole('ADMIN')")
    public String editOrderForm(@PathVariable UUID id,
                                Model model) {

        OrderEntity order = orderPageService.getForEdit(id);

        model.addAttribute("order", order);

        return "orders/editOrder";
    }
}