package org.example.calorietracker.repository;

import org.example.calorietracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Репозиторий для работы с сущностями {@link User}.
 * Обеспечивает стандартные CRUD-операции для управления пользователями системы.
 *
 * <p>Наследует все методы из {@link JpaRepository} включая:
 * <ul>
 *   <li>{@code save()} - сохранение и обновление пользователя</li>
 *   <li>{@code findById()} - поиск пользователя по ID</li>
 *   <li>{@code findAll()} - получение всех пользователей</li>
 *   <li>{@code deleteById()} - удаление пользователя</li>
 *   <li>и другие стандартные операции</li>
 * </ul>
 *
 * <p>Примеры использования:
 * <pre>
 * // Создание нового пользователя
 * userRepository.save(newUser);
 *
 * // Поиск пользователя по email (если добавить метод)
 * Optional<User> user = userRepository.findByEmail(email);
 *
 * // Получение всех пользователей
 * List<User> allUsers = userRepository.findAll();
 * </pre>
 *
 * <p>Может быть расширен дополнительными методами для специфичных запросов.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Примеры возможных дополнительных методов:
    // Optional<User> findByEmail(String email);
    // List<User> findByAgeBetween(int minAge, int maxAge);
    // List<User> findByGoal(GoalType goal);
}
