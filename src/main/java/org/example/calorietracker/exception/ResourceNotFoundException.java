package org.example.calorietracker.exception;

/**
 * Исключение, выбрасываемое при попытке доступа к несуществующему ресурсу.
 * Используется для обработки случаев, когда запрашиваемые данные не найдены в системе.
 *
 * <p>Примеры использования:
 * <pre>
 * throw new ResourceNotFoundException("Пользователь с ID 123 не найден");
 * throw new ResourceNotFoundException("Блюдо не найдено");
 * </pre>
 */
public class ResourceNotFoundException extends RuntimeException {
    /**
     * Создает новое исключение с указанным сообщением об ошибке.
     *
     * @param message детализированное сообщение об ошибке,
     *                которое должно содержать информацию о том,
     *                какой именно ресурс не был найден
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
