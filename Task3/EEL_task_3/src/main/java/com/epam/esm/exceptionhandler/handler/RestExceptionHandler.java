package com.epam.esm.exceptionhandler.handler;

import com.epam.esm.exceptionhandler.exceptions.NoSuchItemException;
import com.epam.esm.exceptionhandler.exceptions.ObjectAlreadyExistsException;
import com.epam.esm.exceptionhandler.exceptions.ObjectIsInvalidException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice()
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @ExceptionHandler(NoSuchItemException.class)
    public ResponseEntity<Object> handleNoSuchItemException(RuntimeException ex, WebRequest request){
        return handleExceptionInternal(ex,ex.getMessage(), new HttpHeaders(), HttpStatus.NO_CONTENT,request);
      }

    @ResponseStatus(value = HttpStatus.CONFLICT)
    @ExceptionHandler(ObjectAlreadyExistsException.class)
    public ResponseEntity<Object> handleObjectAlreadyExists(RuntimeException ex, WebRequest request){
        return handleExceptionInternal(ex,ex.getMessage(), new HttpHeaders(), HttpStatus.CONFLICT,request);
    }
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ObjectIsInvalidException.class)
    public ResponseEntity<Object> handleObjectIsInvalid(RuntimeException ex, WebRequest request){
        return handleExceptionInternal(ex,ex.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST,request);
    }


}