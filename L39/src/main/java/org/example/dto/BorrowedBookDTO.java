package org.example.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;

public class BorrowedBookDTO {
    @JsonProperty("borrow_id")
    private Integer borrowId;

    @JsonProperty("book")
    private BookDTO book;

    @JsonProperty("reader")
    private ReaderDTO reader;

    @JsonProperty("borrow_date")
    private LocalDate borrowDate;

    @JsonProperty("return_date")
    private LocalDate returnDate;

    @JsonProperty("status")
    private String status;

    public BorrowedBookDTO(org.example.model.BorrowedBook borrowedBook) {
        this.borrowId = borrowedBook.getBorrowId();
        this.book = new BookDTO(borrowedBook.getBook());
        this.reader = new ReaderDTO(borrowedBook.getReader());
        this.borrowDate = borrowedBook.getBorrowDate();
        this.returnDate = borrowedBook.getReturnDate();
        this.status = borrowedBook.getStatus();
    }


    public Integer getBorrowId() {
        return borrowId;
    }

    public void setBorrowId(Integer borrowId) {
        this.borrowId = borrowId;
    }

    public BookDTO getBook() {
        return book;
    }

    public void setBook(BookDTO book) {
        this.book = book;
    }

    public ReaderDTO getReader() {
        return reader;
    }

    public void setReader(ReaderDTO reader) {
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
}