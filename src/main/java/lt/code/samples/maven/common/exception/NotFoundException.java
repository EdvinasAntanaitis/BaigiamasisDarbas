package lt.code.samples.maven.common.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) { super(message); }
}
