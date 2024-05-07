package org.biwaby.studytracker.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
@Component
public class ExceptionsHandler {

    @ExceptionHandler(TimerNoteAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleException(TimerNoteAlreadyExistsException e) {
        return ResponseEntity.status(400).body(
                new ErrorResponse(
                        400L,
                        "С данным предметом запись таймера уже существует"
                )
        );
    }
}
