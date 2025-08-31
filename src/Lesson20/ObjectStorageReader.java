package Lesson20;

import Lesson20.Exceptions.ObjectNotFoundException;

import java.io.IOException;
import java.util.List;

public interface ObjectStorageReader {
    byte[] read(String namespace, String name) throws ObjectNotFoundException, IOException;
    List<byte[]> read(String namespace, String name, int chunkSize) throws ObjectNotFoundException, IOException;
}
