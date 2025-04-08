package org.example.calorietracker.dto.meal;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * DTO for {@link org.example.calorietracker.model.Meal}
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MealCreateDTO {
    @NotNull
    private Long userId;

    private List<Long> dishIds;
}