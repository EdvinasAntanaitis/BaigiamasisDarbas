package lt.code.samples.maven.user.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lt.code.samples.maven.user.dto.UserDto;
import lt.code.samples.maven.user.dto.UserUpdateDto;
import lt.code.samples.maven.user.model.AuthorityEntity;
import lt.code.samples.maven.user.model.UserEntity;
import lt.code.samples.maven.user.repository.AuthorityRepository;
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
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public UserDto getUserDtoByUsername(String username) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String role = user.getAuthorities().stream()
                .findFirst()
                .map(AuthorityEntity::getAuthority)
                .orElse("USER");

        return UserDto.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(role)
                .id(user.getId())
                .build();
    }


    @Transactional
    public void updateUserAdmin(UserUpdateDto dto) {
        updateUserAdmin(
                Long.parseLong(dto.getId()),
                dto.getEmail(),
                dto.getFirstName(),
                dto.getLastName(),
                dto.getPhoneNumber(),
                normalizeRole(dto.getRole()),
                dto.getPassword()
        );
    }

    @Transactional
    public void updateUserAdmin(Long id,
                                String email,
                                String firstName,
                                String lastName,
                                String phoneNumber,
                                String role,
                                String rawPasswordOrNull) {

        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPhoneNumber(phoneNumber);

        if (rawPasswordOrNull != null && !rawPasswordOrNull.isBlank()) {
            user.setPassword(passwordEncoder.encode(rawPasswordOrNull));
        }

        user.getAuthorities().clear();
        AuthorityEntity authority = authorityRepository.findByName(role)
                .orElseGet(() -> {
                    AuthorityEntity a = new AuthorityEntity();
                    a.setName(role);
                    return authorityRepository.save(a);
                });
        user.getAuthorities().add(authority);

        userRepository.save(user);
    }

    @Transactional
    public void deleteUserById(Long id) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity current = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new EntityNotFoundException("Current user not found"));

        if (current.getId() == id) {
            throw new IllegalArgumentException("Cannot delete yourself.");
        }

        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        user.getAuthorities().clear();
        userRepository.save(user);
        userRepository.delete(user);
    }

    private String normalizeRole(String role) {
        if (role == null) return null;
        return role.startsWith("ROLE_") ? role : "ROLE_" + role;
    }
}
