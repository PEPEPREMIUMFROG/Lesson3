package org.example.rest;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.example.database.DatabaseConnectionManager;
import org.example.model.BorrowedBook;
import org.example.service.LibraryService;
import org.example.service.LibraryServiceException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@Path("/borrowed")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BorrowedBookResource {

    private LibraryService libraryService;

    public BorrowedBookResource() {
        try {
            Connection connection = DatabaseConnectionManager.getConnection();
            connection.setAutoCommit(false);
            this.libraryService = new LibraryService(connection);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize database connection for BorrowedBookResource", e);
        }
    }

    @POST
    public Response borrowBook(BorrowBookRequest request) {
        try {
            BorrowedBook borrowedBook = libraryService.borrowBook(request.getBookId(), request.getReaderId());
            return Response.status(Response.Status.CREATED).entity(borrowedBook).build();
        } catch (LibraryServiceException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ErrorResponse(e.getMessage())).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new ErrorResponse("Internal server error")).build();
        }
    }

    @GET
    public Response getAllBorrowedBooks() {
        try {
            List<BorrowedBook> borrowedBooks = libraryService.getAllBorrowedBooks();
            return Response.ok(borrowedBooks).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new ErrorResponse("Internal server error")).build();
        }
    }


    @PUT
    @Path("/{id}/return")
    public Response returnBook(@PathParam("id") int borrowId) {
        try {

            libraryService.returnBook(borrowId);
            return Response.status(Response.Status.OK).entity(new SuccessResponse("Book returned successfully")).build(); 
        } catch (LibraryServiceException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ErrorResponseBorrowed(e.getMessage())).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new ErrorResponse("Internal server error")).build();
        }
    }
}


class BorrowBookRequest {
    private int bookId;
    private int readerId;

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getReaderId() {
        return readerId;
    }

    public void setReaderId(int readerId) {
        this.readerId = readerId;
    }
}

class ErrorResponseBorrowed {
    private String message;

    public ErrorResponseBorrowed(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

class SuccessResponse {
    private String message;

    public SuccessResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}