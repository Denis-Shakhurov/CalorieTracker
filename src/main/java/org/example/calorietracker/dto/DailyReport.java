package org.example.calorietracker.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.calorietracker.dto.meal.MealDTO;

import java.time.LocalDate;
import java.util.List;

/**
 * DTO для представления дневного отчета о питании.
 * Содержит информацию о всех приемах пищи за указанный день
 * и общее количество потребленных калорий.
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DailyReport {
    /**
     * Дата, за которую сформирован отчет.
     * Формат сериализации: строка в формате ISO-8601 (yyyy-MM-dd)
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDate date;

    /**
     * Список приемов пищи за указанный день.
     * Каждый прием пищи представлен в виде MealDTO.
     * Список может быть пустым, если в этот день не было приемов пищи.
     */
    private List<MealDTO> meals;

    /**
     * Суммарное количество потребленных калорий за день.
     * Рассчитывается как сумма калорий из всех приемов пищи.
     * Значение всегда неотрицательное.
     */
    private double totalCalories;
}
