package org.example.calorietracker.service;

import lombok.RequiredArgsConstructor;
import org.example.calorietracker.dto.DailyReport;
import org.example.calorietracker.dto.meal.MealCreateDTO;
import org.example.calorietracker.dto.meal.MealDTO;
import org.example.calorietracker.exception.ResourceNotFoundException;
import org.example.calorietracker.mapper.MealMapper;
import org.example.calorietracker.model.Meal;
import org.example.calorietracker.model.User;
import org.example.calorietracker.repository.MealRepository;
import org.example.calorietracker.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервис для работы с приемами пищи.
 * Обеспечивает бизнес-логику для управления приемами пищи,
 * формирования отчетов и анализа питания.
 */
@Service
@RequiredArgsConstructor
public class MealService {
    private final MealRepository mealRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final MealMapper mealMapper;

    /**
     * Получает прием пищи по идентификатору.
     *
     * @param id идентификатор приема пищи
     * @return DTO приема пищи
     * @throws ResourceNotFoundException если прием пищи не найден
     */
    public MealDTO getById(Long id) throws ResourceNotFoundException {
        Meal meal = mealRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Meal not found"));
        return mealMapper.map(meal);
    }

    /**
     * Получает список всех приемов пищи.
     *
     * @return список DTO всех приемов пищи
     */
    public List<MealDTO> getAll() {
        List<Meal> meals = mealRepository.findAll();
        return meals.stream()
                .map(mealMapper::map)
                .toList();
    }

    /**
     * Создает новый прием пищи.
     *
     * @param createDTO DTO с данными для создания приема пищи
     * @return DTO созданного приема пищи
     * @throws ResourceNotFoundException если пользователь не найден
     */
    public MealDTO create(MealCreateDTO createDTO) {
        User user = userRepository.findById(createDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Meal meal = mealMapper.map(createDTO);
        meal.setUser(user);

        mealRepository.save(meal);
        return mealMapper.map(meal);
    }

    /**
     * Удаляет прием пищи.
     *
     * @param id идентификатор удаляемого приема пищи
     * @throws ResourceNotFoundException если прием пищи не найден
     */
    public void delete(Long id) throws ResourceNotFoundException {
        mealRepository.deleteById(id);
    }

    /**
     * Формирует дневной отчет о питании.
     *
     * @param userId идентификатор пользователя
     * @param date дата для формирования отчета
     * @return отчет с приемами пищи и суммарной калорийностью
     * @throws ResourceNotFoundException если пользователь не найден
     */
    public DailyReport getDailyReport(Long userId, LocalDate date) throws ResourceNotFoundException {
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
     * Проверяет, не превышена ли дневная норма калорий.
     *
     * @param userId идентификатор пользователя
     * @param date дата для проверки
     * @return true если норма не превышена, false в противном случае
     * @throws ResourceNotFoundException если пользователь не найден
     */
    public boolean isWithinDailyLimit(Long userId, LocalDate date) {
        double dailyIntake = userService.getDailyCalorieIntake(userId);
        double totalCalories = mealRepository.findTotalCaloriesByUserIdAndCreatedAt(userId, date);
        return totalCalories <= dailyIntake;
    }

    /**
     * Получает историю питания за указанный период.
     *
     * @param userId идентификатор пользователя
     * @param startDate начальная дата периода (включительно)
     * @param endDate конечная дата периода (включительно)
     * @return список дневных отчетов за период
     * @throws ResourceNotFoundException если пользователь не найден
     * @throws IllegalArgumentException если даты некорректны (startDate > endDate)
     */
    public List<DailyReport> getNutritionHistory(
            Long userId, LocalDate startDate, LocalDate endDate) throws ResourceNotFoundException{
        List<LocalDate> dates = startDate.datesUntil(endDate.plusDays(1))
                .collect(Collectors.toList());

        return dates.stream()
                .map(date -> getDailyReport(userId, date))
                .collect(Collectors.toList());
    }
}
