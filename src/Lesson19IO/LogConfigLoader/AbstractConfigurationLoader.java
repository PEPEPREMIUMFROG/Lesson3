package Lesson19IO.LogConfigLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public abstract class AbstractConfigurationLoader<T> implements ConfigurationLoader<T> {
    protected Scanner readFile(String filePath) throws FileNotFoundException {
        return new Scanner(new File(filePath));
    }

    protected abstract T parse(Scanner scanner) throws Exception;

    @Override
    public T load(String configFilePath) throws Exception {
        try (Scanner scanner = readFile(configFilePath)) {
            return parse(scanner);
        }
    }
}
