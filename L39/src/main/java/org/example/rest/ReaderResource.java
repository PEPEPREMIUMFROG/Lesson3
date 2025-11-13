package org.example.rest;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.example.dto.ReaderDTO;
import org.example.model.Reader;
import org.example.service.LibraryService;
import org.example.service.LibraryServiceException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("/readers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReaderResource {

    private LibraryService libraryService;

    public ReaderResource() {
        this.libraryService = new LibraryService();
    }

    @POST
    public Response addReader(Reader reader) {
        try {
            Reader savedReader = libraryService.addReader(reader);
            ReaderDTO readerDTO = new ReaderDTO(savedReader);
            return Response.status(Response.Status.CREATED).entity(readerDTO).build();
        } catch (LibraryServiceException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ErrorResponse(e.getMessage())).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Internal server error: " + e.getMessage())).build();
        }
    }

    @GET
    public Response getAllReaders() {
        try {
            List<Reader> readers = libraryService.getAllReaders();
            List<ReaderDTO> readerDTOs = readers.stream()
                    .map(ReaderDTO::new)
                    .collect(Collectors.toList());
            return Response.ok(readerDTOs).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Internal server error: " + e.getMessage())).build();
        }
    }

    @GET
    @Path("/{id}")
    public Response getReaderById(@PathParam("id") int id) {
        try {
            Optional<Reader> readerOpt = libraryService.getReaderById(id);
            if (readerOpt.isPresent()) {
                ReaderDTO readerDTO = new ReaderDTO(readerOpt.get());
                return Response.ok(readerDTO).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(new ErrorResponse("Reader not found with id: " + id)).build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Internal server error: " + e.getMessage())).build();
        }
    }

    @GET
    @Path("/search/email")
    public Response findReaderByEmail(@QueryParam("email") String email) {
        try {
            Optional<Reader> readerOpt = libraryService.findReaderByEmail(email);
            if (readerOpt.isPresent()) {
                ReaderDTO readerDTO = new ReaderDTO(readerOpt.get());
                return Response.ok(readerDTO).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(new ErrorResponse("Reader not found with email: " + email)).build();
            }
        } catch (LibraryServiceException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ErrorResponse(e.getMessage())).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Internal server error: " + e.getMessage())).build();
        }
    }
}