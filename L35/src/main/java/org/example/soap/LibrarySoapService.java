package org.example.soap;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebResult;
import jakarta.jws.WebService;
import org.example.model.Book;

@WebService(name = "LibrarySoapService", targetNamespace = "http://soap.example.org/")
public interface LibrarySoapService {

    @WebMethod
    @WebResult(name = "book")
    Book getBookById(@WebParam(name = "bookId") int bookId);

    @WebMethod
    @WebResult(name = "book")
    Book addBook(@WebParam(name = "title") String title,
                 @WebParam(name = "author") String author,
                 @WebParam(name = "publishedYear") int publishedYear,
                 @WebParam(name = "genre") String genre);
}