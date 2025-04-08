package org.example.calorietracker.dto.meal;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Data Transfer Object (DTO) для создания нового приема пищи (Meal).
 * Содержит необходимую информацию для создания записи о приеме пищи,
 * включая пользователя и список блюд.
 *
 * @see org.example.calorietracker.model.Meal Сущность приема пищи в модели данных
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MealCreateDTO {
    /**
     * Идентификатор пользователя, к которому относится прием пищи.
     * Не может быть null.
     */
    @NotNull
    private Long userId;

    /**
     * Список идентификаторов блюд, входящих в данный прием пищи.
     * Может быть пустым, если прием пищи создается без блюд.
     * Null значение эквивалентно пустому списку.
     */
    private List<Long> dishIds;
}
