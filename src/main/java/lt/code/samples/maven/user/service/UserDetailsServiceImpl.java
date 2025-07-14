package lt.code.samples.maven.security.service;

import lombok.RequiredArgsConstructor;
import lt.code.samples.maven.user.repository.UserRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        return userRepository.findUserByUsernameWithAuthorities(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User with username " +
                                username + " not found"));
    }
}
