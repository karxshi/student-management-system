# Student Management System

## Описание

Проект представляет собой REST API для управления информацией о студентах университета.

Основные возможности:

- создание студента;
- получение списка студентов;
- получение информации о студенте;
- редактирование данных студента;
- удаление студента.

## Стек технологий

- Java 21
- Spring Boot 3.5
- Spring Data JPA
- H2 Database
- Maven
- Lombok
- Swagger/OpenAPI

## Архитектура

Проект реализован по многослойной архитектуре:

- Controller
- Service
- Repository
- Entity

## ER-модель

Student
- id
- firstName
- lastName
- email
- groupName
- course

## REST API

GET /students

GET /students/{id}

POST /students

PUT /students/{id}

DELETE /students/{id}
