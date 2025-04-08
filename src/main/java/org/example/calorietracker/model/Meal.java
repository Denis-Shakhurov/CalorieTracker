package org.example.calorietracker.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.List;

/**
 * Сущность, представляющая прием пищи пользователя.
 * Содержит информацию о времени создания и связях с пользователем и блюдами.
 *
 * <p>Пример использования:
 * <pre>
 * Meal meal = new Meal();
 * meal.setUser(currentUser);
 * // добавление блюд через dish.setMeal(meal)
 * </pre>
 */
@Getter
@Setter
@Entity
@Table(name = "meals")
@EntityListeners(AuditingEntityListener.class)
public class Meal implements BaseEntity {
    /**
     * Уникальный идентификатор приема пищи.
     * Генерируется автоматически при сохранении.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    /**
     * Дата и время создания записи о приеме пищи.
     * Заполняется автоматически при создании сущности.
     * Формат: YYYY-MM-DD
     */
    @CreatedDate
    private LocalDate createdAt;

    /**
     * Пользователь, которому принадлежит прием пищи.
     * Связь Many-to-One с сущностью {@link User}.
     * Обязательное поле (не может быть null).
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * Список блюд, входящих в данный прием пищи.
     * Связь One-to-Many с сущностью {@link Dish}.
     * Управляется через поле "meal" в сущности Dish.
     * Может быть пустым (прием пищи без блюд).
     */
    @OneToMany(mappedBy = "meal")
    private List<Dish> dishes;
}
