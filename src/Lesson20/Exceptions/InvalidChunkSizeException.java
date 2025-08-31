package Lesson20.Exceptions;

public class InvalidChunkSizeException extends IllegalArgumentException {
    public InvalidChunkSizeException(String s) {
        super(s);
    }
}
