package org.example.calorietracker.dto.user;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.calorietracker.model.GenderType;
import org.example.calorietracker.model.GoalType;

/**
 * Data Transfer Object (DTO) для создания нового пользователя.
 * Содержит все необходимые данные для регистрации пользователя в системе,
 * включая персональные данные и цели пользователя.
 *
 * @see org.example.calorietracker.model.User Сущность пользователя в модели данных
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateDTO {
    /**
     * Полное имя пользователя.
     * Не может быть пустым или состоять только из пробелов.
     * Обязательное поле.
     */
    @NotBlank
    @NotNull
    private String name;

    /**
     * Электронная почта пользователя.
     * Должна быть в валидном формате email.
     * Не обязательное поле (может быть null).
     */
    @Email
    private String email;

    /**
     * Возраст пользователя в годах.
     * Должен быть в диапазоне от 1 до 100 лет.
     * Обязательное поле.
     */
    @Min(1)
    @Max(100)
    @NotNull
    private Integer age;

    /**
     * Вес пользователя в килограммах.
     * Минимальное значение - 5 кг.
     * Обязательное поле.
     */
    @Min(5)
    @NotNull
    private Double weight;

    /**
     * Рост пользователя в сантиметрах.
     * Минимальное значение - 30 см.
     * Обязательное поле.
     */
    @Min(30)
    @NotNull
    private Double height;

    /**
     * Пол пользователя.
     * Допустимые значения определяются enum GenderType.
     * Обязательное поле.
     */
    @NotNull
    private GenderType gender;

    /**
     * Цель пользователя (например, похудение, поддержание веса и т.д.).
     * Допустимые значения определяются enum GoalType.
     * Обязательное поле.
     */
    @NotNull
    private GoalType goal;
}
