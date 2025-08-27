package Lesson19IO.LoggerConfiguration;

import Lesson19IO.Logger.LoggingLevel;

public interface LoggerConfiguration {
    LoggingLevel getLevel();
    String getPattern();
}
