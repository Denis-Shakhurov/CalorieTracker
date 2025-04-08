package org.example.calorietracker.mapper;

import org.example.calorietracker.dto.user.UserCreateDTO;
import org.example.calorietracker.dto.user.UserDTO;
import org.example.calorietracker.dto.user.UserUpdateDTO;
import org.example.calorietracker.model.Meal;
import org.example.calorietracker.model.User;
import org.example.calorietracker.repository.MealRepository;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Маппер для преобразования между сущностью User и соответствующими DTO.
 * Обеспечивает конвертацию данных пользователя в обоих направлениях.
 *
 * <p>Поддерживаемые преобразования:
 * <ul>
 *   <li>User ↔ UserDTO (двустороннее преобразование)</li>
 *   <li>UserCreateDTO → User (создание нового пользователя)</li>
 *   <li>UserUpdateDTO → User (обновление существующего пользователя)</li>
 * </ul>
 *
 * <p>Особенности:
 * <ul>
 *   <li>Игнорирует null-значения при обновлении</li>
 *   <li>Автоматически преобразует списки ID приемов пищи ↔ List<Meal></li>
 *   <li>Работает в контексте Spring (внедрение зависимостей)</li>
 * </ul>
 */
@Mapper(
        uses = {ReferenceMapper.class, JsonNullableMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class UserMapper {
    @Autowired
    private MealRepository mealRepository;

    /**
     * Преобразует UserDTO в сущность User.
     *
     * @param userDTO DTO пользователя
     * @return сущность User
     */
    public abstract User map(UserDTO userDTO);

    /**
     * Преобразует сущность User в UserDTO.
     *
     * @param user сущность пользователя
     * @return UserDTO
     */
    public abstract UserDTO map(User user);

    /**
     * Преобразует UserCreateDTO в сущность User.
     *
     * @param userCreateDTO DTO для создания пользователя
     * @return новая сущность User
     */
    public abstract User map(UserCreateDTO userCreateDTO);

    /**
     * Обновляет существующую сущность User из UserUpdateDTO.
     *
     * @param userUpdateDTO DTO с обновленными данными
     * @param user сущность User для обновления
     */
    public abstract void update(UserUpdateDTO userUpdateDTO, @MappingTarget User user);

    /**
     * Преобразует список ID приемов пищи в список сущностей Meal.
     *
     * @param mealIds список ID приемов пищи
     * @return список сущностей Meal
     * @throws jakarta.persistence.EntityNotFoundException если прием пищи не найден
     */
    public List<Meal> longToMeals(List<Long> mealIds) {
        return mealIds.stream()
                .map(id -> mealRepository.findById(id).orElseThrow())
                .collect(Collectors.toList());
    }

    /**
     * Преобразует список сущностей Meal в список их ID.
     *
     * @param meals список сущностей Meal (может быть null)
     * @return список ID приемов пищи (пустой список, если входной null)
     */
    public List<Long> mealToLongs(List<Meal> meals) {
        return meals == null
                ? new ArrayList<>()
                : meals.stream()
                .map(Meal::getId)
                .collect(Collectors.toList());
    }
}
