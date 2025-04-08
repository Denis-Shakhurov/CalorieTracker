package org.example.calorietracker.mapper;

import lombok.RequiredArgsConstructor;
import org.example.calorietracker.dto.meal.MealCreateDTO;
import org.example.calorietracker.dto.meal.MealDTO;
import org.example.calorietracker.model.Dish;
import org.example.calorietracker.model.Meal;
import org.example.calorietracker.repository.DishRepository;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(
        uses = {ReferenceMapper.class, JsonNullableMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class MealMapper {
    @Autowired
    private  DishRepository dishRepository;

    @Mapping(target = "dishes", source = "dishIds")
    @Mapping(target = "user", ignore = true)
    public abstract Meal map(MealDTO mealDTO);

    @Mapping(target = "dishIds", source = "dishes")
    @Mapping(target = "userId", source = "user.id")
    public abstract MealDTO map(Meal meal);

    @Mapping(target = "dishes", source = "dishIds")
    @Mapping(target = "user", ignore = true)
    public abstract Meal map(MealCreateDTO mealCreateDTO);

    public List<Dish> longToDish(List<Long> dishIds) {
        return dishIds == null
                ? new ArrayList<>()
                : dishIds.stream()
                .map(id -> dishRepository.findById(id).orElseThrow())
                .collect(Collectors.toList());
    }

    public List<Long> dishToLong(List<Dish> dishes) {
        return dishes.stream()
                .map(Dish::getId)
                .collect(Collectors.toList());
    }
}
