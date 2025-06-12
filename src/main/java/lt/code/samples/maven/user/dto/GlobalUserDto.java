package lt.code.samples.maven.user.dto;

import java.util.StringJoiner;

public record GlobalUserDto(String username,
                            String password,
                            String[] roles) {

    @Override
    public String toString() {
        return new StringJoiner(", ", GlobalUserDto.class.getSimpleName() + "[", "]")
                .add("username='" + username + "'")
                .add("roles=" + String.join(", ", roles))
                .toString();
    }
}
