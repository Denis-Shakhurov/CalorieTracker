package org.example.calorietracker.repository;

import org.example.calorietracker.model.Meal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MealRepository extends JpaRepository<Meal, Long> {
    List<Meal> findByUserIdAndCreatedAt(Long userId, LocalDate date);

    @Query("SELECT COALESCE(SUM(d.calories), 0) " +
            "FROM Meal m JOIN m.dishes d " +
            "WHERE m.user.id = :userId " +
            "AND DATE(m.createdAt) = :date")
    Double findTotalCaloriesByUserIdAndCreatedAt(@Param("userId") Long userId,
                                                 @Param("date") LocalDate date);
}