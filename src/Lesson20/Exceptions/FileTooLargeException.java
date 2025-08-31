package Lesson20.Exceptions;

public class FileTooLargeException extends RuntimeException {
    public FileTooLargeException(String s) {
        super(s);
    }
}
