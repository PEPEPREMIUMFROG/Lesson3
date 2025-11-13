package org.example.rest;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.example.dto.BorrowedBookDTO;
import org.example.model.BorrowedBook;
import org.example.service.LibraryService;
import org.example.service.LibraryServiceException;

import java.util.List;
import java.util.stream.Collectors;

@Path("/borrowed")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BorrowedBookResource {

    private LibraryService libraryService;

    public BorrowedBookResource() {
        this.libraryService = new LibraryService();
    }

    @POST
    public Response borrowBook(BorrowBookRequest request) {
        try {
            BorrowedBook borrowedBook = libraryService.borrowBook(request.getBookId(), request.getReaderId());
            BorrowedBookDTO borrowedBookDTO = new BorrowedBookDTO(borrowedBook);
            return Response.status(Response.Status.CREATED).entity(borrowedBookDTO).build();
        } catch (LibraryServiceException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ErrorResponse(e.getMessage())).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Internal server error: " + e.getMessage())).build();
        }
    }

    @GET
    @Path("/{readerId}/borrowed")
    public Response getBooksBorrowedByReader(@PathParam("readerId") int readerId) {
        try {
            var borrowedBooks = libraryService.getBorrowedBooksByReaderId(readerId);
            List<BorrowedBookDTO> borrowedBookDTOs = borrowedBooks.stream()
                    .map(BorrowedBookDTO::new)
                    .collect(Collectors.toList());
            return Response.ok(borrowedBookDTOs).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Internal server error: " + e.getMessage())).build();
        }
    }


    @GET
    public Response getAllBorrowedBooks() {
        try {
            List<BorrowedBook> borrowedBooks = libraryService.getAllBorrowedBooks();
            List<BorrowedBookDTO> borrowedBookDTOs = borrowedBooks.stream()
                    .map(BorrowedBookDTO::new)
                    .collect(Collectors.toList());
            return Response.ok(borrowedBookDTOs).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Internal server error: " + e.getMessage())).build();
        }
    }

    @PUT
    @Path("/{id}/return")
    public Response returnBook(@PathParam("id") int borrowId) {
        try {
            libraryService.returnBook(borrowId);
            return Response.ok(new SuccessResponse("Book returned successfully")).build();
        } catch (LibraryServiceException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ErrorResponse(e.getMessage())).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Internal server error: " + e.getMessage())).build();
        }
    }

    @GET
    @Path("/overdue")
    public Response getOverdueBooks() {
        try {
            List<BorrowedBook> overdueBooks = libraryService.getOverdueBooks();
            List<BorrowedBookDTO> overdueBookDTOs = overdueBooks.stream()
                    .map(BorrowedBookDTO::new)
                    .collect(Collectors.toList());
            return Response.ok(overdueBookDTOs).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Internal server error: " + e.getMessage())).build();
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