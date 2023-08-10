package com.epam.esm.exceptionhandler.handler;

import com.epam.esm.exceptionhandler.exceptions.rest.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatchException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import static com.epam.esm.utils.Constants.*;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler({NoSuchItemException.class, PageException.class, ObjectNotFoundException.class})
    public ResponseEntity<Problem> handleNotFoundException(RuntimeException ex) {
        Problem problem = buildProblem(Status.NOT_FOUND, NOT_FOUND, ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problem);
    }

    @ExceptionHandler(ObjectAlreadyExistsException.class)
    public ResponseEntity<Problem> handleObjectAlreadyExists(RuntimeException ex) {
        Problem problem = buildProblem(Status.CONFLICT, ALREADY_EXIST, ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(problem);
    }

    @ExceptionHandler(ObjectIsInvalidException.class)
    public ResponseEntity<Problem> handleObjectIsInvalid(RuntimeException ex) {
        Problem problem = buildProblem(Status.BAD_REQUEST, INVALID_OBJECT, ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problem);
    }

    @ExceptionHandler({JsonPatchException.class, JsonProcessingException.class})
    public ResponseEntity<Problem> handleJsonException(RuntimeException ex) {
        Problem problem = buildProblem(Status.INTERNAL_SERVER_ERROR, JSON_EXCEPTION, ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problem);
    }

    @ExceptionHandler(UserCreationFailureException.class)
    public ResponseEntity<Problem> handleUserCreationException(RuntimeException ex) {
        Problem problem = buildProblem(Status.CONFLICT, FAILED_TO_CREATE_USER, ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(problem);
    }

    @ExceptionHandler(EmailNotFoundException.class)
    public ResponseEntity<Problem> handleEmailNotFoundException(RuntimeException ex) {
        Problem problem = buildProblem(Status.BAD_REQUEST, EMAIL_NOT_FOUND, ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problem);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<Problem> handleInvalidTokenException(RuntimeException ex) {
        Problem problem = buildProblem(Status.FORBIDDEN, INVALID_TOKEN, ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(problem);
    }

    @ExceptionHandler(WrongAuthenticationInstanceException.class)
    public ResponseEntity<Problem> handleWrongInstanceDetectedException(RuntimeException ex) {
        Problem problem = buildProblem(Status.FORBIDDEN, WRONG_AUTHENTICATION_INSTANCE, ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(problem);
    }

    private Problem buildProblem(Status status, String title, String detail) {
        return Problem.builder()
                .withStatus(status)
                .withTitle(title)
                .withDetail(detail)
                .build();
    }
}