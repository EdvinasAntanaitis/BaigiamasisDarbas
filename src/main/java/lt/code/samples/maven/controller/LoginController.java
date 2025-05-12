//package lt.code.samples.maven.controller;
//
//import org.springframework.ui.Model;
//import lombok.RequiredArgsConstructor;
//import lt.code.samples.maven.repository.UserRepository;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//
//import jakarta.servlet.http.HttpSession;
//
//@Controller
//@RequiredArgsConstructor
//public class LoginController {
//
//    private final UserRepository userRepo;
//    private final BCryptPasswordEncoder passwordEncoder;
//
//    @GetMapping("/login")
//    public String showLoginForm() {
//        return "login";
//    }
//
//    @PostMapping("/login")
//    public String login(@RequestParam String username,
//                        @RequestParam String password,
//                        HttpSession session,
//                        Model model) {
//
//        return userRepo.findByUsername(username)
//                .map(user -> {
//                    if (passwordEncoder.matches(password, user.getPassword())) {
//                        session.setAttribute("user", user);
//                        return "redirect:/dashboard";
//                    } else {
//                        model.addAttribute("error", "Invalid password");
//                        return "login";
//                    }
//                })
//                .orElseGet(() -> {
//                    model.addAttribute("error", "User not found");
//                    return "login";
//                });
//    }
//
//    @GetMapping("/logout")
//    public String logout(HttpSession session) {
//        session.invalidate();
//        return "redirect:/login";
//    }
//}
