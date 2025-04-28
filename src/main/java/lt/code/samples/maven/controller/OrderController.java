package lt.code.samples.maven.controller;

import jakarta.servlet.http.HttpSession;
import lt.code.samples.maven.order.Order;
import lt.code.samples.maven.repository.OrderRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderRepository orderRepo;

    public OrderController(OrderRepository orderRepo) {
        this.orderRepo = orderRepo;
    }

    @GetMapping
    public String listOrders(Model model, HttpSession session) {
        if (session.getAttribute("user") == null) return "redirect:/login";
        model.addAttribute("orders", orderRepo.findAll());
        return "orders/list";
    }

    @GetMapping("/{id}")
    public String viewOrder(@PathVariable Long id, Model model, HttpSession session) {
        if (session.getAttribute("user") == null) return "redirect:/login";

        Optional<Order> orderOpt = orderRepo.findById(id);
        if (orderOpt.isEmpty()) return "redirect:/orders";

        model.addAttribute("order", orderOpt.get());
        return "orders/view";
    }
}
