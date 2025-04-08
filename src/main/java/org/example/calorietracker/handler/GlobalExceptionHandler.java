package org.example.calorietracker.handler;

import org.example.calorietracker.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Глобальный обработчик исключений для REST контроллеров.
 * Перехватывает и обрабатывает исключения, возникающие в процессе работы приложения,
 * возвращая соответствующие HTTP-ответы.
 *
 * <p>Обрабатывает следующие типы исключений:
 * <ul>
 *   <li>{@link ResourceNotFoundException} - 404 Not Found</li>
 *   <li>{@link MethodArgumentNotValidException} - 400 Bad Request</li>
 * </ul>
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Обрабатывает исключения, связанные с отсутствием запрашиваемых ресурсов.
     *
     * @param e перехваченное исключение {@link ResourceNotFoundException}
     * @return ответ с HTTP статусом 404 и сообщением об ошибке
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }

    /**
     * Обрабатывает исключения валидации входных параметров.
     *
     * @param e перехваченное исключение {@link MethodArgumentNotValidException}
     * @return ответ с HTTP статусом 400 и списком ошибок валидации
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(getErrorMessage(e.getBindingResult()));
    }

    /**
     * Форматирует сообщение об ошибках валидации.
     *
     * @param bindingResult результат валидации, содержащий информацию об ошибках
     * @return строка с перечнем ошибок в формате: "имя_поля" - сообщение_об_ошибке
     */
    private String getErrorMessage(BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();

        bindingResult.getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return errors.keySet().stream()
                .map(k -> "\"" + k + "\" - " + errors.get(k))
                .collect(Collectors.joining("\n"));
    }
}
