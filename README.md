## ExploreWithMe - сервис, который помогает находить интересные мероприятия и создавать собственные. 

- Основная идея проекта - сделать свободное время более интересным и продуктивным. 
- Приложение содержит два сервиса:
  * Основной сервис - предназначен для работы с событиями и их участниками. 
  * Сервис статистики - хранит информацию об обращениях к основному сервису. Позволяет формировать статистику для основного сервиса. 

## Дополнительная функциональность

Комментарии к событиям
- позволяет оставлять и модерировать комментарии к событиям,
- позволяет ставить лайки и дизлайки событию,
- позволяет получать количество комментариев у событий для понимания их популярности.
  
- https://github.com/Iregor/java-explore-with-me/pull/9

## Технологический стек

Проект ExploreWithMe разработан с использованием следующих технологий и инструментов:

- Java 11
- Spring Boot
- Hibernate ORM, Query DSL - маппинг данных
- Apache Maven - управление зависимостями и сборка приложения. 
- PostgresSQL, H2 - реляционные базы данных
- Docker, Docker Compose - контейнеризация сервисов приложения.

## Микросервисная архитектура

Приложение состоит из 2 основных модулей:
- Stats - сервер хранения обращений к приложению и формирования статистики
- Service - сервер обработки запросов к приложению

## Установка и запуск проекта

### 1 вариант:
Необходимо настроенная система виртуализации, установленный Docker Desktop(скачать и установить можно с официального сайта https://www.docker.com/products/docker-desktop/)

1. Клонируйте репозиторий проекта на свою локальную машину.
2. Запустите командную строку и перейдите в корень директории с проектом.
3. Введите следующую команду, которая подготовит и запустит приложение на вашей локальной машине
   ```
   $  docker-compose up
   ```
4. Приложение будет запущено на порту 8080 и готово принимать http-запросы. 
5. Список доступных эндпоинтов предоставлен ниже


### 2 вариант:

1. Установите Java Development Kit (JDK) версии 11 или выше, если у вас его еще нет.
2. Установите PostgreSQL и создайте базу данных для проекта.
3. Клонируйте репозиторий проекта на свою локальную машину.
4. Настройте файл `application.properties`, расположенный в директории `src/main/resources`, чтобы указать данные для подключения к вашей базе данных PostgreSQL.
5. Запустите приложение, выполнив следующую команду в корневой директории проекта:
   ```
   mvn spring-boot:run
   ```
6. Приложение будет запущено на порту 8080 и готово принимать http-запросы. 

Эндпоинты
---
- /compilations - Получение подборок событий
- /compilations/{compId} - Получение подборки событий по его id
---
- /admin/categories Добавление новой категории
- /admin/categories/{catId} Удаление категории
- /admin/categories/{catId} Получение списка бронирований для всех вещей текущего пользователя.
---
- /users/{userId}/events - Получение событий, добавленных текущим пользователем
- /users/{userId}/events - Добавление нового события
- /users/{userId}/events/{eventId} - Получение полной информации о событии добавленном текущим пользователем
- /users/{userId}/events/{eventId} - Изменение события добавленного текущим пользователем
- /users/{userId}/events/{eventId}/requests - Получение информации о запросах на участие в событии текущего пользователя
- /users/{userId}/events/{eventId}/requests - Изменение статуса (подтверждена, отменена) заявок на участие в событии текущего пользователя
---
- /categories - Получение категорий
- /categories/{catId} - Получение информации о категории по её идентификатору
---
- /admin/events - Поиск событий
- /admin/events/{eventId} - Редактирование данных события и его статуса (отклонение/публикация).
--- 
- /events/{id} - Получение подробной информации об опубликованном событии по его идентификатору
- /events/{id} - Получение подробной информации об опубликованном событии по его идентификатору
---
- /users/{userId}/requests - Получение информации о заявках текущего пользователя на участие в чужих событиях
- /users/{userId}/requests - Добавление запроса от текущего пользователя на участие в событии
- /users/{userId}/requests/{requestId}/cancel - Отмена своего запроса на участие в событии
---
- /admin/users - Получение информации о пользователях
- /admin/users - Добавление нового пользователя
- /admin/users/{userId} - Удаление пользователя
---
- /admin/compilations - Добавление новой подборки (подборка может не содержать событий)
- /admin/compilations/{compId} - Удаление подборки
- /admin/compilations/{compId} - Обновить информацию о подборке
---
- /stats - Получение статистики по посещениям
- /hit - Сохранение информации о том, что на uri конкретного сервиса был отправлен запрос пользователем.
---
