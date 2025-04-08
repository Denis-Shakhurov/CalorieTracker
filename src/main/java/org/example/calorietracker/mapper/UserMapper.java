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

@Mapper(
        uses = {ReferenceMapper.class, JsonNullableMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class UserMapper {
    @Autowired
    private MealRepository mealRepository;

    public abstract User map(UserDTO userDTO);

    public abstract UserDTO map(User user);

    public abstract User map(UserCreateDTO userCreateDTO);

    public abstract void update(UserUpdateDTO userUpdateDTO, @MappingTarget User user);

    public List<Meal> longToMeals(List<Long> mealIds) {
        return mealIds.stream()
                .map(id -> mealRepository.findById(id).orElseThrow())
                .collect(Collectors.toList());
    }

    public List<Long> mealToLongs(List<Meal> meals) {
        return meals == null
                ? new ArrayList<>()
                : meals.stream()
                .map(Meal::getId)
                .collect(Collectors.toList());
    }
}
