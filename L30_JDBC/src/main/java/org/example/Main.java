package org.example;

import org.example.dao.*;
import org.example.model.Book;
import org.example.model.OccupiedBook;
import org.example.model.Reader;
import org.example.service.LibraryService;
import org.example.service.LibraryServiceImpl;

import java.time.LocalDate;
import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        BookDAO bookDAO = new BookDAOImpl();
        ReaderDAO readerDAO = new ReaderDAOImpl();
        BorrowedBookDAO borrowedBookDAO = new BorrowedBookDAOImpl();
        LibraryService library = new LibraryServiceImpl(bookDAO, readerDAO, borrowedBookDAO);

        addBook(library);
        addReader(library);
        borrowBook(library);
        listOccupiedBooks(library);
        filterBooksByStatus(library);
        findRecentBorrowedBooks(library);
        updateReader(library);
        returnBook(library);
    }

    private static void addBook(LibraryService library) {
        System.out.println("addBook");
        Book book = new Book();
        book.setTitle("Cloud in pants");
        book.setAuthor("Vladimir Mayakovsky");
        book.setPublishedYear(1930);
        book.setGenre("poem");
        Book saved = library.addBook(book);
        System.out.println("Added: " + saved.getTitle() + "(ID: " + saved.getId() + ")\n");
    }

    private static void addReader(LibraryService library) {
        String testString = String.valueOf(System.currentTimeMillis() + 1);
        System.out.println("addReader");
        Reader reader = new Reader();
        reader.setName("Vladd Mayakov");
        reader.setEmail(testString);
        reader.setPhone(testString);
        Reader saved = library.addReader(reader);
        System.out.println("Registered: " + saved.getName() + " (ID: " + saved.getId() + ")\n");
    }

    private static void borrowBook(LibraryService library) {
        Book newBook = new Book();
        newBook.setTitle("New Book " + System.currentTimeMillis());
        newBook.setAuthor("Author");
        newBook.setPublishedYear(2025);
        newBook.setGenre("poem");
        Book saved = library.addBook(newBook);
        System.out.println("Added book ID: " + saved.getId());
        try {
            Book borrowed = library.borrowBook(saved.getId(), 4L);
            System.out.println("Successfully borrowed: " + borrowed.getTitle());
        } catch (Exception e) {
            System.out.println("Failed to borrow: " + e.getMessage());
        }
    }

    private static void listOccupiedBooks(LibraryService library) {
        System.out.println("listOccupiedBooks");
        List<OccupiedBook> occupied = library.getOccupiedBooks();
        if (occupied.isEmpty()) {
            System.out.println("No books are currently borrowed.\n");
        } else {
            for (OccupiedBook ob : occupied) {
                System.out.printf("%s by %s → held by %s (phone is %s) since %s%n", ob.getBook().getTitle(), ob.getBook().getAuthor(), ob.getReader().getName(), ob.getReader().getPhone(), ob.getBorrowDate());
            }
            System.out.println();
        }
    }

    private static void filterBooksByStatus(LibraryService library) {
        System.out.println("filterBooksByStatus");
        System.out.println("Books with status 'returned' (available for borrowing):");
        try {
            List<Book> returned = library.findBooksByStatus("returned");
            if (returned.isEmpty()) {
                System.out.println("  No books with status 'returned'.");
            } else {
                returned.forEach(b -> System.out.println("  " + b.getTitle() + " - " + b.getGenre()));
            }
        } catch (Exception e) {
            System.out.println("  Error: " + e.getMessage());
        }
        System.out.println();
        System.out.println("Books with status 'borrowed' (currently borrowed):");
        try {
            List<Book> borrowed = library.findBooksByStatus("borrowed");
            if (borrowed.isEmpty()) {
                System.out.println("  No books with status 'borrowed'.");
            } else {
                borrowed.forEach(b -> System.out.println("  " + b.getTitle() + " by " + b.getAuthor()));
            }
        } catch (Exception e) {
            System.out.println("  Error: " + e.getMessage());
        }
        System.out.println();
    }

    private static void findRecentBorrowedBooks(LibraryService library) {
        System.out.println("findRecentBorrowedBooks");
        List<Book> recent = library.findBooksBorrowedAfterDateNotReturned(LocalDate.of(2025, 1, 1));
        if (recent.isEmpty()) {
            System.out.println(" No recent unreturned books found.\n");
        } else {
            recent.forEach(b -> System.out.println(b.getTitle()));
            System.out.println();
        }
    }

    private static void updateReader(LibraryService library) {
        System.out.println("updateReader");
        try {
            Reader reader = new Reader();
            reader.setId(2L);
            reader.setName("Dmitriy Ivanov");
            reader.setEmail("DI@spring.io");
            reader.setPhone("9999999999");
            Reader updated = library.updateReader(reader);
            System.out.println("Updated reader: " + updated.getName() + " with email: " + updated.getEmail() + "\n");
        } catch (Exception e) {
            System.out.println("Update failed: " + e.getMessage() + "\n");
        }
    }

    private static void returnBook(LibraryService library) {
        try {
            Book returned = library.returnBook(20L);
            System.out.println("Returned: " + returned.getTitle());
        } catch (Exception e) {
            System.out.println("Return failed: " + e.getMessage());
        }
    }
}