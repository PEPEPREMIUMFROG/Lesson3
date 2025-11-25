package org.example.dao;

import org.example.mapper.BookRowMapper;
import org.example.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JdbcBookDAO implements BookDAO {

    private final JdbcTemplate jdbcTemplate;
    private final BookRowMapper bookRowMapper;

    @Autowired
    public JdbcBookDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.bookRowMapper = new BookRowMapper();
    }

    @Override
    public Book save(Book book) {
        String sql = "INSERT INTO books (title, published_year, author_id) VALUES (?, ?, ?) RETURNING id";
        Long id = jdbcTemplate.queryForObject(sql, Long.class, book.getTitle(), book.getPublishedYear(), book.getAuthorId());
        book.setId(id);
        return book;
    }

    @Override
    public void update(Book book) {
        String sql = "UPDATE books SET title = ?, published_year = ?, author_id = ? WHERE id = ?";
        jdbcTemplate.update(sql, book.getTitle(), book.getPublishedYear(), book.getAuthorId(), book.getId());
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM books WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public Optional<Book> findById(Long id) {
        String sql = "SELECT * FROM books WHERE id = ?";
        try {
            Book book = jdbcTemplate.queryForObject(sql, bookRowMapper, id);
            return Optional.ofNullable(book);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Book> findAll() {
        String sql = "SELECT * FROM books";
        return jdbcTemplate.query(sql, bookRowMapper);
    }

    @Override
    public List<Book> findByTitle(String title) {
        String sql = "SELECT * FROM books WHERE title ILIKE ?";
        return jdbcTemplate.query(sql, bookRowMapper, "%" + title + "%");
    }

    @Override
    public List<Book> findByAuthorId(Long authorId) {
        String sql = "SELECT * FROM books WHERE author_id = ?";
        return jdbcTemplate.query(sql, bookRowMapper, authorId);
    }
}