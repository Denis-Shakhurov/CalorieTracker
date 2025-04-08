package org.example.calorietracker.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

/**
 * Сущность блюда, представляющая информацию о пищевой ценности.
 * Содержит данные о калорийности и макронутриентах блюда.
 *
 * <p>Связана с сущностью {@link Meal} отношением Many-to-One.
 *
 * <p>Пример использования:
 * <pre>
 * Dish dish = new Dish();
 * dish.setName("Салат Цезарь");
 * dish.setCalories(350.0);
 * dish.setProteins(15.0);
 * dish.setFats(25.0);
 * dish.setCarbohydrates(10.0);
 * </pre>
 */
@Getter
@Setter
@Entity
@Table(name = "dishes")
public class Dish implements BaseEntity {
    /**
     * Уникальный идентификатор блюда в базе данных.
     * Генерируется автоматически при сохранении.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    /**
     * Название блюда.
     * Не имеет ограничений по длине, но должен быть уникальным в рамках системы.
     */
    private String name;

    /**
     * Калорийность блюда в килокалориях (ккал).
     * Минимальное значение - 0.
     */
    @Min(0)
    private double calories;

    /**
     * Содержание белков в граммах.
     * Минимальное значение - 0.
     */
    @Min(0)
    private double proteins;

    /**
     * Содержание жиров в граммах.
     * Минимальное значение - 0.
     */
    @Min(0)
    private double fats;

    /**
     * Содержание углеводов в граммах.
     * Минимальное значение - 0.
     */
    @Min(0)
    private double carbohydrates;

    /**
     * Прием пищи, к которому относится это блюдо.
     * Связь Many-to-One с сущностью {@link Meal}.
     * Может быть null, если блюдо не привязано к конкретному приему пищи.
     */
    @ManyToOne
    @JoinColumn(name = "meal_id")
    private Meal meal;
}
