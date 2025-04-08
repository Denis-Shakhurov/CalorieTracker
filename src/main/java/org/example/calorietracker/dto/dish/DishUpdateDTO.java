package org.example.calorietracker.dto.dish;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.openapitools.jackson.nullable.JsonNullable;

/**
 * DTO for {@link org.example.calorietracker.model.Dish}
 */
@AllArgsConstructor
@Getter
public class DishUpdateDTO {
    private JsonNullable<Long> id;

    @NotBlank
    private JsonNullable<String> name;

    @Min(0)
    private JsonNullable<Double> calories;

    @Min(0)
    private JsonNullable<Double> proteins;

    @Min(0)
    private JsonNullable<Double> fats;

    @Min(0)
    private JsonNullable<Double> carbohydrates;
}