package org.example.calorietracker.service;

import lombok.RequiredArgsConstructor;
import org.example.calorietracker.dto.dish.DishCreateDTO;
import org.example.calorietracker.dto.dish.DishDTO;
import org.example.calorietracker.dto.dish.DishUpdateDTO;
import org.example.calorietracker.exception.ResourceNotFoundException;
import org.example.calorietracker.mapper.DishMapper;
import org.example.calorietracker.model.Dish;
import org.example.calorietracker.model.Meal;
import org.example.calorietracker.model.User;
import org.example.calorietracker.repository.DishRepository;
import org.example.calorietracker.repository.MealRepository;
import org.example.calorietracker.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DishService {
    private final DishRepository dishRepository;
    private final MealRepository mealRepository;
    private final DishMapper dishMapper;

    public DishDTO getById(Long id) throws ResourceNotFoundException {
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dish not found"));
        return dishMapper.map(dish);
    }

    public List<DishDTO> getAll() {
        List<Dish> dishes = dishRepository.findAll();
        return dishes.stream()
                .map(dishMapper::map)
                .toList();
    }

    public DishDTO create(DishCreateDTO createDTO) {
        Long meatId = createDTO.getMealId();
        Meal meal = null;
        if (meatId != null) {
            meal = mealRepository.findById(meatId)
                    .orElseThrow(() -> new ResourceNotFoundException("Meal not found"));
        }

        Dish dish = dishMapper.map(createDTO);
        dish.setMeal(meal);

        dishRepository.save(dish);
        return dishMapper.map(dish);
    }

    public DishDTO update(DishUpdateDTO updateDTO, Long id) throws ResourceNotFoundException {
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dish not found"));
        dishMapper.update(updateDTO, dish);
        dishRepository.save(dish);
        return dishMapper.map(dish);
    }

    public void delete(Long id) throws ResourceNotFoundException {
        dishRepository.deleteById(id);
    }
}
