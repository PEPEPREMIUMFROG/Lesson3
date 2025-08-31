package Lesson20;

import Lesson20.Exceptions.ObjectNotFoundException;
import Lesson20.Exceptions.PathTraversalException;
import Lesson20.Exceptions.StorageInitializationException;

import java.io.IOException;
import java.nio.file.*;

public class FileStorage implements ObjectStorage<Path> {

    private final Path rootDir;

    public FileStorage(String rootDirPath) throws StorageInitializationException {
        this.rootDir = Paths.get(rootDirPath).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.rootDir);
        } catch (IOException e) {
            throw new StorageInitializationException("Failed to create root directory at " + this.rootDir, e);
        }
    }

    @Override
    public void put(String namespace, String name, Path obj) {
        Path filePath = getCorrectFilePath(namespace,name,rootDir);
        try {
            Files.createDirectories(filePath.getParent());
        } catch (IOException e) {
            throw new RuntimeException("Cannot create directory: " + filePath.getParent(), e);
        }
        try {
            Files.copy(obj, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            throw new RuntimeException("Error writing file: " + filePath, ex);
        }
    }

    @Override
    public Path get(String namespace, String name) throws ObjectNotFoundException {
        Path filePath = getCorrectFilePath(namespace,name,rootDir);
        Validator.validateFileExists(filePath);
        return filePath;
    }

    private Path getCorrectFilePath(String namespace, String name, Path rootDir){
        Validator.validateParameters(namespace, name);
        Path namespacePath = Paths.get(namespace).normalize();
        Path filePath = rootDir.resolve(namespacePath).resolve(name).normalize();
        Path rootDirNormalized = rootDir.normalize();
        Path filePathNormalized = filePath.normalize();
        if (!filePathNormalized.startsWith(rootDirNormalized)) {
            throw new PathTraversalException(
                    "Path traversal attempt: " + filePath + " (root: " + rootDir + ")");
        }
        return filePath;
    }

}