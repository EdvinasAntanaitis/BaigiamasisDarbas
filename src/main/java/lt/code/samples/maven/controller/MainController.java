package lt.code.samples.maven.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String home() {
        return "redirect:/dashboard"; // or "home" if you have a home.html template
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("message", "Welcome to the Dashboard!");
        return "dashboard"; // this must match dashboard.html in templates
    }

//    @GetMapping("/dashboard")
//    public String dashboard(HttpSession session, Model model) {
//        if (!SessionUtils.isLoggedIn(session)) {
//           return "redirect:/login";
//       }
//
//        model.addAttribute("user", session.getAttribute("user"));
//        return "dashboard";
//    }
}
