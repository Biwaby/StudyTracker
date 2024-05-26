package org.biwaby.studytracker.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.biwaby.studytracker.exceptions.NotFoundExceptions.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
@Component
public class ExceptionsHandler {

    @ExceptionHandler(ProjectTaskNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleException(ProjectTaskNotFoundException e) {
        return ResponseEntity.status(400).body(
                new ErrorResponse(
                        400L,
                        "Task not found"
                )
        );
    }

    @ExceptionHandler(ProjectTaskAlreadyCompletedException.class)
    public ResponseEntity<ErrorResponse> handleException(ProjectTaskAlreadyCompletedException e) {
        return ResponseEntity.status(400).body(
                new ErrorResponse(
                        400L,
                        "This task has already been completed"
                )
        );
    }

    @ExceptionHandler(ProjectTaskHasNotBeenCompletedAnywayException.class)
    public ResponseEntity<ErrorResponse> handleException(ProjectTaskHasNotBeenCompletedAnywayException e) {
        return ResponseEntity.status(400).body(
                new ErrorResponse(
                        400L,
                        "This task hasn't been completed anyway"
                )
        );
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleException(UserAlreadyExistsException e) {
        return ResponseEntity.status(400).body(
                new ErrorResponse(
                        400L,
                        "A user with this username already exist"
                )
        );
    }

    @ExceptionHandler(IncorrectUserException.class)
    public ResponseEntity<ErrorResponse> handleException(IncorrectUserException e) {
        return ResponseEntity.status(400).body(
                new ErrorResponse(
                        400L,
                        "Incorrect user data has been entered"
                )
        );
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleException(UserNotFoundException e) {
        return ResponseEntity.status(400).body(
                new ErrorResponse(
                        400L,
                        "User not found"
                )
        );
    }

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResponse(RoleNotFoundException e) {
        return ResponseEntity.status(400).body(
                new ErrorResponse(
                        400L,
                        "Role not found"
                )
        );
    }

    @ExceptionHandler(UserDoesNotHaveRoleException.class)
    public ResponseEntity<ErrorResponse> handleResponse(UserDoesNotHaveRoleException e) {
        return ResponseEntity.status(400).body(
                new ErrorResponse(
                        400L,
                        "User doesn't have a role"
                )
        );
    }

    @ExceptionHandler(ProjectNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResponse(ProjectNotFoundException e) {
        return ResponseEntity.status(400).body(
                new ErrorResponse(
                        400L,
                        "Project not found"
                )
        );
    }

    @ExceptionHandler(TagNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResponse(TagNotFoundException e) {
        return ResponseEntity.status(400).body(
                new ErrorResponse(
                        400L,
                        "Tag not found"
                )
        );
    }

    @ExceptionHandler(TimerRecordNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResponse(TimerRecordNotFoundException e) {
        return ResponseEntity.status(400).body(
                new ErrorResponse(
                        400L,
                        "Timer record not found"
                )
        );
    }

    @ExceptionHandler(RecordAlreadyHasTagException.class)
    public ResponseEntity<ErrorResponse> handleResponse(RecordAlreadyHasTagException e) {
        return ResponseEntity.status(400).body(
                new ErrorResponse(
                        400L,
                        "Timer record already has tag"
                )
        );
    }

    @ExceptionHandler(RecordAlreadyHasProjectException.class)
    public ResponseEntity<ErrorResponse> handleResponse(RecordAlreadyHasProjectException e) {
        return ResponseEntity.status(400).body(
                new ErrorResponse(
                        400L,
                        "Timer record already has project"
                )
        );
    }

    @ExceptionHandler(RecordDoesNotHaveTagException.class)
    public ResponseEntity<ErrorResponse> handleResponse(RecordDoesNotHaveTagException e) {
        return ResponseEntity.status(400).body(
                new ErrorResponse(
                        400L,
                        "Record doesn't have this tag"
                )
        );
    }

    @ExceptionHandler(RecordDoesNotHaveProjectException.class)
    public ResponseEntity<ErrorResponse> handleResponse(RecordDoesNotHaveProjectException e) {
        return ResponseEntity.status(400).body(
                new ErrorResponse(
                        400L,
                        "Record doesn't have this project"
                )
        );
    }
}
