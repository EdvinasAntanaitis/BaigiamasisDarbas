package lt.code.samples.maven.worklog.controller;

import lombok.RequiredArgsConstructor;
import lt.code.samples.maven.order.model.OrderEntity;
import lt.code.samples.maven.worklog.service.WorkLogService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/orders")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('USER','ADMIN')")
public class WorkLogPageController {

    private final WorkLogService workLogService;

    @GetMapping("/worklog")
    public String worklogPage(@RequestParam(required = false) Long orderId,
                              @RequestParam(required = false) String orderName,
                              Model model,
                              RedirectAttributes ra) {

        if (orderId == null && (orderName == null || orderName.isBlank())) {
            model.addAttribute("order", null);
            model.addAttribute("worklogs", null);
            return "orders/worklog";
        }

        OrderEntity order = (orderId != null)
                ? workLogService.findOrderById(orderId).orElse(null)
                : workLogService.findOrderByName(orderName.trim()).orElse(null);

        if (order == null) {
            ra.addFlashAttribute("error", "Order not found.");
            return "redirect:/orders/worklog";
        }

        model.addAttribute("order", order);
        model.addAttribute("worklogs", order.getWorkLogs());
        return "orders/worklog";
    }
}
