package org.example.model;

import java.time.LocalDate;

public class OccupiedBook {
    private Book book;
    private Reader reader;
    private LocalDate borrowDate;

    public OccupiedBook(Book book, Reader reader, LocalDate borrowDate) {
        this.book = book;
        this.reader = reader;
        this.borrowDate = borrowDate;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Reader getReader() {
        return reader;
    }

    public void setReader(Reader reader) {
        this.reader = reader;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
    }
}
