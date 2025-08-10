package lt.code.samples.maven.order.controller;

import lombok.RequiredArgsConstructor;
import lt.code.samples.maven.order.model.OrderEntity;
import lt.code.samples.maven.order.service.OrderPageService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/orders")
@RequiredArgsConstructor
public class ViewOrderController {

    private final OrderPageService orderPageService;

    @GetMapping("/{id:[0-9]+}")
    public String viewOrder(@PathVariable Long id, Model model) {
        var v = orderPageService.getViewData(id);
        model.addAttribute("order", v.order());
        model.addAttribute("totalArea", v.totalArea());
        model.addAttribute("areaPerGroup", v.areaPerGroup());
        return "orders/view";
    }

    @PostMapping("/end")
    public String endOrder(@RequestParam Long orderId, RedirectAttributes ra) {
        orderPageService.endOrder(orderId);
        ra.addFlashAttribute("message", "Order ended successfully.");
        return "redirect:/orders";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id:[0-9]+}/delete")
    public String deleteOrder(@PathVariable Long id, RedirectAttributes ra) {
        orderPageService.deleteOrder(id);
        ra.addFlashAttribute("message", "Order deleted.");
        return "redirect:/orders";
    }

    @GetMapping("/{id:[0-9]+}/edit")
    @PreAuthorize("hasRole('ADMIN')")
    public String editOrderForm(@PathVariable Long id, Model model) {
        OrderEntity order = orderPageService.getForEdit(id);
        model.addAttribute("order", order);
        return "orders/editOrder";
    }
}
