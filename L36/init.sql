CREATE TABLE books (
    book_id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    published_year INT CHECK (published_year > 0),
    genre VARCHAR(100)
);

CREATE TABLE readers (
    reader_id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    phone VARCHAR(15) UNIQUE
);

CREATE TABLE borrowed_books (
    borrow_id SERIAL PRIMARY KEY,
    book_id INT NOT NULL REFERENCES books(book_id) ON DELETE CASCADE,
    reader_id INT NOT NULL REFERENCES readers(reader_id) ON DELETE CASCADE,
    borrow_date DATE NOT NULL,
    return_date DATE,
    status VARCHAR(20) CHECK (status IN ('borrowed', 'returned'))
);

CREATE INDEX idx_books_title ON books(title);
CREATE INDEX idx_borrowed_books_status ON borrowed_books(status);

INSERT INTO books (title, author, published_year, genre) VALUES
('Война и мир', 'Лев Толстой', 1869, 'Роман'),
('Преступление и наказание', 'Федор Достоевский', 1866, 'Роман'),
('Мастер и Маргарита', 'Михаил Булгаков', 1967, 'Роман'),
('1984', 'Джордж Оруэлл', 1949, 'Антиутопия'),
('Гарри Поттер и философский камень', 'Дж. К. Роулинг', 1997, 'Фэнтези');

INSERT INTO readers (name, email, phone) VALUES
('Иван Иванов', 'ivan@mail.com', '+79161234567'),
('Петр Петров', 'petr@mail.com', '+79167654321'),
('Мария Сидорова', 'maria@mail.com', '+79169998877'),
('Анна Козлова', 'anna@mail.com', '+79165554433');

INSERT INTO borrowed_books (book_id, reader_id, borrow_date, return_date, status) VALUES
(1, 1, CURRENT_DATE - INTERVAL '10 days', NULL, 'borrowed'),
(2, 2, CURRENT_DATE - INTERVAL '5 days', CURRENT_DATE - INTERVAL '1 day', 'returned'),
(3, 1, CURRENT_DATE - INTERVAL '3 days', NULL, 'borrowed'),
(4, 3, CURRENT_DATE - INTERVAL '7 days', NULL, 'borrowed');