package Lesson22.Task1;

import java.util.ArrayList;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ThreadSafeList<T> {

    private final ReadWriteLock mutex = new ReentrantReadWriteLock();
    private final ArrayList<T> list = new ArrayList<T>();


    public void add(T item) {
        mutex.writeLock().lock();
        try {
            list.add(item);
        } finally {
            mutex.writeLock().unlock();
        }
    }

    public boolean remove(T item) {
        mutex.writeLock().lock();
        try {
            return list.remove(item);
        } finally {
            mutex.writeLock().unlock();
        }
    }

    public T remove(int index) {
        mutex.writeLock().lock();
        try {
            return list.remove(index);
        } finally {
            mutex.writeLock().unlock();
        }
    }

    public T get(int index) {
        mutex.readLock().lock();
        try {
            if (index < 0 || index >= list.size()) {
                throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + list.size());
            }
            return list.get(index);
        } finally {
            mutex.readLock().unlock();
        }
    }

    @Override
    public String toString() {
        mutex.readLock().lock();
        try {
            return "Task1.ThreadSafeList" + list.toString();
        } finally {
            mutex.readLock().unlock();
        }
    }

}
