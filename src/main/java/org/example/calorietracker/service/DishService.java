package org.example.calorietracker.service;

import lombok.RequiredArgsConstructor;
import org.example.calorietracker.dto.dish.DishCreateDTO;
import org.example.calorietracker.dto.dish.DishDTO;
import org.example.calorietracker.dto.dish.DishUpdateDTO;
import org.example.calorietracker.exception.ResourceNotFoundException;
import org.example.calorietracker.mapper.DishMapper;
import org.example.calorietracker.model.Dish;
import org.example.calorietracker.model.Meal;
import org.example.calorietracker.repository.DishRepository;
import org.example.calorietracker.repository.MealRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Сервис для работы с блюдами.
 * Обеспечивает бизнес-логику для операций CRUD с блюдами.
 */
@Service
@RequiredArgsConstructor
public class DishService {
    private final DishRepository dishRepository;
    private final MealRepository mealRepository;
    private final DishMapper dishMapper;

    /**
     * Получает блюдо по идентификатору.
     *
     * @param id идентификатор блюда
     * @return DTO блюда
     * @throws ResourceNotFoundException если блюдо не найдено
     */
    public DishDTO getById(Long id) {
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dish not found"));
        return dishMapper.map(dish);
    }

    /**
     * Получает список всех блюд.
     *
     * @return список DTO всех блюд
     */
    public List<DishDTO> getAll() {
        return dishRepository.findAll().stream()
                .map(dishMapper::map)
                .toList();
    }

    /**
     * Создает новое блюдо.
     *
     * @param createDTO DTO с данными для создания блюда
     * @return DTO созданного блюда
     * @throws ResourceNotFoundException если связанный прием пищи не найден
     */
    public DishDTO create(DishCreateDTO createDTO) {
        Meal meal = resolveMeal(createDTO.getMealId());

        Dish dish = dishMapper.map(createDTO);
        dish.setMeal(meal);

        return dishMapper.map(dishRepository.save(dish));
    }

    /**
     * Обновляет существующее блюдо.
     *
     * @param updateDTO DTO с обновленными данными блюда
     * @param id идентификатор обновляемого блюда
     * @return DTO обновленного блюда
     * @throws ResourceNotFoundException если блюдо не найдено
     */
    public DishDTO update(DishUpdateDTO updateDTO, Long id) {
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dish not found"));

        dishMapper.update(updateDTO, dish);
        return dishMapper.map(dishRepository.save(dish));
    }

    /**
     * Удаляет блюдо.
     *
     * @param id идентификатор удаляемого блюда
     * @throws ResourceNotFoundException если блюдо не найдено
     */
    public void delete(Long id) {
        if (!dishRepository.existsById(id)) {
            throw new ResourceNotFoundException("Dish not found");
        }
        dishRepository.deleteById(id);
    }

    /**
     * Находит прием пищи по идентификатору.
     *
     * @param mealId идентификатор приема пищи (может быть null)
     * @return сущность Meal или null если mealId равен null
     * @throws ResourceNotFoundException если прием пищи не найден
     */
    private Meal resolveMeal(Long mealId) {
        if (mealId == null) {
            return null;
        }
        return mealRepository.findById(mealId)
                .orElseThrow(() -> new ResourceNotFoundException("Meal not found"));
    }
}
