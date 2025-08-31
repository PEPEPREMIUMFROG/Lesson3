package Lesson20.Exceptions;

public class InvalidParameterException extends IllegalArgumentException {
    public InvalidParameterException(String s) {
        super(s);
    }
}
