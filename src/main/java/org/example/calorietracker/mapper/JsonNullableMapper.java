package org.example.calorietracker.mapper;

import org.mapstruct.Condition;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.openapitools.jackson.nullable.JsonNullable;

/**
 * Маппер для работы с JsonNullable - оберткой для nullable полей в DTO.
 * Обеспечивает преобразование между JsonNullable и обычными Java-значениями.
 *
 * <p>Используется для:
 * <ul>
 *   <li>Обертывания nullable значений в JsonNullable</li>
 *   <li>Разворачивания JsonNullable в обычные значения</li>
 *   <li>Проверки наличия значения в JsonNullable (для условного маппинга)</li>
 * </ul>
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class JsonNullableMapper {

    /**
     * Обертывает значение в JsonNullable.
     *
     * @param <T> тип значения
     * @param entity значение для обертывания (может быть null)
     * @return JsonNullable с переданным значением
     */
    public <T> JsonNullable<T> wrap(T entity) {
        return JsonNullable.of(entity);
    }

    /**
     * Разворачивает JsonNullable в обычное значение.
     *
     * @param <T> тип значения
     * @param jsonNullable JsonNullable для разворачивания
     * @return значение из JsonNullable или null, если JsonNullable равен null или пуст
     */
    public <T> T unwrap(JsonNullable<T> jsonNullable) {
        return jsonNullable == null ? null : jsonNullable.orElse(null);
    }

    /**
     * Проверяет наличие значения в JsonNullable.
     * Используется MapStruct для условного маппинга.
     *
     * @param <T> тип значения
     * @param nullable JsonNullable для проверки
     * @return true если JsonNullable содержит значение, false в противном случае
     */
    @Condition
    public <T> boolean isPresent(JsonNullable<T> nullable) {
        return nullable != null && nullable.isPresent();
    }
}