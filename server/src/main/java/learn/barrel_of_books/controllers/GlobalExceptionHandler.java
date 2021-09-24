package learn.barrel_of_books.controllers;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<GlobalErrorResponse> handleException(DataAccessException ex){
        return new ResponseEntity<>(
                new GlobalErrorResponse("We can't show you the details, but something went wrong in our database. Sorry :("),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<GlobalErrorResponse> handleException(IllegalArgumentException ex){
        return new ResponseEntity<>(
                new GlobalErrorResponse("Argument is inappropriate."),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<GlobalErrorResponse> handleException(HttpMessageNotReadableException ex){
        return new ResponseEntity<>(
                new GlobalErrorResponse("Required request body is missing."),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<GlobalErrorResponse> handleException(HttpMediaTypeNotSupportedException ex){
        return new ResponseEntity<>(
                new GlobalErrorResponse("Content of a type is not supported by request handler."),
                HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<GlobalErrorResponse> handleException(HttpRequestMethodNotSupportedException ex){
        return new ResponseEntity<>(
                new GlobalErrorResponse("Request method is not supported."),
                HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<GlobalErrorResponse> handleException(MissingRequestHeaderException ex){
        return new ResponseEntity<>(
                new GlobalErrorResponse("Required headers are missing."),
                HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<GlobalErrorResponse> handleException(Exception ex){
        return new ResponseEntity<>(
                new GlobalErrorResponse("Something went wrong on our end. Your request failed. :("),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
