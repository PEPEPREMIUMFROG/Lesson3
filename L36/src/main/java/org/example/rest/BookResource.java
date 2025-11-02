package org.example.rest;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.example.database.DatabaseConnectionManager;
import org.example.model.Book;
import org.example.service.LibraryService;
import org.example.service.LibraryServiceException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Path("/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookResource {

    private LibraryService libraryService;

    public BookResource() {
        try {
            Connection connection = DatabaseConnectionManager.getConnection();
            connection.setAutoCommit(false);
            this.libraryService = new LibraryService(connection);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize database connection for BookResource", e);
        }
    }

    @POST
    public Response addBook(Book book) {
        try {
            Book savedBook = libraryService.addBook(book);
            return Response.status(Response.Status.CREATED).entity(savedBook).build();
        } catch (LibraryServiceException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ErrorResponse(e.getMessage())).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new ErrorResponse("Internal server error")).build();
        }
    }

    @GET
    @Path("/{readerId}/borrowed")
    public Response getBooksBorrowedByReader(@PathParam("readerId") int readerId) {
        try {
            var borrowedBooks = libraryService.getBorrowedBooksByReaderId(readerId);
            return Response.ok(borrowedBooks).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new ErrorResponse("Internal server error")).build();
        }
    }


    @GET
    public Response getAllBooks() {
        try {
            List<Book> books = libraryService.getAllBooks();
            return Response.ok(books).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new ErrorResponse("Internal server error")).build();
        }
    }


    @GET
    @Path("/{id}")
    public Response getBookById(@PathParam("id") int id) {
        try {
            Optional<Book> bookOpt = libraryService.getBookById(id);
            if (bookOpt.isPresent()) {
                return Response.ok(bookOpt.get()).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).entity(new ErrorResponse("Book not found with id: " + id)).build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new ErrorResponse("Internal server error")).build();
        }
    }
}

class ErrorResponse {
    private String message;

    public ErrorResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}