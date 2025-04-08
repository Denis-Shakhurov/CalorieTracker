package org.example.calorietracker.dto.dish;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.calorietracker.model.Dish;

/**
 * Data Transfer Object (DTO) для передачи данных о блюде.
 * Используется для представления информации о блюде в API.
 * Содержит основные параметры блюда и его идентификатор.
 *
 * @see Dish Сущность блюда в модели данных
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DishDTO {
    /**
     * Уникальный идентификатор блюда в системе
     */
    private Long id;

    /**
     * Название блюда
     */
    private String name;

    /**
     * Калорийность блюда в килокалориях.
     * Значение должно быть неотрицательным.
     */
    @Min(0)
    private double calories;

    /**
     * Содержание белков в граммах.
     * Значение должно быть неотрицательным.
     */
    @Min(0)
    private double proteins;

    /**
     * Содержание жиров в граммах.
     * Значение должно быть неотрицательным.
     */
    @Min(0)
    private double fats;

    /**
     * Содержание углеводов в граммах.
     * Значение должно быть неотрицательным.
     */
    @Min(0)
    private double carbohydrates;

    /**
     * Идентификатор приема пищи, к которому относится блюдо.
     * Может быть null, если блюдо не привязано к конкретному приему пищи.
     */
    private Long mealId;
}
