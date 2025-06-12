package lt.code.samples.maven.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lt.code.samples.maven.user.dto.UserSignUpDto;
import lt.code.samples.maven.user.service.UserRegistrationService;
import lt.code.samples.maven.user.validator.UserSignupValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static lt.code.samples.maven.HttpEndpoint.USERS_SIGN_UP;

@Controller
@RequestMapping(USERS_SIGN_UP)
@RequiredArgsConstructor
public class UserProfileController {

    public static final String USER_SIGN_UP_VIEW = "user/newuser";

    private final UserSignupValidator userSignupValidator;
    private final UserRegistrationService userRegistrationService;

    @GetMapping("/profilemanagement")
    public String profileManagement(Model model) {
        model.addAttribute("users", userRegistrationService.findAllUsers());
        return "user/profilemanagement";
    }

    @GetMapping("/newuser")
    public String showNewUserForm(Model model) {
        model.addAttribute("userSignUpDto", UserSignUpDto.builder().build());
        return USER_SIGN_UP_VIEW;
    }

    @PostMapping("/newuser")
    public String createUser(@Valid UserSignUpDto userSignUpDto,
                             BindingResult errors,
                             Model model,
                             RedirectAttributes redirectAttributes) {

        userSignupValidator.validate(userSignUpDto, errors);

        if (errors.hasErrors()) {
            model.addAttribute("userSignUpDto", userSignUpDto);
            return "user/newuser";
        }


        userRegistrationService.createUser(userSignUpDto);
        redirectAttributes.addFlashAttribute("message", "User was successfully created.");
        return "redirect:/user/profilemanagement";
    }

    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        userRegistrationService.deleteUserById(id);
        redirectAttributes.addFlashAttribute("message", "User deleted successfully.");
        return "redirect:/user/profilemanagement";
    }
}
