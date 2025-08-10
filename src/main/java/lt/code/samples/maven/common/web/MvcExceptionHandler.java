package lt.code.samples.maven.common.web;

import lt.code.samples.maven.common.exception.NotFoundException;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Order(1)
@ControllerAdvice(annotations = Controller.class)
public class MvcExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public String handleNotFoundMvc(NotFoundException ex, RedirectAttributes ra) {
        ra.addFlashAttribute("error", ex.getMessage() != null ? ex.getMessage() : "Not found");
        return "redirect:/dashboard";
    }
}
