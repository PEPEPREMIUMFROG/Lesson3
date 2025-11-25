CREATE TABLE IF NOT EXISTS authors (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    country VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS books (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    published_year INT NOT NULL,
    author_id INT NOT NULL,
    FOREIGN KEY (author_id) REFERENCES authors(id) ON DELETE CASCADE
);

INSERT INTO authors (name, country) VALUES
('Lev Tolstoy', 'Russia'),
('Fyodor Dostoevsky', 'Russia'),
('George Orwell', 'UK');

INSERT INTO books (title, published_year, author_id) VALUES
('War and Peace', 1869, 1),
('Anna Karenina', 1877, 1),
('Crime and Punishment', 1866, 2),
('1984', 1949, 3),
('Animal Farm', 1945, 3);