# Student Management System

REST API для управления студентами, учебными курсами и записями студентов на курсы.

Проект разработан в рамках прохождения учебной практики в Университете «Синергия».

---

## Возможности

### Управление студентами

- создание студента;
- получение студента по идентификатору;
- получение списка студентов с пагинацией;
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

### Управление записями на курсы

- запись студента на курс;
- получение списка курсов студента;
- получение списка студентов курса;
- выставление итоговой оценки;
- удаление записи на курс.

---

## Используемые технологии

- Java 21
- Spring Boot 4
- Spring Web
- Spring Data JPA
- Jakarta Validation
- SpringDoc OpenAPI (Swagger)
- H2 Database
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
│   ├── course
│   └── enrollment
├── dto
├── entity
├── exception
├── mapper
├── repository
│   ├── student
│   ├── course
│   └── enrollment
├── service
│   ├── student
│   ├── course
│   └── enrollment
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

### Course

| Поле | Описание |
|------|----------|
| id | идентификатор |
| name | название курса |
| description | описание |
| durationHours | продолжительность курса (часов) |

### Enrollment

| Поле | Описание |
|------|----------|
| id | идентификатор записи |
| student | студент |
| course | учебный курс |
| enrollmentDate | дата записи |
| status | статус прохождения курса |
| grade | итоговая оценка |

Связь между сущностями:

```
Student (1)
     │
     │
     ▼
Enrollment
     ▲
     │
     │
Course (1)
```

Один студент может быть записан на несколько курсов, а один курс может содержать множество студентов.

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
| GET | `/api/v1/students/search/by-course` |

### Courses

| Метод | Endpoint |
|--------|----------|
| POST | `/api/v1/courses` |
| GET | `/api/v1/courses/{id}` |
| GET | `/api/v1/courses` |
| PUT | `/api/v1/courses/{id}` |
| DELETE | `/api/v1/courses/{id}` |

### Enrollments

| Метод | Endpoint |
|--------|----------|
| POST | `/api/v1/enrollments` |
| GET | `/api/v1/enrollments/student/{studentId}` |
| GET | `/api/v1/enrollments/course/{courseId}` |
| PATCH | `/api/v1/enrollments/{id}/grade` |
| DELETE | `/api/v1/enrollments/{id}` |

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
- CRUD для учебных курсов;
- запись студентов на курсы;
- выставление итоговой оценки;
- поиск студентов по различным параметрам;
- пагинация результатов;
- валидация входящих данных;
- централизованная обработка исключений;
- документирование REST API с помощью Swagger/OpenAPI;
- unit-тесты сервисного слоя;
- тесты контроллеров с использованием MockMvc.