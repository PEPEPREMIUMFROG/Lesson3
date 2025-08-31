package Lesson20.Exceptions;

import java.io.IOException;

public class StorageInitializationException extends Exception {
    public StorageInitializationException(String message) {
        super(message);
    }

    public StorageInitializationException(String message, IOException e) {
    }
}
