package org.example.calorietracker.repository;

import org.example.calorietracker.model.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Репозиторий для работы с сущностями {@link Dish}.
 * Обеспечивает базовые CRUD-операции и доступ к данным о блюдах.
 *
 * <p>Наследует стандартные методы работы с JPA:
 * <ul>
 *   <li>{@code save()} - сохранение/обновление блюда</li>
 *   <li>{@code findById()} - поиск по идентификатору</li>
 *   <li>{@code findAll()} - получение всех блюд</li>
 *   <li>{@code deleteById()} - удаление блюда</li>
 *   <li>и другие стандартные методы</li>
 * </ul>
 *
 * <p>Примеры использования:
 * <pre>
 * // Сохранение нового блюда
 * dishRepository.save(newDish);
 *
 * // Поиск блюда по ID
 * Optional<Dish> dish = dishRepository.findById(1L);
 *
 * // Получение всех блюд
 * List<Dish> allDishes = dishRepository.findAll();
 * </pre>
 */
@Repository
public interface DishRepository extends JpaRepository<Dish, Long> {
    // Можно добавить кастомные методы запросов:
    // List<Dish> findByMealId(Long mealId);
    // List<Dish> findByNameContainingIgnoreCase(String name);
}
