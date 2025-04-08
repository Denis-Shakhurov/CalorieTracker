package org.example.calorietracker.repository;

import org.example.calorietracker.model.Meal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Репозиторий для работы с сущностями {@link Meal}.
 * Предоставляет стандартные CRUD-операции и специализированные методы
 * для работы с приемами пищи пользователей.
 *
 * <p>Наследует все стандартные методы {@link JpaRepository}:
 * <ul>
 *   <li>{@code save()}, {@code findAll()}, {@code deleteById()} и др.</li>
 * </ul>
 */
@Repository
public interface MealRepository extends JpaRepository<Meal, Long> {

    /**
     * Находит все приемы пищи пользователя за указанную дату.
     *
     * @param userId идентификатор пользователя
     * @param date дата для поиска (без времени)
     * @return список приемов пищи или пустой список, если ничего не найдено
     */
    List<Meal> findByUserIdAndCreatedAt(Long userId, LocalDate date);

    /**
     * Вычисляет суммарную калорийность всех блюд пользователя за указанную дату.
     *
     * <p>Использует JPQL-запрос для соединения таблиц и агрегации данных.
     *
     * @param userId идентификатор пользователя
     * @param date дата для расчета (без времени)
     * @return сумма калорий (0 если нет данных)
     *
     * <p>Пример запроса:
     * <pre>
     * SELECT SUM(d.calories)
     * FROM Meal m JOIN m.dishes d
     * WHERE m.user.id = 123
     * AND DATE(m.createdAt) = '2023-01-01'
     * </pre>
     */
    @Query("SELECT COALESCE(SUM(d.calories), 0) " +
            "FROM Meal m JOIN m.dishes d " +
            "WHERE m.user.id = :userId " +
            "AND DATE(m.createdAt) = :date")
    Double findTotalCaloriesByUserIdAndCreatedAt(@Param("userId") Long userId,
                                                 @Param("date") LocalDate date);
}
