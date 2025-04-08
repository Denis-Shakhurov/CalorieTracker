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
 * DTO for {@link org.example.calorietracker.model.User}
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;

    private String name;

    private String email;

    @Min(1)
    @Max(100)
    private Integer age;

    @Min(5)
    private Double weight;

    @Min(30)
    private Double height;

    private Double dailyCalorieIntake;

    private GenderType gender;

    private GoalType goal;

    private List<Long> mealIds;
}