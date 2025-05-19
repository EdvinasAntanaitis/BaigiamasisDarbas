package lt.code.samples.maven.controller;

import lt.code.samples.maven.dto.MaterialGroupDTO;
import lt.code.samples.maven.dto.OrderFormDTO;
import lt.code.samples.maven.dto.PartDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String home() {
        return "redirect:/dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("message", "Welcome to the Dashboard!");
        return "dashboard";
    }

    @GetMapping("/orders/new")
    public String showNewOrderForm(Model model) {
        OrderFormDTO form = new OrderFormDTO();

        MaterialGroupDTO group = new MaterialGroupDTO();
        group.getParts().add(new PartDTO());

        form.getPartGroups().add(group);

        model.addAttribute("orderForm", form);
        return "orders/new";
    }
}

