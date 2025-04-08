package org.example.calorietracker.dto.dish;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * DTO for {@link org.example.calorietracker.model.Dish}
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class DishUpdateDTO {
    @NotBlank
    private String name;

    @Min(0)
    private Double calories;

    @Min(0)
    private Double proteins;

    @Min(0)
    private Double fats;

    @Min(0)
    private Double carbohydrates;

    @Min(0)
    private Long mealId;
}