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

@Mapper(
        uses = {ReferenceMapper.class, JsonNullableMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class DishMapper {
    @Autowired
    private MealRepository mealRepository;

    @Mapping(target = "mealId", source = "meal.id")
    public abstract DishDTO map(Dish dish);

    @Mapping(target = "meal", source = "mealId")
    public abstract Dish map(DishDTO dishDTO);

    @Mapping(target = "meal", source = "mealId")
    public abstract Dish map(DishCreateDTO dishCreateDTO);

    @Mapping(target = "meal", source = "mealId")
    public abstract void update(DishUpdateDTO dishUpdateDTO, @MappingTarget Dish dish);

    public Meal longToMeal(Long mealId) {
        return mealId == null
                ? null
                : mealRepository.findById(mealId)
                    .orElseThrow(() -> new ResourceNotFoundException("Meal not found"));
    }
}
