package org.example.dao;



import org.example.model.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorDAO {
    Author save(Author author);
    void update(Author author);
    void delete(Long id);
    Optional<Author> findById(Long id);
    List<Author> findAll();
    List<Author> findByName(String name);
}