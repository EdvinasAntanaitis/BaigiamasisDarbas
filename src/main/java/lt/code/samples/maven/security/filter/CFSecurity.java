package lt.code.samples.maven.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import java.io.IOException;
import jakarta.servlet.Filter;

@Component
@Log4j2
public class CFSecurity implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, SecurityException, ServletException {
        log.info("CFSecurity filter");
        filterChain.doFilter(servletRequest, servletResponse);
        log.info("CFSecurity filter end");
        }
}
