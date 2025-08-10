package lt.code.samples.maven.common.web;

import lt.code.samples.maven.common.exception.NotFoundException;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Order(1)
@RestControllerAdvice(annotations = RestController.class)
public class ApiExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleNotFoundRest(NotFoundException ex) {
        String msg = ex.getMessage() != null ? ex.getMessage() : "Not found";
        return ResponseEntity.status(404).body(msg);
    }
}
