package org.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "borrowed_books")
public class BorrowedBook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "borrow_id")
    @JsonProperty("borrow_id")
    private Integer borrowId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reader_id", nullable = false)
    private Reader reader;

    @Column(name = "borrow_date", nullable = false)
    @JsonProperty("borrow_date")
    private LocalDate borrowDate;

    @Column(name = "return_date")
    @JsonProperty("return_date")
    private LocalDate returnDate;

    @Column(name = "status", nullable = false, length = 20)
    @JsonProperty("status")
    private String status;

    public BorrowedBook() {
    }

    public BorrowedBook(Book book, Reader reader, LocalDate borrowDate,
                        LocalDate returnDate, String status) {
        this.book = book;
        this.reader = reader;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
        this.status = status;
    }

    public Integer getBorrowId() {
        return borrowId;
    }

    public void setBorrowId(Integer borrowId) {
        this.borrowId = borrowId;
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

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    @JsonProperty(value = "book_id", access = JsonProperty.Access.WRITE_ONLY)
    public Integer getBookId() {
        return book != null ? book.getBookId() : null;
    }

    @JsonProperty(value = "reader_id", access = JsonProperty.Access.WRITE_ONLY)
    public Integer getReaderId() {
        return reader != null ? reader.getReaderId() : null;
    }

    @JsonProperty("book_id")
    public void setBookId(Integer bookId) {
        if (this.book == null) {
            this.book = new Book();
        }
        this.book.setBookId(bookId);
    }

    @JsonProperty("reader_id")
    public void setReaderId(Integer readerId) {
        if (this.reader == null) {
            this.reader = new Reader();
        }
        this.reader.setReaderId(readerId);
    }

    @Override
    public String toString() {
        return "BorrowedBook{" +
                "borrowId=" + borrowId +
                ", bookId=" + (book != null ? book.getBookId() : null) +
                ", readerId=" + (reader != null ? reader.getReaderId() : null) +
                ", borrowDate=" + borrowDate +
                ", returnDate=" + returnDate +
                ", status='" + status + '\'' +
                '}';
    }
}