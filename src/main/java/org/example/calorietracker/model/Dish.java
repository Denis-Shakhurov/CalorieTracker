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

@Getter
@Setter
@Entity
@Table(name = "dishes")
public class Dish implements BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String name;

    @Min(0)
    private double calories;

    @Min(0)
    private double proteins;

    @Min(0)
    private double fats;

    @Min(0)
    private double carbohydrates;

    @ManyToOne
    @JoinColumn(name = "meal_id")
    private Meal meal;
}