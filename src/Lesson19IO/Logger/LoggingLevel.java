package Lesson19IO.Logger;

public enum LoggingLevel {
    INFO,
    DEBUG;

    public boolean allows(LoggingLevel requestedLevel) {
        return this == requestedLevel || (this == DEBUG && requestedLevel == INFO);
    }
}

