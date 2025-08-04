package lt.code.samples.maven.user.service;

import lombok.RequiredArgsConstructor;
import lt.code.samples.maven.user.dto.UserDto;
import lt.code.samples.maven.user.model.AuthorityEntity;
import lt.code.samples.maven.user.model.UserEntity;
import lt.code.samples.maven.user.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void updateUser(Long id, String firstName, String lastName,
                           String email, String password, String role) {

        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);

        if (password != null && !password.isBlank()) {
            user.setPassword(passwordEncoder.encode(password));
        }

        userRepository.save(user);
    }

    public UserDto getUserDtoByUsername(String username) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return UserDto.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getAuthorities().stream()
                        .findFirst()
                        .map(AuthorityEntity::getAuthority)
                        .orElse("USER"))
                .build();
    }

    @Transactional
    public void deleteUserById(Long id) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        UserEntity currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new RuntimeException("Current user not found"));

        if (currentUser.getId() == id) {
            throw new RuntimeException("Cannot delete yourself.");
        }

        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 🧹 Išvalom roles iš vartotojo
        user.getAuthorities().clear();
        userRepository.save(user); // būtina, kad būtų atnaujintas tarp lentelių ryšys

        // 🗑 Dabar galime ištrinti naudotoją
        userRepository.delete(user);
    }


}


