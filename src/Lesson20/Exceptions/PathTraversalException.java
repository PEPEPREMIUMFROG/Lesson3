package Lesson20.Exceptions;

public class PathTraversalException extends SecurityException {
    public PathTraversalException(String s) {
        super(s);
    }
}
