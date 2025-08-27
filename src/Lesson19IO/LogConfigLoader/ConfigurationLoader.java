package Lesson19IO.LogConfigLoader;

public interface ConfigurationLoader<T> {
    T load(String configFilePath) throws Exception;
}
