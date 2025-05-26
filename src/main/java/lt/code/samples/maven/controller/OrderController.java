package lt.code.samples.maven.controller;

import lt.code.samples.maven.dto.OrderFormDTO;
import lt.code.samples.maven.dto.PartDTO;
import lt.code.samples.maven.dto.PartGroupDTO;
import lt.code.samples.maven.order.Order;
import lt.code.samples.maven.order.Part;
import lt.code.samples.maven.order.PartGroup;
import lt.code.samples.maven.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

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
    @GetMapping("/orders/new")
    public String showNewOrderForm(Model model) {
        model.addAttribute("orderForm", new OrderFormDTO());
        return "orders/new";
    }

    //naujai sukurta
    @PostMapping("/orders/new")
    public String submitNewOrder(@ModelAttribute OrderFormDTO orderForm,
                                 RedirectAttributes redirectAttributes) {
        return "redirect:/orders/new";
    }
}
