package org.example.calorietracker.dto.dish;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) для обновления данных о блюде.
 * Содержит поля, которые могут быть изменены при обновлении информации о блюде.
 * Все числовые параметры должны быть неотрицательными.
 *
 * @see org.example.calorietracker.model.Dish Сущность блюда в модели данных
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class DishUpdateDTO {
    /**
     * Обновленное название блюда.
     * Не может быть пустым или состоять только из пробелов.
     */
    @NotBlank
    private String name;

    /**
     * Обновленная калорийность блюда (в ккал).
     * Должна быть положительной или равной нулю.
     */
    @Min(0)
    private Double calories;

    /**
     * Обновленное содержание белков (в граммах).
     * Должно быть положительным или равным нулю.
     */
    @Min(0)
    private Double proteins;

    /**
     * Обновленное содержание жиров (в граммах).
     * Должно быть положительным или равным нулю.
     */
    @Min(0)
    private Double fats;

    /**
     * Обновленное содержание углеводов (в граммах).
     * Должно быть положительным или равным нулю.
     */
    @Min(0)
    private Double carbohydrates;

    /**
     * Идентификатор приема пищи, к которому привязано блюдо.
     * Должен быть положительным или равным нулю.
     * Null не допускается (используйте 0 для сброса привязки).
     */
    @Min(0)
    private Long mealId;
}
