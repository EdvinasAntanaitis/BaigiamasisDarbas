package lt.code.samples.maven.security.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lt.code.samples.maven.user.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Log4j2
@Configuration
@RequiredArgsConstructor
public class BasicSecurityConfig {

    private final DataSource datasource;
    private final UserService userService;
    private final ApplicationUsersPropertyConfig applicationUsersPropertyConfig;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests.requestMatchers("/login/**").permitAll()
                                .anyRequest()
                                .authenticated())
                .formLogin(formLogin ->
                        formLogin
                                .loginPage("/login")
                                .defaultSuccessUrl("/dashboard", true)
                                .permitAll()
                )
                .build();
    }

    @Bean
    public UserDetailsService inMemoryUserDetailsService(){
        final PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        return new InMemoryUserDetailsManager(
                applicationUsersPropertyConfig.getUsers().stream()
                        .map(user -> {
                            log.info("Creating global user: {}", user);

                            return User.withUsername(user.username())
                                    .password(user.password())
                                    .roles(user.roles())
                                    .build();
                        })
                        .toList()
        );
    }

    @Bean
    public UserDetailsService jdbcUserDetailsService() {
        var users = new JdbcUserDetailsManager(datasource);
        users.setUsersByUsernameQuery("SELECT username, password, TRUE AS enabled FROM users WHERE username = ?");
        users.setAuthoritiesByUsernameQuery("SELECT username, 'ROLE_ADMIN' AS authority FROM users WHERE username = ?");


        return users;
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        var daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());

        return daoAuthenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
