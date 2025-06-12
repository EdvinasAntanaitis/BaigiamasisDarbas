package lt.code.samples.maven.user.validator;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.RequiredArgsConstructor;
import lt.code.samples.maven.user.dto.UserSignUpDto;
import lt.code.samples.maven.user.repository.UserRepository;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class UserSignupValidator implements Validator {

    private static final String EMAIL_REGEX = "^((?!\\.)[\\w\\-_.]*[^.])(@\\w+)(\\.\\w+(\\.\\w+)?[^.\\W])$";
    private static final Set<String> ALLOWED_ROLES = Set.of("ROLE_ADMIN", "ROLE_WORKER", "ROLE_CLIENT");

    private final UserRepository userRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return UserSignUpDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (!(target instanceof UserSignUpDto userSignUpDto)) {
            return;
        }

        if (!isValidEmail(userSignUpDto.email())) {
            errors.rejectValue("email", "user.signup.email.invalid", "Invalid email format.");
        }

        if (userRepository.findByUsername(userSignUpDto.username()).isPresent()) {
            errors.rejectValue("username", "user.signup.username.exists", "Username already exists.");
        }

        if (userSignUpDto.password() == null || !userSignUpDto.password().equals(userSignUpDto.passwordRepeat())) {
            errors.rejectValue("passwordRepeat", "passwords.not.match", "Passwords do not match.");
        }

        if (userSignUpDto.role() == null || userSignUpDto.role().isBlank()) {
            errors.rejectValue("role", "role.empty", "Please select a role.");
        } else if (!ALLOWED_ROLES.contains(userSignUpDto.role())) {
            errors.rejectValue("role", "role.invalid", "Selected role is not valid.");
        }
    }

    private boolean isValidEmail(String email) {
        final Pattern pattern = Pattern.compile(EMAIL_REGEX);
        final Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}

