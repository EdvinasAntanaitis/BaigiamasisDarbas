package lt.code.samples.maven.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lt.code.samples.maven.user.dto.UserSignUpDto;
import lt.code.samples.maven.user.model.AuthorityEntity;
import lt.code.samples.maven.user.model.UserEntity;
import lt.code.samples.maven.user.repository.AuthorityRepository;
import lt.code.samples.maven.user.repository.UserRepository;
import lt.code.samples.maven.user.service.UserRegistrationService;
import lt.code.samples.maven.user.service.UserService;
import lt.code.samples.maven.user.validator.UserSignupValidator;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;


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
        userService.deleteUserById(id);
        redirectAttributes.addFlashAttribute("message", "User deleted successfully.");
        return "redirect:/user/profilemanagement";
    }

    @PostMapping("/edit")
    public String updateUser(@RequestParam Long id,
                             @RequestParam String email,
                             @RequestParam String firstName,
                             @RequestParam String lastName,
                             @RequestParam String phoneNumber,
                             @RequestParam String role,
                             @RequestParam(required = false) String password) {

        UserEntity user = userRepository.findById(id).orElseThrow();

        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPhoneNumber(phoneNumber);

        if (password != null && !password.isBlank()) {
            user.setPassword(passwordEncoder.encode(password));
        }

        user.getAuthorities().clear();

        AuthorityEntity authority = authorityRepository.findByName(role.toUpperCase())
                .orElseGet(() -> {
                    AuthorityEntity newAuthority = new AuthorityEntity();
                    newAuthority.setName("ROLE_" + role.toUpperCase());
                    return authorityRepository.save(newAuthority);
                });

        user.getAuthorities().add(authority);

        userRepository.save(user);

        return "redirect:/user/profilemanagement";
    }




}
