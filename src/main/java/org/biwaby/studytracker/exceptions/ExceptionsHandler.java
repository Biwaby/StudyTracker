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

    @ExceptionHandler(TimetableItemNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleException(TimetableItemNotFoundException e) {
        return ResponseEntity.status(400).body(
                new ErrorResponse(
                        400L,
                        "Элемент расписания не найден"
                )
        );
    }

    @ExceptionHandler(TimerNoteNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleException(TimerNoteNotFoundException e) {
        return ResponseEntity.status(400).body(
                new ErrorResponse(
                        400L,
                        "Запись таймера не найдена"
                )
        );
    }

    @ExceptionHandler(BuildingNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleException(BuildingNotFoundException e) {
        return ResponseEntity.status(400).body(
                new ErrorResponse(
                        400L,
                        "Учебный корпус не найден"
                )
        );
    }

    @ExceptionHandler(ClassroomNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleException(ClassroomNotFoundException e) {
        return ResponseEntity.status(400).body(
                new ErrorResponse(
                        400L,
                        "Учебная аудитория не найдена"
                )
        );
    }

    @ExceptionHandler(ClassTypeNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleException(ClassTypeNotFoundException e) {
        return ResponseEntity.status(400).body(
                new ErrorResponse(
                        400L,
                        "Тип занятия не найден"
                )
        );
    }

    @ExceptionHandler(SubjectNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleException(SubjectNotFoundException e) {
        return ResponseEntity.status(400).body(
                new ErrorResponse(
                        400L,
                        "Учебная дисциплина не найдена"
                )
        );
    }

    @ExceptionHandler(TeacherNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleException(TeacherNotFoundException e) {
        return ResponseEntity.status(400).body(
                new ErrorResponse(
                        400L,
                        "Преподаватель не найден"
                )
        );
    }
}
