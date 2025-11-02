package org.example.rest;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.example.database.DatabaseConnectionManager;
import org.example.model.Reader;
import org.example.service.LibraryService;
import org.example.service.LibraryServiceException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Path("/readers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReaderResource {

    private LibraryService libraryService;

    public ReaderResource() {
        try {
            Connection connection = DatabaseConnectionManager.getConnection();
            connection.setAutoCommit(false);
            this.libraryService = new LibraryService(connection);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize database connection for ReaderResource", e);
        }
    }

    @POST
    public Response addReader(Reader reader) {
        try {
            Reader savedReader = libraryService.addReader(reader);
            return Response.status(Response.Status.CREATED).entity(savedReader).build();
        } catch (LibraryServiceException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ErrorResponse(e.getMessage())).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new ErrorResponse("Internal server error")).build();
        }
    }

    @GET
    public Response getAllReaders() {
        try {
            List<Reader> readers = libraryService.getAllReaders();
            return Response.ok(readers).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new ErrorResponse("Internal server error")).build();
        }
    }

    @GET
    @Path("/{id}")
    public Response getReaderById(@PathParam("id") int id) {
        try {
            Optional<Reader> readerOpt = libraryService.getReaderById(id);
            if (readerOpt.isPresent()) {
                return Response.ok(readerOpt.get()).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).entity(new ErrorResponse("Reader not found with id: " + id)).build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new ErrorResponse("Internal server error")).build();
        }
    }
}