# Домашнее задание: Patterns (Перепланирование встречи)

[![CI](https://github.com/dimitrieva-vika/aqa-homeworks-patterns/actions/workflows/gradle.yml/badge.svg)](https://github.com/dimitrieva-vika/aqa-homeworks-patterns/actions/workflows/gradle.yml)

## Описание проекта

Автотесты для формы перепланирования встречи с использованием Selenide, Faker, Lombok и Allure.

## Статус тестирования

| Тест | Статус | Описание |
|------|--------|----------|
| shouldHaveCorrectDefaultValues | ✅ PASSED | Проверка значений по умолчанию |
| shouldSuccessfulPlanMeeting | ✅ PASSED | Успешное планирование встречи |
| shouldSuccessfulReplanMeeting | ✅ PASSED | Перепланирование с теми же данными |
| shouldReplanMeetingWithDifferentUserData | ✅ PASSED | Перепланирование с другими данными |
| shouldShowErrorWhenCityIsEmpty | ✅ PASSED | Пустой город |
| shouldShowErrorWhenCityIsInvalid | ✅ PASSED | Некорректный город |
| shouldShowErrorWhenDateIsEmpty | ✅ PASSED | Пустая дата |
| shouldShowErrorWhenNameIsEmpty | ✅ PASSED | Пустое имя |
| shouldShowErrorWhenNameContainsDigits | ✅ PASSED | Имя с цифрами |
| shouldShowErrorWhenPhoneIsEmpty | ✅ PASSED | Пустой телефон |
| shouldShowErrorWhenAgreementNotChecked | ✅ PASSED | Неотмеченный чекбокс |
| shouldShowErrorOnlyForCityWhenAllFieldsEmpty | ✅ PASSED | Все поля пустые |

**Итог: ✅ Все 12 тестов успешно пройдены.**

## Технологии

- Java 11
- JUnit 5
- Selenide 6.19.1
- Faker 1.0.2
- Lombok 1.18.36
- Allure 2.24.0
- Gradle 8.14.5
- GitHub Actions (CI)

## Запуск тестов

### Локальный запуск

1. Запустите SUT:
   ```bash
   java -jar ./artifacts/app-replan-delivery.jar
   ```

2. Запустите тесты:
   ```bash
   ./gradlew clean test
   ```

3. Сгенерируйте Allure отчет:
   ```bash
   ./gradlew allureReport
   ```

4. Откройте Allure отчет:
   ```bash
   ./gradlew allureServe
   ```



## Скриншоты Allure отчета

### Общий вид

![Allure Overview](screenshots/allure-overview.png)

## Сайты

![Allure Test Details](screenshots/allure-suites.png)

### Графики и статистика

![Allure Graphs](screenshots/allure-graphs.png)