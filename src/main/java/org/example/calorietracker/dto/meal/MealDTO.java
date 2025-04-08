package org.example.calorietracker.dto.meal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.calorietracker.model.Meal;

import java.time.LocalDate;
import java.util.List;

/**
 * DTO for {@link Meal}
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MealDTO {
    private Long id;

    private LocalDate createdAt;

    private Long userId;

    private List<Long> dishIds;
}