package org.example.calorietracker.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.calorietracker.model.GenderType;
import org.example.calorietracker.model.GoalType;
import org.openapitools.jackson.nullable.JsonNullable;

/**
 * DTO for {@link org.example.calorietracker.model.User}
 */
@Getter
@AllArgsConstructor
public class UserUpdateDTO {
    @NotBlank
    private JsonNullable<String> name;

    @Email
    private JsonNullable<String> email;

    @Min(1)
    @Max(100)
    private JsonNullable<Integer> age;

    @Min(5)
    private JsonNullable<Double> weight;

    @Min(30)
    private JsonNullable<Double> height;

    private JsonNullable<GenderType> gender;

    private JsonNullable<GoalType> goal;
}