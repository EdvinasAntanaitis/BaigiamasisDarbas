package lt.code.samples.maven.config;

import lt.code.samples.maven.logIn.User;
import lt.code.samples.maven.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initUsers(UserRepository userRepo, BCryptPasswordEncoder encoder) {
        return args -> {
            if (userRepo.findByUsername("admin").isEmpty()) {
                User user = new User();
                user.setFullName("Admin Adminson");
                user.setUsername("admin");
                user.setPassword(encoder.encode("admin123"));
                user.setRole("ADMIN");
                user.setRights("ALL");

                userRepo.save(user);
                System.out.println("✅ Admin user created: admin / admin123");
            } else {
                System.out.println("ℹ️ Admin user already exists");
            }
        };
    }
}
