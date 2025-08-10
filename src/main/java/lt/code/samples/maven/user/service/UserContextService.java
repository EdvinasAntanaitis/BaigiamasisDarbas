package lt.code.samples.maven.user.service;

import lt.code.samples.maven.user.model.UserEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserContextService {

    public String currentUserFullName() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof UserEntity user)) {
            return "Unknown";
        }
        String fn = user.getFirstName() == null ? "" : user.getFirstName();
        String ln = user.getLastName()  == null ? "" : user.getLastName();
        String full = (fn + " " + ln).trim();
        return full.isBlank() ? user.getUsername() : full;
    }
}
