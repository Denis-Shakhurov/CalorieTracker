package org.example.calorietracker.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.calorietracker.model.GenderType;
import org.example.calorietracker.model.GoalType;

/**
 * Data Transfer Object (DTO) для обновления данных пользователя.
 * Содержит поля, которые могут быть изменены при обновлении профиля пользователя.
 * Все числовые параметры имеют ограничения на минимальные/максимальные значения.
 *
 * @see org.example.calorietracker.model.User Сущность пользователя в модели данных
 */
@Getter
@AllArgsConstructor
public class UserUpdateDTO {
    /**
     * Обновленное имя пользователя.
     * Не может быть пустым или состоять только из пробелов.
     */
    @NotBlank
    private String name;

    /**
     * Обновленный email пользователя.
     * Должен быть в валидном формате email.
     * Может быть null, если email не требуется обновлять.
     */
    @Email
    private String email;

    /**
     * Обновленный возраст пользователя в годах.
     * Должен быть в диапазоне от 1 до 100 лет.
     * Может быть null, если возраст не требуется обновлять.
     */
    @Min(1)
    @Max(100)
    private Integer age;

    /**
     * Обновленный вес пользователя в килограммах.
     * Минимальное значение - 5 кг.
     * Может быть null, если вес не требуется обновлять.
     */
    @Min(5)
    private Double weight;

    /**
     * Обновленный рост пользователя в сантиметрах.
     * Минимальное значение - 30 см.
     * Может быть null, если рост не требуется обновлять.
     */
    @Min(30)
    private Double height;

    /**
     * Обновленный пол пользователя.
     * Определяется перечислением GenderType.
     * Может быть null, если пол не требуется обновлять.
     */
    private GenderType gender;

    /**
     * Обновленная цель пользователя.
     * Определяется перечислением GoalType.
     * Может быть null, если цель не требуется обновлять.
     */
    private GoalType goal;
}
