package lt.code.samples.maven.controller;

import jakarta.servlet.http.HttpSession;
import lt.code.samples.maven.utility.SessionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        if (!SessionUtils.isLoggedIn(session)) {
            return "redirect:/login";
        }

        model.addAttribute("user", session.getAttribute("user"));
        return "dashboard";
    }
}
