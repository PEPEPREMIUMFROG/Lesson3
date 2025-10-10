package org.example.service;

import org.example.model.Book;
import org.example.model.OccupiedBook;
import org.example.model.Reader;

import java.time.LocalDate;
import java.util.List;

public interface LibraryService {
    Book addBook(Book book);
    Reader addReader(Reader reader);
    Book borrowBook(Long bookId, Long readerId);
    Book returnBook(Long bookId);
    List<OccupiedBook> getOccupiedBooks();
    Reader updateReader(Reader reader);
    List<Book> findBooksBorrowedAfterDateNotReturned(LocalDate date);
    List<Book> findBooksByStatus(String status);
}
