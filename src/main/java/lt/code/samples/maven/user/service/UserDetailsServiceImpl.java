package lt.code.samples.maven.user.service;

import lombok.RequiredArgsConstructor;
import lt.code.samples.maven.user.model.UserEntity;
import lt.code.samples.maven.user.repository.UserRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
@Primary
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        return userRepository.findUserByUsernameWithAuthorities(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User with username " +
                                username + " not found"));
    }

    public void deleteUserById(Long id) {
        UserEntity user = userRepository.findById(id).orElseThrow();
        user.getAuthorities().clear();  // Pašalinam roles
        userRepository.save(user);      // Išsaugom naudotoją be roles
        userRepository.delete(user);    // Tada trinam
    }

}
