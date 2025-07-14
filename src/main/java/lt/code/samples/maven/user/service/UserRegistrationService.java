package lt.code.samples.maven.user.service;

import lombok.RequiredArgsConstructor;
import lt.code.samples.maven.user.dto.UserSignUpDto;
import lt.code.samples.maven.user.model.AuthorityEntity;
import lt.code.samples.maven.user.model.UserEntity;
import lt.code.samples.maven.user.repository.AuthorityRepository;
import lt.code.samples.maven.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserRegistrationService {

    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;

    public void createUser(UserSignUpDto dto) {
        AuthorityEntity authority = authorityRepository.findByName(dto.role())
                .orElseThrow(() -> new IllegalArgumentException("Role not found: " + dto.role()));

        UserEntity user = UserEntity.builder()
                .firstName(dto.name())
                .lastName(dto.surname())
                .email(dto.email())
                .username(dto.username())
                .phoneNumber(dto.phoneNumber())
                .password(passwordEncoder.encode(dto.password()))
                .authorities(Set.of(authority))
                .build();

        userRepository.save(user);
    }

    public List<UserEntity> findAllUsers() {
        return userRepository.findAll();
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    public void updateUser(Long id, String firstName, String lastName, String email, String password, String role) {
        UserEntity user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));

        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);

        if (password != null && !password.isBlank()) {
            user.setPassword(passwordEncoder.encode(password)); // Įsitikink, kad turi passwordEncoder!
        }

        // Role keitimas (paprastas variantas)
        user.getAuthorities().clear();
        AuthorityEntity authority = authorityRepository.findByName("ROLE_" + role)
                .orElseThrow(() -> new RuntimeException("Role not found: " + role));
        user.getAuthorities().add(authority);

        userRepository.save(user);
    }

}


