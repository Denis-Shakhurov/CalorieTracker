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
import org.example.calorietracker.dto.dish.DishCreateDTO;
import org.example.calorietracker.dto.dish.DishDTO;
import org.example.calorietracker.dto.dish.DishUpdateDTO;
import org.example.calorietracker.service.DishService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Dish Controller", description = "API для управления блюдами")
@Validated
@RestController
@RequestMapping("/dishes")
@RequiredArgsConstructor
public class DishController {
    private final DishService dishService;

    @Operation(summary = "Получить блюдо по ID", description = "Возвращает блюдо с указанным идентификатором")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Блюдо найдено",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = DishDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Блюдо не найдено",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<DishDTO> getDish(
            @Parameter(description = "ID блюда", required = true, example = "1")
            @PathVariable Long id) {
        DishDTO dishDTO = dishService.getById(id);
        return ResponseEntity.ok(dishDTO);
    }

    @Operation(summary = "Получить все блюда", description = "Возвращает список всех блюд")
    @ApiResponse(responseCode = "200", description = "Список блюд получен",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = DishDTO.class))})
    @GetMapping
    public ResponseEntity<List<DishDTO>> getDishes() {
        List<DishDTO> dishDTOS = dishService.getAll();
        return ResponseEntity.ok(dishDTOS);
    }

    @Operation(summary = "Создать новое блюдо", description = "Создает новое блюдо и возвращает его")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Блюдо создано",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = DishDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Неверные входные данные",
                    content = @Content)
    })
    @PostMapping
    public ResponseEntity<DishDTO> create(
            @Parameter(description = "Данные для создания блюда", required = true)
            @RequestBody @Valid DishCreateDTO createDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(dishService.create(createDTO));
    }

    @Operation(summary = "Обновить блюдо",
            description = "Обновляет существующее блюдо и возвращает обновленные данные")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Блюдо обновлено",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = DishDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Неверные входные данные",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Блюдо не найдено",
                    content = @Content)
    })
    @PostMapping("/{id}")
    public ResponseEntity<DishDTO> update(
            @Parameter(description = "ID блюда для обновления", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Данные для обновления блюда", required = true)
            @RequestBody @Valid DishUpdateDTO updateDTO) {
        DishDTO dishDTO = dishService.update(updateDTO, id);
        return ResponseEntity.ok(dishDTO);
    }

    @Operation(summary = "Удалить блюдо", description = "Удаляет блюдо с указанным идентификатором")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Блюдо удалено",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Блюдо не найдено",
                    content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID блюда для удаления", required = true, example = "1")
            @PathVariable Long id) {
        dishService.delete(id);
        return ResponseEntity.noContent().build();
    }
}