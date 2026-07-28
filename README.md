# Student Management System

REST API для управления студентами и учебными курсами.

Проект разработан в рамках прохождения учебной практики в Университете «Синергия».

---

## Возможности

### Управление студентами

- создание студента;
- получение студента по идентификатору;
- получение списка студентов;
- обновление информации о студенте;
- удаление студента;
- поиск по фамилии;
- поиск по учебной группе;
- поиск по номеру курса обучения.

### Управление курсами

- создание курса;
- получение курса по идентификатору;
- получение списка курсов;
- обновление курса;
- удаление курса.

---

## Используемые технологии

- Java 21
- Spring Boot 4
- Spring Web
- Spring Data JPA
- Jakarta Validation
- H2 Database
- SpringDoc OpenAPI (Swagger)
- Lombok
- JUnit 5
- Mockito
- MockMvc
- Maven

---

## Архитектура проекта

Проект реализован с использованием многослойной архитектуры.

```
Controller
      │
      ▼
Service
      │
      ▼
Repository
      │
      ▼
Database (H2)
```

Структура проекта:

```
src/main/java
├── config
├── controller
│   ├── student
│   └── course
├── dto
├── entity
├── exception
├── mapper
├── repository
│   ├── student
│   └── course
├── service
│   ├── student
│   └── course
└── StudentManagementSystemApplication
```

---

## Модель данных

### Student

| Поле | Описание |
|------|----------|
| id | идентификатор |
| firstName | имя |
| lastName | фамилия |
| email | электронная почта |
| groupName | учебная группа |
| courseNumber | номер курса обучения |
| course | учебный курс |

### Course

| Поле | Описание |
|------|----------|
| id | идентификатор |
| name | название курса |
| description | описание |
| durationHours | продолжительность в часах |

Связь между сущностями:

```
Course (1)
      ▲
      │
Student (N)
```

Один курс может быть назначен нескольким студентам.

---

## REST API

### Students

| Метод | Endpoint |
|--------|----------|
| POST | `/api/v1/students` |
| GET | `/api/v1/students/{id}` |
| GET | `/api/v1/students` |
| PUT | `/api/v1/students/{id}` |
| DELETE | `/api/v1/students/{id}` |
| GET | `/api/v1/students/search/by-last-name` |
| GET | `/api/v1/students/search/by-group` |
| GET | `/api/v1/students/search/by-course-number` |

### Courses

| Метод | Endpoint |
|--------|----------|
| POST | `/api/v1/courses` |
| GET | `/api/v1/courses/{id}` |
| GET | `/api/v1/courses` |
| PUT | `/api/v1/courses/{id}` |
| DELETE | `/api/v1/courses/{id}` |

---

## Документация API

После запуска приложения Swagger доступен по адресу:

```
http://localhost:8080/swagger-ui/index.html
```

---

## Запуск проекта

### Клонирование

```bash
git clone https://github.com/USERNAME/student-management-system.git
```

### Сборка

```bash
mvn clean install
```

### Запуск

```bash
mvn spring-boot:run
```

или

```bash
java -jar target/student-management-system.jar
```

---

## База данных

Используется встроенная база данных H2.

Консоль:

```
http://localhost:8080/h2-console
```

Параметры подключения:

```
JDBC URL:
jdbc:h2:mem:testdb

User:
sa

Password:
(пустой)
```

---

## Тестирование

Запуск всех тестов:

```bash
mvn test
```

Используются:

- JUnit 5;
- Mockito;
- MockMvc.

---

## Реализованные возможности

- CRUD для студентов;
- CRUD для курсов;
- связь Student → Course;
- валидация входящих данных;
- централизованная обработка ошибок;
- Swagger/OpenAPI;
- unit-тесты;
- controller-тесты.

---

