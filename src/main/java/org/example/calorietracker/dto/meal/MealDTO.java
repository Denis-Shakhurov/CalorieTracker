package org.example.calorietracker.dto.meal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.calorietracker.model.Meal;

import java.time.LocalDate;
import java.util.List;

/**
 * Data Transfer Object (DTO) для представления информации о приеме пищи (Meal).
 * Содержит полную информацию о приеме пищи, включая идентификатор, дату создания,
 * связанного пользователя и список блюд.
 *
 * @see org.example.calorietracker.model.Meal Сущность приема пищи в модели данных
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MealDTO {
    /**
     * Уникальный идентификатор приема пищи в системе
     */
    private Long id;

    /**
     * Дата и время создания записи о приеме пищи.
     * Формат: YYYY-MM-DD (ISO-8601)
     */
    private LocalDate createdAt;

    /**
     * Идентификатор пользователя, которому принадлежит прием пищи.
     * Всегда должен иметь значение (не может быть null).
     */
    private Long userId;

    /**
     * Список идентификаторов блюд, входящих в данный прием пищи.
     * Может быть пустым, если в прием пищи не включены блюда.
     * Null значение эквивалентно пустому списку.
     */
    private List<Long> dishIds;
}
