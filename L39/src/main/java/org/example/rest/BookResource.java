package org.example.rest;


import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.example.dto.BookDTO;
import org.example.model.Book;
import org.example.service.LibraryService;
import org.example.service.LibraryServiceException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookResource {

    private LibraryService libraryService;

    public BookResource() {
        this.libraryService = new LibraryService();
    }

    @POST
    public Response addBook(Book book) {
        try {
            Book savedBook = libraryService.addBook(book);
            BookDTO bookDTO = new BookDTO(savedBook);
            return Response.status(Response.Status.CREATED).entity(bookDTO).build();
        } catch (LibraryServiceException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ErrorResponse(e.getMessage())).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Internal server error: " + e.getMessage())).build();
        }
    }


    @GET
    public Response getAllBooks() {
        try {
            List<Book> books = libraryService.getAllBooks();
            List<BookDTO> bookDTOs = books.stream()
                    .map(BookDTO::new)
                    .collect(Collectors.toList());
            return Response.ok(bookDTOs).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Internal server error: " + e.getMessage())).build();
        }
    }

    @GET
    @Path("/{id}")
    public Response getBookById(@PathParam("id") int id) {
        try {
            Optional<Book> bookOpt = libraryService.getBookById(id);
            if (bookOpt.isPresent()) {
                BookDTO bookDTO = new BookDTO(bookOpt.get());
                return Response.ok(bookDTO).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(new ErrorResponse("Book not found with id: " + id)).build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Internal server error: " + e.getMessage())).build();
        }
    }

    @GET
    @Path("/search/title")
    public Response searchBooksByTitle(@QueryParam("q") String title) {
        try {
            List<Book> books = libraryService.findBooksByTitle(title);
            List<BookDTO> bookDTOs = books.stream()
                    .map(BookDTO::new)
                    .collect(Collectors.toList());
            return Response.ok(bookDTOs).build();
        } catch (LibraryServiceException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ErrorResponse(e.getMessage())).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Internal server error: " + e.getMessage())).build();
        }
    }

    @GET
    @Path("/search/author")
    public Response searchBooksByAuthor(@QueryParam("q") String author) {
        try {
            List<Book> books = libraryService.findBooksByAuthor(author);
            List<BookDTO> bookDTOs = books.stream()
                    .map(BookDTO::new)
                    .collect(Collectors.toList());
            return Response.ok(bookDTOs).build();
        } catch (LibraryServiceException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ErrorResponse(e.getMessage())).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Internal server error: " + e.getMessage())).build();
        }
    }
}

