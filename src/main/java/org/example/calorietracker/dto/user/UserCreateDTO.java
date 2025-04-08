package org.example.calorietracker.dto.user;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.calorietracker.model.GenderType;
import org.example.calorietracker.model.GoalType;

/**
 * DTO for {@link org.example.calorietracker.model.User}
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateDTO {
    @NotBlank
    @NotNull
    private String name;

    @Email
    private String email;

    @Min(1)
    @Max(100)
    @NotNull
    private Integer age;

    @Min(5)
    @NotNull
    private Double weight;

    @Min(30)
    @NotNull
    private Double height;

    @NotNull
    private GenderType gender;

    @NotNull
    private GoalType goal;
}