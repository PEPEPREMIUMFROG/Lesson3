package Lesson20;

import Lesson20.Exceptions.ObjectNotFoundException;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class FileStorageReader implements ObjectStorageReader {

    private final FileStorage storage;

    public FileStorageReader(FileStorage storage) {
        this.storage = storage;
    }

    @Override
    public byte[] read(String namespace, String name) throws ObjectNotFoundException {
        Path filePath = storage.get(namespace, name);
        try (FileChannel channel = FileChannel.open(filePath, StandardOpenOption.READ)) {
            long fileSize = channel.size();
            Validator.validateFileSize(fileSize);
            ByteBuffer buffer = ByteBuffer.allocate((int) fileSize);
            while (buffer.hasRemaining()) {
                if (channel.read(buffer) == -1) {
                    throw new IOException("Unexpected end of file");
                }
            }
            buffer.flip();
            byte[] data = new byte[buffer.remaining()];
            buffer.get(data);
            return data;
        } catch (IOException e) {
            throw new ObjectNotFoundException("Error reading file: " + filePath, e);
        }
    }

    @Override
    public List<byte[]> read(String namespace, String name, int chunkSize) throws ObjectNotFoundException {
        Validator.validateChunkSize(chunkSize);
        Path filePath = storage.get(namespace, name);
        try (FileChannel channel = FileChannel.open(filePath, StandardOpenOption.READ)) {
            List<byte[]> chunks = new ArrayList<>();
            ByteBuffer buffer = ByteBuffer.allocate(chunkSize);
            while (channel.read(buffer) > 0) {
                buffer.flip();
                byte[] chunk = new byte[buffer.remaining()];
                buffer.get(chunk);
                chunks.add(chunk);
                buffer.clear();
            }
            return chunks;
        } catch (IOException e) {
            throw new RuntimeException("Error reading file in chunks: " + filePath, e);
        }
    }
}
