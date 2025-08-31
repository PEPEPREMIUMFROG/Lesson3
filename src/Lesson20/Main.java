package Lesson20;

import Lesson20.Exceptions.ObjectNotFoundException;
import Lesson20.Exceptions.StorageInitializationException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Main {

    private static final String ROOT_DIR = "src/Lesson20/storage";
    private static final String TEST_IMAGE_PATH = "res/Lesson20/testImage.png";
    private static final String TEST_FILE_PATH = "res/Lesson20/testFile.txt";


    public static void main(String[] args) {
        try {
            FileStorage fileStorage = new FileStorage(ROOT_DIR);
            FileStorageReader reader = new FileStorageReader(fileStorage);
            demonstrateGet(fileStorage);
            readTxtFile(reader);
            readImgFile(reader, fileStorage);
            getNonExistsFile(fileStorage);
        } catch (StorageInitializationException | ObjectNotFoundException e) {
            System.err.println("Критическая ошибка: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Необработанная ошибка: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void demonstrateGet(FileStorage fileStorage) {
        System.out.println("\nDemonstrate FileStorage put() and get()");
        Path sourcePath = Paths.get(TEST_FILE_PATH);
        fileStorage.put("documents", "example.txt", sourcePath);
        Path puttedFile = Paths.get(ROOT_DIR).resolve("documents/example.txt");
        System.out.printf("Result of fileStorage.put(): %s", puttedFile.toString());

        Path storedFilePath = fileStorage.get("documents", "example.txt");
        System.out.println("\nResult of fileStorage.get():");
        System.out.println("Type: " + storedFilePath.getClass().getSimpleName());
        System.out.println("File path " + storedFilePath);
        System.out.println("File exists: " + Files.exists(storedFilePath));
        System.out.println("Is regular: " + Files.isRegularFile(storedFilePath));
    }

    private static void readTxtFile(FileStorageReader reader) {
        System.out.println("\nDemonstrate read text file");
        byte[] fullContent = reader.read("documents", "example.txt");
        System.out.println("Read all file: " + new String(fullContent));
        List<byte[]> chunks = reader.read("documents", "example.txt", 10);
        System.out.println("Read with 10 bytes:");
        for (int i = 0; i < chunks.size(); i++) {
            System.out.println("Chunk #" + i + ": |" + new String(chunks.get(i)) + "|");
        }
    }

    private static void readImgFile(FileStorageReader reader, FileStorage fileStorage) throws IOException {
        System.out.println("\nDemonstrate read an image file: ");
        Path imageSource = Paths.get(TEST_IMAGE_PATH);

        if (Files.exists(imageSource)) {
            fileStorage.put("images", "test.jpg", imageSource);
            Path imageLocation = fileStorage.get("images", "test.jpg");
            System.out.println("Image path: " + imageLocation);
            System.out.println("Image size: " + Files.size(imageLocation) + " byte");
            List<byte[]> imageChunks = reader.read("images", "test.jpg", 1024);
            System.out.println("Read in " + imageChunks.size() + " chunks of 1 kb");
        } else {
            System.err.println("No image with path: " + TEST_IMAGE_PATH);
        }
    }

    private static void getNonExistsFile(FileStorage fileStorage){
        System.out.println("\nDemonstrate of getting non exists file");
        try {
            fileStorage.get("non-existent", "missing.txt");
        } catch (ObjectNotFoundException e) {
            System.out.println("Error when calling filestorage.get(): " + e.getMessage());
        }
    }
}
