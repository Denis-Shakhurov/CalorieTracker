package org.example.calorietracker.mapper;

import org.example.calorietracker.dto.meal.MealCreateDTO;
import org.example.calorietracker.dto.meal.MealDTO;
import org.example.calorietracker.model.Dish;
import org.example.calorietracker.model.Meal;
import org.example.calorietracker.repository.DishRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Маппер для преобразования между сущностью Meal и соответствующими DTO.
 * Обеспечивает двустороннее преобразование с учетом связей между сущностями.
 *
 * <p>Основные преобразования:
 * <ul>
 *   <li>Meal ↔ MealDTO</li>
 *   <li>MealCreateDTO → Meal</li>
 * </ul>
 *
 * <p>Особенности:
 * <ul>
 *   <li>Игнорирует null-значения при обновлении</li>
 *   <li>Автоматически преобразует dishIds ↔ List<Dish></li>
 *   <li>Игнорирует пользователя при создании (должен устанавливаться отдельно)</li>
 * </ul>
 */
@Mapper(
        uses = {ReferenceMapper.class, JsonNullableMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class MealMapper {
    @Autowired
    private DishRepository dishRepository;

    /**
     * Преобразует MealDTO в сущность Meal.
     *
     * @param mealDTO DTO приема пищи
     * @return сущность Meal
     */
    @Mapping(target = "dishes", source = "dishIds")
    @Mapping(target = "user", ignore = true)
    public abstract Meal map(MealDTO mealDTO);

    /**
     * Преобразует сущность Meal в MealDTO.
     *
     * @param meal сущность приема пищи
     * @return DTO приема пищи
     */
    @Mapping(target = "dishIds", source = "dishes")
    @Mapping(target = "userId", source = "user.id")
    public abstract MealDTO map(Meal meal);

    /**
     * Преобразует MealCreateDTO в сущность Meal.
     *
     * @param mealCreateDTO DTO для создания приема пищи
     * @return сущность Meal
     */
    @Mapping(target = "dishes", source = "dishIds")
    @Mapping(target = "user", ignore = true)
    public abstract Meal map(MealCreateDTO mealCreateDTO);

    /**
     * Преобразует список ID блюд в список сущностей Dish.
     *
     * @param dishIds список ID блюд
     * @return список сущностей Dish
     * @throws jakarta.persistence.EntityNotFoundException если блюдо не найдено
     */
    public List<Dish> longToDish(List<Long> dishIds) {
        return dishIds == null
                ? new ArrayList<>()
                : dishIds.stream()
                .map(id -> dishRepository.findById(id).orElseThrow())
                .collect(Collectors.toList());
    }

    /**
     * Преобразует список сущностей Dish в список их ID.
     *
     * @param dishes список сущностей Dish
     * @return список ID блюд
     */
    public List<Long> dishToLong(List<Dish> dishes) {
        return dishes.stream()
                .map(Dish::getId)
                .collect(Collectors.toList());
    }
}
