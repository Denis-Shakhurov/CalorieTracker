package org.example.calorietracker.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.calorietracker.dto.meal.MealDTO;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DailyReport {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDate date;
    private List<MealDTO> meals;
    private double totalCalories;
}
