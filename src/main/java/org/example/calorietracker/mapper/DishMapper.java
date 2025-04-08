package org.example.calorietracker.mapper;

import org.example.calorietracker.dto.dish.DishCreateDTO;
import org.example.calorietracker.dto.dish.DishDTO;
import org.example.calorietracker.dto.dish.DishUpdateDTO;
import org.example.calorietracker.exception.ResourceNotFoundException;
import org.example.calorietracker.model.Dish;
import org.example.calorietracker.model.Meal;
import org.example.calorietracker.repository.MealRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Маппер для преобразования между сущностью Dish и DTO.
 * Использует MapStruct для автоматического преобразования объектов.
 *
 * <p>Основные преобразования:
 * <ul>
 *   <li>Dish ↔ DishDTO</li>
 *   <li>DishCreateDTO → Dish</li>
 *   <li>Обновление Dish из DishUpdateDTO</li>
 * </ul>
 *
 * <p>Особенности:
 * <ul>
 *   <li>Игнорирует null-значения при обновлении</li>
 *   <li>Автоматически преобразует mealId ↔ Meal</li>
 *   <li>Генерирует исключение при отсутствии связанного Meal</li>
 * </ul>
 */
@Mapper(
        uses = {ReferenceMapper.class, JsonNullableMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class DishMapper {
    @Autowired
    private MealRepository mealRepository;

    /**
     * Преобразует сущность Dish в DishDTO.
     *
     * @param dish сущность блюда
     * @return DTO блюда
     */
    @Mapping(target = "mealId", source = "meal.id")
    public abstract DishDTO map(Dish dish);

    /**
     * Преобразует DishDTO в сущность Dish.
     *
     * @param dishDTO DTO блюда
     * @return сущность блюда
     */
    @Mapping(target = "meal", source = "mealId")
    public abstract Dish map(DishDTO dishDTO);

    /**
     * Преобразует DishCreateDTO в сущность Dish.
     *
     * @param dishCreateDTO DTO для создания блюда
     * @return сущность блюда
     */
    @Mapping(target = "meal", source = "mealId")
    public abstract Dish map(DishCreateDTO dishCreateDTO);

    /**
     * Обновляет сущность Dish из DishUpdateDTO.
     *
     * @param dishUpdateDTO DTO для обновления блюда
     * @param dish сущность блюда для обновления
     */
    @Mapping(target = "meal", source = "mealId")
    public abstract void update(DishUpdateDTO dishUpdateDTO, @MappingTarget Dish dish);

    /**
     * Преобразует ID приема пищи в сущность Meal.
     *
     * @param mealId ID приема пищи
     * @return сущность Meal
     * @throws ResourceNotFoundException если прием пищи не найден
     */
    public Meal longToMeal(Long mealId) {
        return mealId == null
                ? null
                : mealRepository.findById(mealId)
                .orElseThrow(() -> new ResourceNotFoundException("Meal not found"));
    }
}
