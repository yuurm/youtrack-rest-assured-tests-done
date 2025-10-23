# YouTrack REST API Test Automation Project

Проект автоматизированного тестирования YouTrack REST API с использованием RestAssured и TestNG.

## 🎯 Описание

Данный проект содержит комплексный набор автоматизированных тестов для проверки функциональности YouTrack через REST API:
- **6 позитивных тест-кейсов** - проверка основных сценариев работы API
- **6 негативных тест-кейсов** - проверка обработки ошибочных ситуаций
- **Data-Driven тесты** - тестирование с использованием CSV и Excel файлов
- **Параллельное выполнение** - ускорение прогона тестов
- **Детальная отчетность** - подробные отчеты о результатах

## 🛠 Технологический стек

- **Java**: 11+
- **Build Tool**: Maven
- **Testing Framework**: TestNG 7.8.0
- **API Testing**: RestAssured 5.3.2
- **JSON Processing**: Jackson 2.15.3
- **Data Providers**: OpenCSV 5.8, Apache POI 5.2.5

## 📋 Предварительные требования

1. Java JDK 11 или выше
2. Maven 3.6+
3. IntelliJ IDEA или Eclipse
4. Доступ к YouTrack instance

## 🚀 Установка и настройка

### 1. Клонирование проекта

```bash
cd youtrack-rest-assured-tests-done
```

### 2. Обновление зависимостей

```bash
mvn clean install
```

### 3. Настройка конфигурации

Отредактируйте `src/test/resources/config.properties`:

```properties
base.url=https://your-instance.youtrack.cloud
api.base.path=/api
auth.token=perm:YOUR_PERMANENT_TOKEN_HERE
test.project.id=0-0
```

### 4. Получение Permanent Token

1. Войдите в YouTrack
2. Перейдите: Profile → Account Security → Authentication
3. Нажмите "New token..."
4. Выберите scopes: **YouTrack** и **YouTrack Administration**
5. Скопируйте токен и вставьте в config.properties

## 📝 Структура проекта

```
youtrack-rest-assured-tests/
├── src/
│   ├── main/java/com/youtrack/api/
│   │   ├── pojo/              # DTO классы
│   │   │   ├── Issue.java
│   │   │   ├── Project.java
│   │   │   └── CustomField.java
│   │   └── utils/             # Утилиты
│   │       ├── ConfigReader.java
│   │       ├── CSVDataProvider.java
│   │       ├── ExcelDataProvider.java
│   │       └── TestReportListener.java
│   └── test/
│       ├── java/com/youtrack/api/tests/
│       │   ├── BaseTest.java
│       │   ├── PositiveTests.java    # 6 позитивных тестов
│       │   ├── NegativeTests.java    # 6 негативных тестов
│       │   └── DataDrivenTests.java  # Data-driven тесты
│       └── resources/
│           ├── config.properties
│           ├── test_data.csv
│           └── test_data.xlsx
├── pom.xml
├── testng.xml
└── README.md
```

## 🧪 Тест-кейсы

### Позитивные тесты (PositiveTests.java)

1. **TC_POS_001**: Создание нового issue с обязательными полями
2. **TC_POS_002**: Получение списка всех проектов
3. **TC_POS_003**: Получение информации о конкретном issue
4. **TC_POS_004**: Обновление summary существующего issue
5. **TC_POS_005**: Получение информации о текущем пользователе
6. **TC_POS_006**: Создание issue с custom fields

### Негативные тесты (NegativeTests.java)

1. **TC_NEG_001**: Создание issue без обязательного поля project
2. **TC_NEG_002**: Получение несуществующего issue
3. **TC_NEG_003**: Создание issue с невалидным project ID
4. **TC_NEG_004**: Запрос без токена авторизации
5. **TC_NEG_005**: Обновление issue с пустым summary
6. **TC_NEG_006**: Создание issue с невалидным типом custom field

### Data-Driven тесты (DataDrivenTests.java)

- Создание issues из CSV файла
- Создание issues из Excel файла
- Тестирование различных значений Priority

## ▶️ Запуск тестов

### Через Maven

```bash
# Запуск всех тестов
mvn clean test

# Запуск с конкретным suite
mvn test -DsuiteXmlFile=testng.xml

# Параллельное выполнение (5 потоков)
mvn test -Dparallel=methods -DthreadCount=5

# Запуск конкретного класса
mvn test -Dtest=PositiveTests

# Запуск конкретного метода
mvn test -Dtest=PositiveTests#testCreateIssueWithRequiredFields
```

### Через IntelliJ IDEA

1. Правый клик на `testng.xml` → Run
2. Правый клик на тестовом классе → Run 'ClassName'
3. Правый клик на методе → Run 'methodName'

### Через Eclipse

1. Правый клик на `testng.xml` → Run As → TestNG Suite
2. Правый клик на классе → Run As → TestNG Test

## 📊 Отчетность

После выполнения тестов генерируются отчеты:

1. **TestNG Reports**: `test-output/index.html`
2. **Maven Surefire Reports**: `target/surefire-reports/`
3. **Custom Text Report**: `test-report-[timestamp].txt`

### Пример отчета

```
================================================================================
YouTrack REST API Test Execution Report
Test Suite: YouTrack REST API Test Suite
Start Time: 2025-10-22T15:30:00
================================================================================

Test: testCreateIssueWithRequiredFields
Status: PASSED
Duration: 1234ms

================================================================================
Test Execution Summary
================================================================================
Total Tests: 12
Passed: 11
Failed: 1
Skipped: 0
Success Rate: 91.67%
================================================================================
```

## 🔧 Troubleshooting

### Проблема: 401 Unauthorized

**Решение**: 
- Проверьте правильность permanent token в config.properties
- Убедитесь что token имеет необходимые scopes

### Проблема: Connection Timeout

**Решение**:
- Увеличьте timeout.seconds в конфигурации
- Проверьте доступность YouTrack instance

### Проблема: 400 Bad Request

**Решение**:
- Проверьте правильность test.project.id
- Убедитесь что проект существует в YouTrack

## 🎓 Best Practices

Реализованные в проекте best practices:

✓ BDD-стиль RestAssured (given/when/then)  
✓ Request Specifications для переиспользования  
✓ POJO/DTO паттерны для типобезопасности  
✓ Data-Driven Testing с внешними данными  
✓ Параллельное выполнение тестов  
✓ Детальное логирование запросов/ответов  
✓ Независимость тестов  
✓ Централизованная конфигурация

## 📚 Дополнительные материалы

- [YouTrack REST API Documentation](https://www.jetbrains.com/help/youtrack/devportal/api-url-and-endpoints.html)
- [RestAssured Documentation](https://rest-assured.io/)
- [TestNG Documentation](https://testng.org/doc/)

## 👨‍💻 Автор

Проект создан для демонстрации автоматизированного тестирования REST API.

## 📄 Лицензия

Данный проект предназначен для образовательных целей.
