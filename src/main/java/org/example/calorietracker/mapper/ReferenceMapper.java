package org.example.calorietracker.mapper;

import jakarta.persistence.EntityManager;
import org.example.calorietracker.model.BaseEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.TargetType;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Маппер для преобразования идентификаторов в JPA сущности.
 * Используется MapStruct для автоматического преобразования ID в связанные сущности.
 *
 * <p>Основное назначение:
 * <ul>
 *   <li>Преобразование первичных ключей (ID) в соответствующие JPA сущности</li>
 *   <li>Использование EntityManager для поиска сущностей</li>
 *   <li>Поддержка наследования через BaseEntity</li>
 * </ul>
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class ReferenceMapper {

    @Autowired
    private EntityManager entityManager;

    /**
     * Преобразует идентификатор в JPA сущность.
     *
     * @param <T> тип сущности, должен наследоваться от BaseEntity
     * @param id идентификатор сущности (может быть null)
     * @param entityClass класс целевой сущности
     * @return найденная сущность или null, если id равен null
     */
    public <T extends BaseEntity> T toEntity(Long id, @TargetType Class<T> entityClass) {
        return id != null ? entityManager.find(entityClass, id) : null;
    }
}
