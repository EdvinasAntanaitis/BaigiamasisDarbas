package lt.code.samples.maven.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lt.code.samples.maven.user.dto.UserSignUpDto;
import lt.code.samples.maven.user.dto.UserUpdateDto;
import lt.code.samples.maven.user.service.UserRegistrationService;
import lt.code.samples.maven.user.service.UserService;
import lt.code.samples.maven.user.validator.UserSignupValidator;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
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
    private final UserService userService;

    @InitBinder("userSignUpDto")
    void bindSignupValidator(WebDataBinder binder) {
        binder.addValidators(userSignupValidator);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/profilemanagement")
    public String profileManagement(Model model) {
        model.addAttribute("users", userRegistrationService.findAllUsers());
        return "user/profilemanagement";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/newuser")
    public String showNewUserForm(Model model) {
        model.addAttribute("userSignUpDto", UserSignUpDto.builder().build());
        return USER_SIGN_UP_VIEW;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/newuser")
    public String createUser(@Valid @ModelAttribute("userSignUpDto") UserSignUpDto dto,
                             BindingResult br,
                             RedirectAttributes ra) {
        if (br.hasErrors()) {
            return USER_SIGN_UP_VIEW;
        }
        userRegistrationService.createUser(dto);
        ra.addFlashAttribute("message", "User was successfully created.");
        return "redirect:/user/profilemanagement";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id, RedirectAttributes ra) {
        userService.deleteUserById(id);
        ra.addFlashAttribute("message", "User deleted successfully.");
        return "redirect:/user/profilemanagement";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/edit")
    public String updateUser(@Valid @ModelAttribute UserUpdateDto dto,
                             BindingResult br,
                             RedirectAttributes ra) {
        if (br.hasErrors()) {
            ra.addFlashAttribute("error", "Please fix form errors and try again.");
            return "redirect:/user/profilemanagement";
        }
        userService.updateUserAdmin(dto);
        ra.addFlashAttribute("message", "User updated successfully.");
        return "redirect:/user/profilemanagement";
    }
}
