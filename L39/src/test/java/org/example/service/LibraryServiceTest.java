package org.example.service;

import org.example.dao.BookDAO;
import org.example.dao.BorrowedBookDAO;
import org.example.dao.ReaderDAO;
import org.example.model.Book;
import org.example.model.BorrowedBook;
import org.example.model.Reader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LibraryServiceTest {

    @Mock
    private BookDAO bookDAO;

    @Mock
    private ReaderDAO readerDAO;

    @Mock
    private BorrowedBookDAO borrowedBookDAO;

    @InjectMocks
    private LibraryService libraryService;

    private Book testBook;
    private Reader testReader;
    private BorrowedBook testBorrowedBook;

    @BeforeEach
    void setUp() {
        testBook = new Book();
        testBook.setBookId(1);
        testBook.setTitle("Test Book");
        testBook.setAuthor("Test Author");
        testBook.setPublishedYear(2023);
        testBook.setGenre("Fiction");
        testReader = new Reader();
        testReader.setReaderId(1);
        testReader.setName("Test Reader");
        testReader.setEmail("test@example.com");
        testReader.setPhone("1234567890");
        testBorrowedBook = new BorrowedBook();
        testBorrowedBook.setBorrowId(1);
        testBorrowedBook.setBook(testBook);
        testBorrowedBook.setReader(testReader);
        testBorrowedBook.setBorrowDate(LocalDate.now().minusDays(15));
        testBorrowedBook.setStatus("borrowed");

        lenient().when(bookDAO.save(any(Book.class))).thenAnswer(invocation -> {
            Book book = invocation.getArgument(0);
            if (book.getBookId() == null) {
                book.setBookId(1);
            }
            return book;
        });

        lenient().when(readerDAO.save(any(Reader.class))).thenAnswer(invocation -> {
            Reader reader = invocation.getArgument(0);
            if (reader.getReaderId() == null) {
                reader.setReaderId(1);
            }
            return reader;
        });

        lenient().when(borrowedBookDAO.borrowBook(any(BorrowedBook.class))).thenAnswer(invocation -> {
            BorrowedBook bb = invocation.getArgument(0);
            if (bb.getBorrowId() == null) {
                bb.setBorrowId(1);
            }
            return bb;
        });
    }

    @Test
    void getAllBooks_Success() {
        List<Book> expectedBooks = Arrays.asList(testBook);
        when(bookDAO.findAll()).thenReturn(expectedBooks);
        List<Book> result = libraryService.getAllBooks();
        assertEquals(1, result.size());
        assertEquals("Test Book", result.get(0).getTitle());
        verify(bookDAO).findAll();
    }

    @Test
    void getBookById_Found() {
        when(bookDAO.findById(1)).thenReturn(Optional.of(testBook));
        Optional<Book> result = libraryService.getBookById(1);
        assertTrue(result.isPresent());
        assertEquals("Test Book", result.get().getTitle());
        verify(bookDAO).findById(1);
    }

    @Test
    void getBookById_NotFound() {
        when(bookDAO.findById(1)).thenReturn(Optional.empty());
        Optional<Book> result = libraryService.getBookById(1);
        assertFalse(result.isPresent());
        verify(bookDAO).findById(1);
    }

    @Test
    void addBook_Success() {
        Book newBook = new Book("New Book", "New Author", 2024, "Science");
        Book result = libraryService.addBook(newBook);
        assertNotNull(result);
        assertEquals("New Book", result.getTitle());
        assertEquals("New Author", result.getAuthor());
        assertEquals(2024, result.getPublishedYear());
        assertEquals("Science", result.getGenre());
        assertEquals(1, result.getBookId());
        verify(bookDAO).save(newBook);
    }

    @Test
    void addBook_InvalidTitle() {
        Book invalidBook = new Book("", "Author", 2023, "Genre");
        LibraryServiceException exception = assertThrows(LibraryServiceException.class, () -> {
            libraryService.addBook(invalidBook);
        });
        assertEquals("Book title cannot be empty", exception.getMessage());
        verify(bookDAO, never()).save(any());
    }

    @Test
    void addBook_InvalidAuthor() {
        Book invalidBook = new Book("Valid Title", "", 2023, "Genre");
        LibraryServiceException exception = assertThrows(LibraryServiceException.class, () -> {
            libraryService.addBook(invalidBook);
        });
        assertEquals("Book author cannot be empty", exception.getMessage());
        verify(bookDAO, never()).save(any());
    }

    @Test
    void addBook_InvalidYear_Future() {
        int currentYear = LocalDate.now().getYear();
        Book invalidBook = new Book("Valid Title", "Valid Author", currentYear + 1, "Genre");
        LibraryServiceException exception = assertThrows(LibraryServiceException.class, () -> {
            libraryService.addBook(invalidBook);
        });
        assertEquals("Publication year cannot be in the future", exception.getMessage());
        verify(bookDAO, never()).save(any());
    }

    @Test
    void findBooksByTitle_Success() {
        List<Book> expectedBooks = Arrays.asList(testBook);
        when(bookDAO.findByTitle("Test")).thenReturn(expectedBooks);
        List<Book> result = libraryService.findBooksByTitle("Test");
        assertEquals(1, result.size());
        assertEquals("Test Book", result.get(0).getTitle());
        verify(bookDAO).findByTitle("Test");
    }

    @Test
    void findBooksByTitle_EmptyTitle() {
        LibraryServiceException exception = assertThrows(LibraryServiceException.class, () -> {
            libraryService.findBooksByTitle("");
        });
        assertEquals("Title cannot be empty", exception.getMessage());
        verify(bookDAO, never()).findByTitle(any());
    }

    @Test
    void findBooksByAuthor_Success() {
        List<Book> expectedBooks = Arrays.asList(testBook);
        when(bookDAO.findByAuthor("Author")).thenReturn(expectedBooks);
        List<Book> result = libraryService.findBooksByAuthor("Author");
        assertEquals(1, result.size());
        assertEquals("Test Author", result.get(0).getAuthor());
        verify(bookDAO).findByAuthor("Author");
    }

    @Test
    void findBooksByAuthor_EmptyAuthor() {
        LibraryServiceException exception = assertThrows(LibraryServiceException.class, () -> {
            libraryService.findBooksByAuthor("");
        });
        assertEquals("Author cannot be empty", exception.getMessage());
        verify(bookDAO, never()).findByAuthor(any());
    }


    @Test
    void getAllReaders_Success() {
        List<Reader> expectedReaders = Arrays.asList(testReader);
        when(readerDAO.findAll()).thenReturn(expectedReaders);
        List<Reader> result = libraryService.getAllReaders();
        assertEquals(1, result.size());
        assertEquals("Test Reader", result.get(0).getName());
        verify(readerDAO).findAll();
    }

    @Test
    void getReaderById_Found() {
        when(readerDAO.findById(1)).thenReturn(Optional.of(testReader));
        Optional<Reader> result = libraryService.getReaderById(1);
        assertTrue(result.isPresent());
        assertEquals("test@example.com", result.get().getEmail());
        verify(readerDAO).findById(1);
    }

    @Test
    void getReaderById_NotFound() {
        when(readerDAO.findById(1)).thenReturn(Optional.empty());
        Optional<Reader> result = libraryService.getReaderById(1);
        assertFalse(result.isPresent());
        verify(readerDAO).findById(1);
    }

    @Test
    void addReader_Success() {
        Reader newReader = new Reader("New Reader", "new@example.com", "9876543210");
        when(readerDAO.findByEmail("new@example.com")).thenReturn(Optional.empty());
        Reader result = libraryService.addReader(newReader);
        assertNotNull(result);
        assertEquals("New Reader", result.getName());
        assertEquals("new@example.com", result.getEmail());
        assertEquals(1, result.getReaderId());
        verify(readerDAO).findByEmail("new@example.com");
        verify(readerDAO).save(newReader);
    }

    @Test
    void addReader_EmailExists() {
        Reader existingReader = new Reader("Existing Reader", "existing@example.com", "1234567890");
        Reader newReader = new Reader("New Reader", "existing@example.com", "9876543210");
        when(readerDAO.findByEmail("existing@example.com")).thenReturn(Optional.of(existingReader));
        LibraryServiceException exception = assertThrows(LibraryServiceException.class, () -> {
            libraryService.addReader(newReader);
        });
        assertEquals("Reader with email 'existing@example.com' already exists", exception.getMessage());
        verify(readerDAO).findByEmail("existing@example.com");
        verify(readerDAO, never()).save(any());
    }

    @Test
    void addReader_InvalidName() {
        Reader invalidReader = new Reader("", "invalid@example.com", "1234567890");
        LibraryServiceException exception = assertThrows(LibraryServiceException.class, () -> {
            libraryService.addReader(invalidReader);
        });
        assertEquals("Reader name cannot be empty", exception.getMessage());
        verify(readerDAO, never()).save(any());
    }

    @Test
    void addReader_InvalidEmail() {
        Reader invalidReader = new Reader("Valid Name", "invalid-email", "1234567890");
        LibraryServiceException exception = assertThrows(LibraryServiceException.class, () -> {
            libraryService.addReader(invalidReader);
        });
        assertEquals("Invalid email format", exception.getMessage());
        verify(readerDAO, never()).save(any());
    }

    @Test
    void findReaderByEmail_Success() {
        when(readerDAO.findByEmail("test@example.com")).thenReturn(Optional.of(testReader));
        Optional<Reader> result = libraryService.findReaderByEmail("test@example.com");
        assertTrue(result.isPresent());
        assertEquals("Test Reader", result.get().getName());
        verify(readerDAO).findByEmail("test@example.com");
    }

    @Test
    void findReaderByEmail_NotFound() {
        when(readerDAO.findByEmail("notfound@example.com")).thenReturn(Optional.empty());
        Optional<Reader> result = libraryService.findReaderByEmail("notfound@example.com");
        assertFalse(result.isPresent());
        verify(readerDAO).findByEmail("notfound@example.com");
    }

    @Test
    void findReaderByEmail_EmptyEmail() {
        LibraryServiceException exception = assertThrows(LibraryServiceException.class, () -> {
            libraryService.findReaderByEmail("");
        });
        assertEquals("Email cannot be empty", exception.getMessage());
        verify(readerDAO, never()).findByEmail(any());
    }


    @Test
    void getBorrowedBooksByReaderId_Success() {
        List<BorrowedBook> expectedBorrows = Arrays.asList(testBorrowedBook);
        when(borrowedBookDAO.findBorrowedBooksByReaderId(1)).thenReturn(expectedBorrows);
        List<BorrowedBook> result = libraryService.getBorrowedBooksByReaderId(1);
        assertEquals(1, result.size());
        assertEquals("borrowed", result.get(0).getStatus());
        assertEquals("Test Book", result.get(0).getBook().getTitle());
        verify(borrowedBookDAO).findBorrowedBooksByReaderId(1);
    }

    @Test
    void getAllBorrowedBooks_Success() {
        List<BorrowedBook> expectedBorrows = Arrays.asList(testBorrowedBook);
        when(borrowedBookDAO.findAllBorrowedBooks()).thenReturn(expectedBorrows);
        List<BorrowedBook> result = libraryService.getAllBorrowedBooks();
        assertEquals(1, result.size());
        assertEquals("Test Reader", result.get(0).getReader().getName());
        verify(borrowedBookDAO).findAllBorrowedBooks();
    }

    @Test
    void borrowBook_Success() {
        when(bookDAO.findById(1)).thenReturn(Optional.of(testBook));
        when(readerDAO.findById(1)).thenReturn(Optional.of(testReader));
        when(borrowedBookDAO.findBorrowedBooksByReaderId(1)).thenReturn(new ArrayList<>());
        BorrowedBook result = libraryService.borrowBook(1, 1);
        assertNotNull(result);
        assertEquals("borrowed", result.getStatus());
        assertEquals(1, result.getBook().getBookId());
        assertEquals(1, result.getReader().getReaderId());
        assertEquals(1, result.getBorrowId());
        verify(bookDAO).findById(1);
        verify(readerDAO).findById(1);
        verify(borrowedBookDAO).findBorrowedBooksByReaderId(1);
        verify(borrowedBookDAO).borrowBook(any(BorrowedBook.class));
    }

    @Test
    void borrowBook_BookNotFound() {
        when(bookDAO.findById(999)).thenReturn(Optional.empty());
        LibraryServiceException exception = assertThrows(LibraryServiceException.class, () -> {
            libraryService.borrowBook(999, 1);
        });
        assertEquals("Book not found with id: 999", exception.getMessage());
        verify(bookDAO).findById(999);
        verify(readerDAO, never()).findById(anyInt());
    }

    @Test
    void borrowBook_ReaderNotFound() {
        when(bookDAO.findById(1)).thenReturn(Optional.of(testBook));
        when(readerDAO.findById(999)).thenReturn(Optional.empty());
        LibraryServiceException exception = assertThrows(LibraryServiceException.class, () -> {
            libraryService.borrowBook(1, 999);
        });
        assertEquals("Reader not found with id: 999", exception.getMessage());
        verify(bookDAO).findById(1);
        verify(readerDAO).findById(999);
    }

    @Test
    void borrowBook_AlreadyBorrowed() {
        List<BorrowedBook> currentBorrows = Arrays.asList(testBorrowedBook);
        when(bookDAO.findById(1)).thenReturn(Optional.of(testBook));
        when(readerDAO.findById(1)).thenReturn(Optional.of(testReader));
        when(borrowedBookDAO.findBorrowedBooksByReaderId(1)).thenReturn(currentBorrows);
        LibraryServiceException exception = assertThrows(LibraryServiceException.class, () -> {
            libraryService.borrowBook(1, 1);
        });
        assertEquals("Book is already borrowed by this reader", exception.getMessage());
        verify(bookDAO).findById(1);
        verify(readerDAO).findById(1);
        verify(borrowedBookDAO).findBorrowedBooksByReaderId(1);
        verify(borrowedBookDAO, never()).borrowBook(any());
    }

    @Test
    void returnBook_Success() {
        libraryService.returnBook(1);
        verify(borrowedBookDAO).returnBook(1);
    }

    @Test
    void getOverdueBooks_Success() {
        List<BorrowedBook> expectedOverdue = Arrays.asList(testBorrowedBook);
        when(borrowedBookDAO.findOverdueBooks()).thenReturn(expectedOverdue);
        List<BorrowedBook> result = libraryService.getOverdueBooks();
        assertEquals(1, result.size());
        assertEquals("borrowed", result.get(0).getStatus());
        verify(borrowedBookDAO).findOverdueBooks();
    }

    @Test
    void addBook_InvalidYear_Negative() {
        Book invalidBook = new Book("Valid Title", "Valid Author", -1, "Genre");
        LibraryServiceException exception = assertThrows(LibraryServiceException.class, () -> {
            libraryService.addBook(invalidBook);
        });
        assertEquals("Invalid publication year", exception.getMessage());
        verify(bookDAO, never()).save(any());
    }
}