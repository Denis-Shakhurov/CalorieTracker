package org.example.calorietracker.dto.user;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.calorietracker.model.GenderType;
import org.example.calorietracker.model.GoalType;

import java.util.List;

/**
 * Data Transfer Object (DTO) для пользователя.
 * Содержит все необходимые данные для отображения пользователя в системе,
 * включая персональные данные и цели пользователя.
 *
 * @see org.example.calorietracker.model.User Сущность пользователя в модели данных
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;

    /**
     * Полное имя пользователя.
     */
    private String name;

    private String email;

    private Integer age;

    private Double weight;

    private Double height;

    private Double dailyCalorieIntake;

    private GenderType gender;

    private GoalType goal;

    private List<Long> mealIds;
}