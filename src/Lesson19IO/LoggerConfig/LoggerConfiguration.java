package Lesson19IO.LoggerConfig;

import Lesson19IO.Logger.LoggingLevel;

public interface LoggerConfiguration {
    LoggingLevel getLevel();
    String getPattern();
}
