package Lesson20.Exceptions;

import java.io.IOException;

public class ObjectNotFoundException extends RuntimeException {
    public ObjectNotFoundException(String message) {
        super(message);
    }

    public ObjectNotFoundException(String message, SecurityException e) {
    }

    public ObjectNotFoundException(String message, IOException e) {
    }
}
