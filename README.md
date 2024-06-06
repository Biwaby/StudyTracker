# Веб-приложение для отслеживания деятельности
Проект представляет собой task-tracker сервис, который позволяет отслеживать затрачиваемое время при выполнении задач или проектов. Пользователь, используя таймер, создаёт записи рабочего сеанса. К записи можно прикрепить проект и его задачи, тем самым конкретизируя запись. Также к записи можно добавлять теги, что поможет упорядочить большое кол-во записей. Реализовано получение сводки по записям, что поможет отследить продуктивность пользователя по дням или по проектам.

`(!) На данный момент готова backend составляющая приложения с REST-API интерфейсом. В скором времени будет реализован frontend.`

## **Стек используемых технологий:**
- Java 21
- Spring Boot 3.2.5
- Spring Web
- Spring Security
- JPA
- БД: PostgreSQL

## **API методы:**
### Неавторизованные пользователи
```
POST /users/register - регистрация нового пользователя
```
Данный запрос принимает следующий JSON:
```json
{
  "username": "{username}",
  "password": "{password}"
}
```
### Только зарегистрированные пользователи (только `USER` и `ADMIN`)
- Записи таймера
```
GET /timer?page={page_num} - возвращает все записи таймера пользователя (с пагинацией)
GET /timer/{recordId} - получение записи таймера пользователя по id
POST /timer/record - создать запись таймера
DELETE /timer/delete?recordId={id} - удалить запись таймера
PUT /timer/edit?recordId={id} - изменить запись
PUT /timer/addProject?recordId={id}&projectId={id} - добавить проект к записи
PUT /timer/removeProject?recordId={id} - убрать проект у записи
PUT /timer/addTask?recordId={id}&taskId={id} - добавить задачу к записи
PUT /timer/removeTask?recordId={id} - убрать задачу у записи
PUT /timer/addTag?recordId={id}&tagId={id} - добавить тег к записи
PUT /timer/removeTag?recordId={id}&tagId={id} - убрать тег у записи
```
Методы создания и изменения записи принимают следующий JSON:
```json
{
  "title": "{title}",
  "startTime": "{startTime} в формате HH:mm:ss",
  "endTime": "{endTime} в формате HH:mm:ss",
  "recordDate": "{recordDate} в формате dd-MM-yyyy"
}
```
- Получение сводки
```
GET /summary/today - получение сводки на сегодняшний день
GET /summary/date?date={day}&month={month}&year={year} - получение сводки на определённый день
GET /summary/project?projectId={id} - получение сводки по проекту
```
- Проекты
```
GET /projects - возвращает список всех проектов пользователя
GET /projects/{projectId} - получение проекта пользователя по id
POST /projects/create - создание нового проекта
DELETE /projects/delete?projectId={id} - удаление проекта
PUT /projects/edit?projectId={id} - изменение проекта
```
Методы создания и изменения проекта принимают следующий JSON:
```json
{
  "title": "{title}",
  "description": "{description}"
}
```
- Задачи
```
GET /projects/tasks?projectId={id} - возвращает список задач проекта пользователя
GET /projects/tasks/{projectId}/{taskId} - получение задачи по id из проекта
POST /projects/tasks/add?projectId={id} - добавление задачи в проект
DELETE /projects/tasks/delete?projectId={id}&taskId={id} - удаление задачи из проекта
PUT /projects/task/edit?projectId={id}&taskId={id} - изменеие задачи в проекте
```
Метод создания задачи принимает следующий JSON:
```json
{
  "title": "{title}",
  "description": "{description}"
}
```
Метод изменения задачи принимает JSON:
```json
{
  "title": "{title}",
  "description": "{description}",
  "completed": true/false
}
```
- Теги
```
GET /tags - возвращает список тегов пользователя
GET /tags/{tagId} - получение тега по id
POST /tags/add - создание нового тега
DELETE /tags/delete?tadId={id} - удаление тега
PUT /tags/edit?tagId={id} - изменение тега
```
Методы создания и изменения тега принимают следующий JSON:
```json
{
  "title": "{title}"
}
```
- Удаление пользователя
```
DELETE /users/delete - удаляет пользователя, который вызвал этот метод, и все его данные
```
### Только администраторы (только `ADMIN`)
- Записи таймера
```
GET /admin/timer?username={username}&page={page_num} - получение записей таймера конкретного пользователя (с пагинацией)
```
- Управление пользователями
```
GET /admin/users?page={num_page} - возвращает список пользователей (с пагинацией)
PUT /admin/user/grant?userId={id}&roleId={id} - выдать роль пользователю
PUT /admin/user/revoke?userId={id}&roleId={id} - отнять роль у пользователя
DELETE /admin/users/delete?userId={id} - удалить пользоваетеля по id
```
- Управление ролями
```
GET /admin/users/roles - возвращает список ролей
POST /admin/users/roles/add - создание новой роли
DELETE /admin/users/roles/delete?roleId={id} - удалить роль
```
Метод создания роли принимает следующий JSON:
```json
{
  "authority": "{authority}"
}
```
---
2024, Biwaby (Kamanin Andrey)
