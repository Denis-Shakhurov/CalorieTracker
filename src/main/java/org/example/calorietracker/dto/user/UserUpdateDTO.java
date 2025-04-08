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
 * DTO for {@link org.example.calorietracker.model.User}
 */
@Getter
@AllArgsConstructor
public class UserUpdateDTO {
    @NotBlank
    private String name;

    @Email
    private String email;

    @Min(1)
    @Max(100)
    private Integer age;

    @Min(5)
    private Double weight;

    @Min(30)
    private Double height;

    private GenderType gender;

    private GoalType goal;
}