package lt.code.samples.maven.order.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lt.code.samples.maven.order.dto.OrderFormDTO;
import lt.code.samples.maven.order.service.NewOrderService;
import lt.code.samples.maven.order.service.OrderFormFactory;
import lt.code.samples.maven.order.service.OrderQueryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final NewOrderService newOrderService;
    private final OrderQueryService orderQueryService;
    private final OrderFormFactory orderFormFactory;

    @GetMapping("/new")
    public String showNewOrderForm(Model model) {
        model.addAttribute("orderForm", orderFormFactory.emptyFormWithOneGroupAndPart());
        return "orders/new";
    }

    @PostMapping("/new")
    public String submitOrder(@Valid @ModelAttribute("orderForm") OrderFormDTO orderForm,
                              BindingResult br,
                              RedirectAttributes ra,
                              Model model) {
        if (br.hasErrors()) return "orders/new";

        newOrderService.createOrderFromDTO(orderForm);
        ra.addFlashAttribute("successMessage", "Order created successfully!");
        return "redirect:/dashboard";
    }

    @GetMapping
    public String listOrders(Model model) {
        model.addAttribute("orders", orderQueryService.findAll());
        return "orders/list";
    }
}
