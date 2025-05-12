package lt.code.samples.maven.controller;

import jakarta.servlet.http.HttpSession;
import lt.code.samples.maven.dto.OrderFormDTO;
import lt.code.samples.maven.order.Order;
import lt.code.samples.maven.repository.OrderRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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

    @GetMapping("/new")
    public String showNewOrderForm(Model model, HttpSession session) {
        if (session.getAttribute("user") == null) return "redirect:/login";
        model.addAttribute("orderForm", new OrderFormDTO());
        return "orders/new";
    }

    @PostMapping("/new")
    public String createOrder(@ModelAttribute("orderForm") OrderFormDTO orderForm, HttpSession session) {
        if (session.getAttribute("user") == null) return "redirect:/login";

        Order order = new Order();
        order.setOrderName(orderForm.getOrderName());
        order.setPaintColor(orderForm.getPaintColor());
        order.setMaterial(orderForm.getMaterial());
        order.setDescription(orderForm.getDescription());
        order.setCreationDate(LocalDateTime.now());

        orderRepo.save(order);

        return "redirect:/orders";
    }
}

