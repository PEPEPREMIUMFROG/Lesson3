package org.example.dao;


import org.example.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookDAO {
    Book save(Book book);
    void update(Book book);
    void delete(Long id);
    Optional<Book> findById(Long id);
    List<Book> findAll();
    List<Book> findByTitle(String title);
    List<Book> findByAuthorId(Long authorId);
}