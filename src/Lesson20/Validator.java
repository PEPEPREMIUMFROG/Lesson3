package Lesson20;

import Lesson20.Exceptions.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Validator {

    private static final long MAX_FILE_SIZE = (long) Integer.MAX_VALUE - 8;

    public static void validateParameters(String namespace, String name) {
        if (namespace == null || name == null) {
            throw new InvalidParameterException("Namespace and name must not be null");
        }
        if (namespace.isEmpty() || name.isEmpty()) {
            throw new InvalidParameterException("Namespace and name must not be empty");
        }
        validateNoPathTraversal(namespace);
        validateNoPathTraversal(name);
    }

    private static void validateNoPathTraversal(String pathComponent) {
        Path path = Paths.get(pathComponent).normalize();
        if (path.isAbsolute()) {
            throw new PathTraversalException("Absolute path is not allowed: " + pathComponent);
        }
        if (path.toString().contains("\0")) {
            throw new PathTraversalException("Path contains invalid characters: " + pathComponent);
        }
        Path normalized = Paths.get(pathComponent).normalize();
        if (!normalized.toString().equals(pathComponent) ||
                normalized.startsWith("..") ||
                normalized.toString().contains(File.separator + "..")) {
            throw new PathTraversalException("Path traversal attempt detected: " + pathComponent);
        }
    }

    public static void validateFileExists(Path filePath) throws ObjectNotFoundException {
        try {
            if (!Files.exists(filePath)) {
                throw new ObjectNotFoundException("File not found: " + filePath);
            }
            if (!Files.isRegularFile(filePath)) {
                throw new ObjectNotFoundException("Path is not a file: " + filePath);
            }
            if (!Files.isReadable(filePath)) {
                throw new ObjectNotFoundException("File is not readable: " + filePath);
            }
        } catch (SecurityException e) {
            throw new ObjectNotFoundException("Access denied to file: " + filePath, e);
        }
    }

    public static void validateFileSize(long fileSize) throws FileTooLargeException {
        if (fileSize > MAX_FILE_SIZE) {
            throw new FileTooLargeException(
                    "File size " + fileSize + " exceeds maximum allowed size " + MAX_FILE_SIZE);
        }
    }

    public static void validateChunkSize(int chunkSize) throws InvalidChunkSizeException {
        if (chunkSize <= 0) {
            throw new InvalidChunkSizeException("Chunk size must be positive, but was: " + chunkSize);
        }
        if (chunkSize > MAX_FILE_SIZE) {
            throw new InvalidChunkSizeException(
                    "Chunk size " + chunkSize + " is too large (max allowed: " + MAX_FILE_SIZE + ")");
        }
    }
}