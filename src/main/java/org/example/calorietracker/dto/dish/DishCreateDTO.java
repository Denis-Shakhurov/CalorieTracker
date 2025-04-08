package org.example.calorietracker.dto.dish;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO (Data Transfer Object) для создания нового блюда.
 * Содержит необходимые поля и ограничения для валидации данных.
 *
 * @see org.example.calorietracker.model.Dish  Связанная сущность блюда
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DishCreateDTO {
    /**
     * Название блюда. Не может быть null или пустым.
     */
    @NotNull
    @NotBlank
    private String name;

    /**
     * Калорийность блюда (в ккал). Должна быть неотрицательной.
     */
    @Min(0)
    private double calories;

    /**
     * Содержание белков (в граммах). Должно быть неотрицательным.
     */
    @Min(0)
    private double proteins;

    /**
     * Содержание жиров (в граммах). Должно быть неотрицательным.
     */
    @Min(0)
    private double fats;

    /**
     * Содержание углеводов (в граммах). Должно быть неотрицательным.
     */
    @Min(0)
    private double carbohydrates;

    /**
     * ID приёма пищи, к которому относится блюдо. Может быть null, если блюдо пока не привязано к конкретному приёму пищи.
     */
    private Long mealId;
}
