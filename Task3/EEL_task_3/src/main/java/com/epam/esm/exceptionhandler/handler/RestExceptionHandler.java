package com.epam.esm.exceptionhandler.handler;

import com.epam.esm.exceptionhandler.exceptions.NoSuchItemException;
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

import javax.servlet.http.HttpServletRequest;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice()
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchItemException.class)
    public ResponseEntity<Object> handleNoSuchItemException(RuntimeException ex, WebRequest request){
        return handleExceptionInternal(ex,ex.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND,request);
      }

}
