package org.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public class BorrowedBook {
    @JsonProperty("borrow_id")
    private Integer borrowId;
    @JsonProperty("book_id")
    private Integer bookId;
    @JsonProperty("reader_id")
    private Integer readerId;
    @JsonProperty("borrow_date")
    private LocalDate borrowDate;
    @JsonProperty("return_date")
    private LocalDate returnDate;
    @JsonProperty("status")
    private String status;

    public BorrowedBook() {
    }

    public BorrowedBook(Integer bookId, Integer readerId, LocalDate borrowDate,
                        LocalDate returnDate, String status) {
        this.bookId = bookId;
        this.readerId = readerId;
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

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public Integer getReaderId() {
        return readerId;
    }

    public void setReaderId(Integer readerId) {
        this.readerId = readerId;
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
}