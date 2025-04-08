package org.example.calorietracker.dto.dish;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.calorietracker.model.Dish;

/**
 * DTO for {@link Dish}
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DishDTO {
    private Long id;
    private String name;
    @Min(0)
    private double calories;
    @Min(0)
    private double proteins;
    @Min(0)
    private double fats;
    @Min(0)
    private double carbohydrates;
    private Long mealId;
}