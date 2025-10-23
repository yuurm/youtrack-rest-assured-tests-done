# Быстрый старт

## Шаг 1: Настройка токена

1. Откройте YouTrack
2. Profile → Account Security → Authentication
3. Создайте новый Permanent Token
4. Скопируйте токен

## Шаг 2: Конфигурация

Откройте `src/test/resources/config.properties` и обновите:

```properties
base.url=https://ваш-инстанс.youtrack.cloud
auth.token=perm:ВАШ_ТОКЕН_ЗДЕСЬ
test.project.id=ID_ВАШЕГО_ПРОЕКТА
```

## Шаг 3: Установка зависимостей

```bash
mvn clean install
```

## Шаг 4: Запуск тестов

```bash
mvn test
```

## Результаты

После выполнения смотрите:
- `test-output/index.html` - HTML отчет TestNG
- `test-report-*.txt` - текстовый отчет
- Консольный вывод с детальными логами

## Возможные проблемы

### 401 Unauthorized
→ Проверьте токен в config.properties

### 400 Bad Request с project
→ Проверьте test.project.id (должен существовать в YouTrack)

### Connection timeout
→ Проверьте доступность YouTrack instance
