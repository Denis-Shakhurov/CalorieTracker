package org.example.calorietracker.service;

import lombok.RequiredArgsConstructor;
import org.example.calorietracker.dto.user.UserCreateDTO;
import org.example.calorietracker.dto.user.UserDTO;
import org.example.calorietracker.dto.user.UserUpdateDTO;
import org.example.calorietracker.exception.ResourceNotFoundException;
import org.example.calorietracker.mapper.UserMapper;
import org.example.calorietracker.model.GenderType;
import org.example.calorietracker.model.GoalType;
import org.example.calorietracker.model.User;
import org.example.calorietracker.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Сервис для работы с пользователями.
 * Обеспечивает управление пользователями и расчет их дневной нормы калорий.
 */
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    /**
     * Получает список всех пользователей.
     *
     * @return список DTO всех пользователей
     */
    public List<UserDTO> getAll() {
        return userRepository.findAll().stream()
                .map(userMapper::map)
                .toList();
    }

    /**
     * Получает пользователя по идентификатору.
     *
     * @param id идентификатор пользователя
     * @return DTO пользователя
     * @throws ResourceNotFoundException если пользователь не найден
     */
    public UserDTO getById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return userMapper.map(user);
    }

    /**
     * Создает нового пользователя.
     * Автоматически рассчитывает дневную норму калорий.
     *
     * @param createDTO DTO с данными для создания пользователя
     * @return DTO созданного пользователя
     */
    public UserDTO create(UserCreateDTO createDTO) {
        User user = userMapper.map(createDTO);

        double bmr = calculateBMR(user);
        double dailyCalories = calculateDailyCalorieIntake(bmr, user.getGoal());
        user.setDailyCalorieIntake(dailyCalories);

        return userMapper.map(userRepository.save(user));
    }

    /**
     * Обновляет данные пользователя.
     *
     * @param updateDTO DTO с обновленными данными
     * @param id идентификатор пользователя
     * @return DTO обновленного пользователя
     * @throws ResourceNotFoundException если пользователь не найден
     */
    public UserDTO update(UserUpdateDTO updateDTO, Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        userMapper.update(updateDTO, user);

        // Пересчет калорий при изменении параметров
        if (needsRecalculation(updateDTO)) {
            double bmr = calculateBMR(user);
            double dailyCalories = calculateDailyCalorieIntake(bmr, user.getGoal());
            user.setDailyCalorieIntake(dailyCalories);
        }

        return userMapper.map(userRepository.save(user));
    }

    /**
     * Удаляет пользователя.
     *
     * @param id идентификатор пользователя
     * @throws ResourceNotFoundException если пользователь не найден
     */
    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found");
        }
        userRepository.deleteById(id);
    }

    /**
     * Получает дневную норму калорий пользователя.
     *
     * @param userId идентификатор пользователя
     * @return дневная норма калорий
     * @throws ResourceNotFoundException если пользователь не найден
     */
    public double getDailyCalorieIntake(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"))
                .getDailyCalorieIntake();
    }

    /**
     * Рассчитывает базовый метаболизм (BMR) по формуле Миффлина-Сан Жеора.
     *
     * @param user сущность пользователя
     * @return значение базового метаболизма
     * @throws IllegalArgumentException если данные пользователя некорректны
     */
    private double calculateBMR(User user) {
        validateUserParameters(user);

        if (user.getGender() == GenderType.MALE) {
            return 88.362 + (13.397 * user.getWeight()) + (4.799 * user.getHeight()) - (5.677 * user.getAge());
        } else {
            return 447.593 + (9.247 * user.getWeight()) + (3.098 * user.getHeight()) - (4.330 * user.getAge());
        }
    }

    /**
     * Рассчитывает дневную норму калорий на основе BMR и цели пользователя.
     *
     * @param bmr базовый метаболизм
     * @param goal цель пользователя
     * @return рекомендуемая дневная норма калорий
     */
    private double calculateDailyCalorieIntake(double bmr, GoalType goal) {
        return switch (goal) {
            case WEIGHT_LOSS -> bmr * 0.8;    // Дефицит 20%
            case MAINTENANCE -> bmr;           // Поддержание веса
            case WEIGHT_GAIN -> bmr * 1.2;     // Профицит 20%
        };
    }

    /**
     * Проверяет, нужно ли пересчитывать калории при обновлении.
     */
    private boolean needsRecalculation(UserUpdateDTO updateDTO) {
        return updateDTO.getWeight() != null ||
                updateDTO.getHeight() != null ||
                updateDTO.getAge() != null ||
                updateDTO.getGender() != null ||
                updateDTO.getGoal() != null;
    }

    /**
     * Валидирует параметры пользователя для расчета BMR.
     */
    private void validateUserParameters(User user) {
        if (user.getWeight() == null || user.getHeight() == null ||
                user.getAge() == null || user.getGender() == null) {
            throw new IllegalArgumentException("User parameters are incomplete for BMR calculation");
        }
    }
}
