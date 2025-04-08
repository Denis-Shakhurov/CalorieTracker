package org.example.calorietracker.service;

import lombok.RequiredArgsConstructor;
import org.example.calorietracker.dto.user.UserCreateDTO;
import org.example.calorietracker.dto.user.UserDTO;
import org.example.calorietracker.dto.user.UserUpdateDTO;
import org.example.calorietracker.exception.ResourceNotFoundException;
import org.example.calorietracker.mapper.UserMapper;
import org.example.calorietracker.model.GoalType;
import org.example.calorietracker.model.User;
import org.example.calorietracker.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public List<UserDTO> getAll() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(userMapper::map)
                .toList();
    }

    public UserDTO getById(Long id) throws ResourceNotFoundException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return userMapper.map(user);
    }

    public UserDTO create(UserCreateDTO createDTO) {
        User user = userMapper.map(createDTO);

        double bmr = calculateBMR(user);
        user.setDailyCalorieIntake(calculateDailyCalorieIntake(
                bmr,
                user.getGoal()
        ));

        userRepository.save(user);

        return userMapper.map(user);
    }

    public UserDTO update(UserUpdateDTO updateDTO, Long id) throws ResourceNotFoundException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        userMapper.update(updateDTO, user);

        userRepository.save(user);
        return userMapper.map(user);
    }

    public void delete(Long id) throws ResourceNotFoundException {
        userRepository.deleteById(id);
    }

    /**
     * Получить дневную норму калорий пользователя.
     */
    public double getDailyCalorieIntake(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return user.getDailyCalorieIntake();
    }


    private double calculateBMR(User user) {
        if (user.getGender().equals("male")) {
            return 88.362 + (13.397 * user.getWeight()) + (4.799 * user.getHeight()) - (5.677 * user.getAge());
        } else {
            return 447.593 + (9.247 * user.getWeight()) + (3.098 * user.getHeight()) - (4.330 * user.getAge());
        }
    }

    private double calculateDailyCalorieIntake(double bmr, GoalType goal) {
        return switch (goal) {
            case WEIGHT_LOSS -> bmr * 0.8;
            case MAINTENANCE -> bmr;
            case WEIGHT_GAIN -> bmr * 1.2;
            default -> bmr;
        };
    }
}
