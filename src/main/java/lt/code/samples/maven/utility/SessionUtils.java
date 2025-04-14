package lt.code.samples.maven.utility;

import jakarta.servlet.http.HttpSession;
import lt.code.samples.maven.logIn.User;

public class SessionUtils {

    public static boolean isLoggedIn(HttpSession session) {
        return session != null && session.getAttribute("user") instanceof User;
    }
}
