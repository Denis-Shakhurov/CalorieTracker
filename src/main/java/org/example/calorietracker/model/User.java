package org.example.calorietracker.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import java.util.List;

/**
 * Сущность пользователя системы. Содержит персональные данные,
 * параметры тела и цели пользователя, а также историю приемов пищи.
 *
 * <p>Пример создания пользователя:
 * <pre>
 * User user = new User();
 * user.setName("Иван Иванов");
 * user.setEmail("ivan@example.com");
 * user.setAge(30);
 * user.setWeight(75.5);
 * user.setHeight(180.0);
 * user.setGender(GenderType.MALE);
 * user.setGoal(GoalType.WEIGHT_LOSS);
 * </pre>
 */
@Getter
@Setter
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class User implements BaseEntity {

    /**
     * Уникальный идентификатор пользователя.
     * Генерируется автоматически при сохранении.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    /**
     * Полное имя пользователя.
     * Не имеет ограничений по длине.
     */
    private String name;

    /**
     * Уникальный email пользователя.
     * Используется для входа в систему.
     * Не может быть null и должен быть уникальным.
     */
    @Column(unique = true, nullable = false)
    private String email;

    /**
     * Возраст пользователя в годах.
     * Должен быть в диапазоне от 1 до 100 лет.
     * Не может быть null.
     */
    @Max(100)
    @Min(1)
    @Column(nullable = false)
    private Integer age;

    /**
     * Вес пользователя в килограммах.
     * Минимальное значение - 5 кг.
     * Может быть null, если не указан.
     */
    @Min(5)
    private Double weight;

    /**
     * Рост пользователя в сантиметрах.
     * Минимальное значение - 30 см.
     * Может быть null, если не указан.
     */
    @Min(30)
    private Double height;

    /**
     * Рекомендуемая суточная норма калорий.
     * Рассчитывается системой на основе параметров пользователя.
     * Может быть null, если расчет не выполнен.
     */
    private Double dailyCalorieIntake;

    /**
     * Пол пользователя.
     * Определяется перечислением {@link GenderType}.
     * Может быть null, если не указан.
     */
    @Enumerated(EnumType.STRING)
    private GenderType gender;

    /**
     * Цель пользователя (похудение/поддержание/набор веса).
     * Определяется перечислением {@link GoalType}.
     * Может быть null, если не указана.
     */
    @Enumerated(EnumType.STRING)
    private GoalType goal;

    /**
     * Список приемов пищи пользователя.
     * Связь One-to-Many с сущностью {@link Meal}.
     * Управляется через поле "user" в сущности Meal.
     * Может быть пустым, если приемы пищи не добавлены.
     */
    @OneToMany(mappedBy = "user")
    private List<Meal> meals;
}
