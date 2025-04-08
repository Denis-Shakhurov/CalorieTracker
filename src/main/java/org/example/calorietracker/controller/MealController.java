package org.example.calorietracker.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.calorietracker.dto.meal.MealCreateDTO;
import org.example.calorietracker.dto.meal.MealDTO;
import org.example.calorietracker.service.MealService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Meal Controller", description = "API для управления приемами пищи")
@Validated
@RestController
@RequestMapping("/meals")
@RequiredArgsConstructor
public class MealController {
    private final MealService mealService;

    @Operation(
            summary = "Получить прием пищи по ID",
            description = "Возвращает данные о приеме пищи по указанному идентификатору"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Прием пищи найден",
                    content = @Content(schema = @Schema(implementation = MealDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Прием пищи не найден",
                    content = @Content
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<MealDTO> getMeal(
            @Parameter(description = "ID приема пищи", example = "1", required = true)
            @PathVariable Long id) {
        MealDTO mealDTO = mealService.getById(id);
        return ResponseEntity.ok(mealDTO);
    }

    @Operation(
            summary = "Получить все приемы пищи",
            description = "Возвращает список всех приемов пищи"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Список приемов пищи успешно получен",
            content = @Content(schema = @Schema(implementation = MealDTO[].class)))
    @GetMapping
    public ResponseEntity<List<MealDTO>> getMeals() {
        List<MealDTO> mealDTOS = mealService.getAll();
        return ResponseEntity.ok(mealDTOS);
    }

    @Operation(
            summary = "Создать новый прием пищи",
            description = "Создает новую запись о приеме пищи"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Прием пищи успешно создан",
                    content = @Content(schema = @Schema(implementation = MealDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Некорректные данные запроса",
                    content = @Content
            )
    })
    @PostMapping
    public ResponseEntity<MealDTO> create(
            @Parameter(description = "Данные для создания приема пищи", required = true)
            @RequestBody @Valid MealCreateDTO mealCreateDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(mealService.create(mealCreateDTO));
    }

    @Operation(
            summary = "Удалить прием пищи",
            description = "Удаляет запись о приеме пищи по указанному идентификатору"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Прием пищи успешно удален",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Прием пищи не найден",
                    content = @Content
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID приема пищи для удаления", example = "1", required = true)
            @PathVariable Long id) {
        mealService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
