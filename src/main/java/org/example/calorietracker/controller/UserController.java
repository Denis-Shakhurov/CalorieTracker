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
import org.example.calorietracker.dto.user.UserCreateDTO;
import org.example.calorietracker.dto.user.UserDTO;
import org.example.calorietracker.dto.user.UserUpdateDTO;
import org.example.calorietracker.service.UserService;
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

@Tag(name = "User Controller", description = "API для управления пользователями")
@Validated
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Operation(
            summary = "Получить всех пользователей",
            description = "Возвращает список всех зарегистрированных пользователей"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Список пользователей успешно получен",
            content = @Content(schema = @Schema(implementation = UserDTO[].class))
    )
    @GetMapping
    public ResponseEntity<List<UserDTO>> findAll() {
        List<UserDTO> userDTOS = userService.getAll();
        return ResponseEntity.ok().body(userDTOS);
    }

    @Operation(
            summary = "Получить пользователя по ID",
            description = "Возвращает данные пользователя по указанному идентификатору"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Пользователь найден",
                    content = @Content(schema = @Schema(implementation = UserDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь не найден",
                    content = @Content
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(
            @Parameter(description = "ID пользователя", required = true, example = "1")
            @PathVariable Long id) {
        UserDTO userDTO = userService.getById(id);
        return ResponseEntity.ok(userDTO);
    }

    @Operation(
            summary = "Создать нового пользователя",
            description = "Регистрирует нового пользователя в системе"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Пользователь успешно создан",
                    content = @Content(schema = @Schema(implementation = UserDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Некорректные данные пользователя",
                    content = @Content
            )
    })
    @PostMapping
    public ResponseEntity<UserDTO> create(
            @Parameter(description = "Данные для создания пользователя", required = true)
            @RequestBody @Valid UserCreateDTO userCreateDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.create(userCreateDTO));
    }

    @Operation(
            summary = "Обновить данные пользователя",
            description = "Обновляет информацию о существующем пользователе"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Данные пользователя успешно обновлены",
                    content = @Content(schema = @Schema(implementation = UserDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Некорректные данные для обновления",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь не найден",
                    content = @Content
            )
    })
    @PostMapping("/{id}")
    public ResponseEntity<UserDTO> update(
            @Parameter(description = "Данные для обновления пользователя", required = true)
            @RequestBody @Valid UserUpdateDTO userUpdateDTO,
            @Parameter(description = "ID пользователя для обновления", required = true, example = "1")
            @PathVariable Long id) {
        UserDTO userDTO = userService.update(userUpdateDTO, id);
        return ResponseEntity.ok(userDTO);
    }

    @Operation(
            summary = "Удалить пользователя",
            description = "Удаляет пользователя по указанному идентификатору"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Пользователь успешно удален",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь не найден",
                    content = @Content
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID пользователя для удаления", required = true, example = "1")
            @PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
