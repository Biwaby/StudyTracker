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

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleException(TaskNotFoundException e) {
        return ResponseEntity.status(400).body(
                new ErrorResponse(
                        400L,
                        "Задание не найдено"
                )
        );
    }

    @ExceptionHandler(TaskAlreadyCompletedException.class)
    public ResponseEntity<ErrorResponse> handleException(TaskAlreadyCompletedException e) {
        return ResponseEntity.status(400).body(
                new ErrorResponse(
                        400L,
                        "Задание уже выполнено"
                )
        );
    }

    @ExceptionHandler(TaskHasNotBeenCompletedAnywayException.class)
    public ResponseEntity<ErrorResponse> handleException(TaskHasNotBeenCompletedAnywayException e) {
        return ResponseEntity.status(400).body(
                new ErrorResponse(
                        400L,
                        "Задание и так не выполнено"
                )
        );
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleException(UserAlreadyExistsException e) {
        return ResponseEntity.status(400).body(
                new ErrorResponse(
                        400L,
                        "Пользователь с данным именем уже существует"
                )
        );
    }

    @ExceptionHandler(IncorrectUserException.class)
    public ResponseEntity<ErrorResponse> handleException(IncorrectUserException e) {
        return ResponseEntity.status(400).body(
                new ErrorResponse(
                        400L,
                        "Введены некорректные данные пользователя"
                )
        );
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleException(UserNotFoundException e) {
        return ResponseEntity.status(400).body(
                new ErrorResponse(
                        400L,
                        "Пользователь не найден"
                )
        );
    }

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResponse(RoleNotFoundException e) {
        return ResponseEntity.status(400).body(
                new ErrorResponse(
                        400L,
                        "Роль не найдена"
                )
        );
    }

    @ExceptionHandler(UserDoesNotHaveRoleException.class)
    public ResponseEntity<ErrorResponse> handleResponse(UserDoesNotHaveRoleException e) {
        return ResponseEntity.status(400).body(
                new ErrorResponse(
                        400L,
                        "Пользователь не владеет данной ролью"
                )
        );
    }
}
