package lt.code.samples.maven.security.config;

import lombok.Getter;
import lt.code.samples.maven.dto.GlobalUserDto;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Getter
@ConfigurationProperties("global")
@Configuration
public class ApplicationUsersPropertyConfig {

    private final List<GlobalUserDto> users = new ArrayList<>();
}
