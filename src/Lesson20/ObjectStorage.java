package Lesson20;

import Lesson20.Exceptions.ObjectNotFoundException;

public interface ObjectStorage <T>{
    void put(String namespace, String name, T object);
    T get(String namespace, String name) throws ObjectNotFoundException;
}
