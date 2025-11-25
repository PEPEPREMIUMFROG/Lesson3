package org.example.dao;

import org.example.mapper.AuthorRowMapper;
import org.example.model.Author;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JdbcAuthorDAO implements AuthorDAO {

    private final JdbcTemplate jdbcTemplate;
    private final AuthorRowMapper authorRowMapper;

    @Autowired
    public JdbcAuthorDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.authorRowMapper = new AuthorRowMapper();
    }

    @Override
    public Author save(Author author) {
        String sql = "INSERT INTO authors (name, country) VALUES (?, ?) RETURNING id";
        Long id = jdbcTemplate.queryForObject(sql, Long.class, author.getName(), author.getCountry());
        author.setId(id);
        return author;
    }

    @Override
    public void update(Author author) {
        String sql = "UPDATE authors SET name = ?, country = ? WHERE id = ?";
        jdbcTemplate.update(sql, author.getName(), author.getCountry(), author.getId());
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM authors WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public Optional<Author> findById(Long id) {
        String sql = "SELECT * FROM authors WHERE id = ?";
        try {
            Author author = jdbcTemplate.queryForObject(sql, authorRowMapper, id);
            return Optional.ofNullable(author);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Author> findAll() {
        String sql = "SELECT * FROM authors";
        return jdbcTemplate.query(sql, authorRowMapper);
    }

    @Override
    public List<Author> findByName(String name) {
        String sql = "SELECT * FROM authors WHERE name ILIKE ?";
        return jdbcTemplate.query(sql, authorRowMapper, "%" + name + "%");
    }
}