package org.example;

import jakarta.ws.rs.core.Application;
import org.example.rest.BookResource;
import org.example.rest.BorrowedBookResource;
import org.example.rest.ReaderResource;

import java.util.HashSet;
import java.util.Set;


public class JaxRsApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();
        classes.add(BookResource.class);
        classes.add(ReaderResource.class);
        classes.add(BorrowedBookResource.class);
        return classes;
    }
}