package org.example.calorietracker.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.calorietracker.dto.DailyReport;
import org.example.calorietracker.service.MealService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "Report Controller", description = "API для работы с отчетами о питании")
@RestController
@RequestMapping("/users/{id}/reports")
@RequiredArgsConstructor
public class ReportController {
    private final MealService mealService;

    @Operation(
            summary = "Получить дневной отчет",
            description = "Возвращает отчет о питании за указанный день"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Отчет успешно получен",
                    content = @Content(schema = @Schema(implementation = DailyReport.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь не найден",
                    content = @Content
            )
    })
    @GetMapping("/daily")
    public ResponseEntity<DailyReport> getDailyReport(
            @Parameter(description = "ID пользователя", required = true, example = "123")
            @PathVariable("id") Long userId,

            @Parameter(
                    description = "Дата отчета",
                    required = true,
                    example = "2023-12-31",
                    schema = @Schema(type = "string", format = "date")
            )
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        DailyReport report = mealService.getDailyReport(userId, date);
        return ResponseEntity.ok(report);
    }

    @Operation(
            summary = "Проверить дневной лимит",
            description = "Проверяет, уложился ли пользователь в дневной лимит калорий"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Результат проверки",
                    content = @Content(schema = @Schema(implementation = Boolean.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь не найден",
                    content = @Content
            )
    })
    @GetMapping("/check-limit")
    public ResponseEntity<Boolean> checkDailyLimit(
            @Parameter(description = "ID пользователя", required = true, example = "123")
            @PathVariable("id") Long userId,

            @Parameter(
                    description = "Дата для проверки",
                    required = true,
                    example = "2023-12-31",
                    schema = @Schema(type = "string", format = "date")
            )
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        boolean isWithinLimit = mealService.isWithinDailyLimit(userId, date);
        return ResponseEntity.ok(isWithinLimit);
    }

    @Operation(
            summary = "Получить историю питания",
            description = "Возвращает отчеты о питании за указанный период"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "История успешно получена",
                    content = @Content(schema = @Schema(implementation = DailyReport[].class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Некорректный диапазон дат",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь не найден",
                    content = @Content
            )
    })
    @GetMapping("/history")
    public ResponseEntity<List<DailyReport>> getNutritionHistory(
            @Parameter(description = "ID пользователя", required = true, example = "123")
            @PathVariable("id") Long userId,

            @Parameter(
                    description = "Начальная дата периода",
                    required = true,
                    example = "2023-01-01",
                    schema = @Schema(type = "string", format = "date")
            )
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,

            @Parameter(
                    description = "Конечная дата периода",
                    required = true,
                    example = "2023-12-31",
                    schema = @Schema(type = "string", format = "date")
            )
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        List<DailyReport> history = mealService.getNutritionHistory(userId, startDate, endDate);
        return ResponseEntity.ok(history);
    }
}
