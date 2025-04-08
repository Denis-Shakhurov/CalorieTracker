package org.example.calorietracker.service;

import lombok.RequiredArgsConstructor;
import org.example.calorietracker.dto.DailyReport;
import org.example.calorietracker.dto.meal.MealCreateDTO;
import org.example.calorietracker.dto.meal.MealDTO;
import org.example.calorietracker.exception.ResourceNotFoundException;
import org.example.calorietracker.mapper.MealMapper;
import org.example.calorietracker.model.Dish;
import org.example.calorietracker.model.Meal;
import org.example.calorietracker.model.User;
import org.example.calorietracker.repository.MealRepository;
import org.example.calorietracker.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MealService {
    private final MealRepository mealRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final MealMapper mealMapper;

    public MealDTO getById(Long id) throws ResourceNotFoundException {
        Meal meal = mealRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Meal not found"));
        return mealMapper.map(meal);
    }

    public List<MealDTO> getAll() {
        List<Meal> meals = mealRepository.findAll();
        return meals.stream()
                .map(mealMapper::map)
                .toList();
    }

    public MealDTO create(MealCreateDTO createDTO) {
        User user = userRepository.findById(createDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Meal meal = mealMapper.map(createDTO);
        meal.setUser(user);

        mealRepository.save(meal);
        return mealMapper.map(meal);
    }

    public void delete(Long id) throws ResourceNotFoundException {
        mealRepository.deleteById(id);
    }

    /**
     * Получить отчет за день: список приемов пищи и общее количество калорий.
     */
    public DailyReport getDailyReport(Long userId, LocalDate date) {
        // Получаем все приемы пищи за день
        List<Meal> meals = mealRepository.findByUserIdAndCreatedAt(userId, date);
        List<MealDTO> mealDTOS = meals.stream()
                .map(mealMapper::map)
                .toList();

        Double totalCalories = mealRepository.findTotalCaloriesByUserIdAndCreatedAt(userId, date);

        // Формируем отчет
        return new DailyReport(date, mealDTOS, totalCalories);
    }

    /**
     * Проверить, уложился ли пользователь в дневную норму калорий.
     */
    public boolean isWithinDailyLimit(Long userId, LocalDate date) {
        double dailyIntake = userService.getDailyCalorieIntake(userId);
        double totalCalories = mealRepository.findTotalCaloriesByUserIdAndCreatedAt(userId, date);
        return totalCalories <= dailyIntake;
    }

    /**
     * Получить общее количество калорий за день.
     */
//    public double getTotalCaloriesConsumed(Long userId, LocalDate date) {
//        List<Meal> meals = mealRepository.findByUserIdAndCreatedAt(userId, date);
//        return meals.stream()
//                .flatMap(meal -> meal.getDishes().stream())
//                .mapToDouble(Dish::getCalories)
//                .sum();
//    }

    /**
     * Получить историю питания за несколько дней.
     */
    public List<DailyReport> getNutritionHistory(Long userId, LocalDate startDate, LocalDate endDate) {
        List<LocalDate> dates = startDate.datesUntil(endDate.plusDays(1))
                .collect(Collectors.toList());

        return dates.stream()
                .map(date -> getDailyReport(userId, date))
                .collect(Collectors.toList());
    }
}
