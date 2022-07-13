
*************************

### Тестовое задание:

Написать набор сервисов для SOA WEB приложения.

Приложение должно реализовывать такие сервисы:

- создание пользователя с (регистрация).

Должен создаваться пользователь с именем и паролем.

Имя должно быть уникальным.

Сразу после создания текущий пользователь должен авторизоваться в томже запросе.

- Не зарегистрированный пользователь должен иметь возможность проверить доступность имени через сервис (валидации).

- созданный в системе пользователь должен иметь возможность авторизоваться, передав в сервис имя и пароль.

Количество неудачных попыток авторизации - не должно превышать 10 за 1 час и сбрасываться при успешной авторизации.

Авторизованный пользователь должен иметь возможность

- создавать /редактировать/удалить животных<br>
 (Вид(из списка-справочника), дата рождения, пол, Кличка(уникальна)).

- получить список своих животных.

- получить детали любого животного по id.

Все взаимодействие должно происходить с использованием JSON формата данных.

Все ошибки должны иметь номера и текстовую расшифровки.

Ошибки, в случае возникновения, так-же должны передаваться в виде JSON объекта.

В качестве базы данных можно взять PostgreSQL, Mongo или любую InMemory базу (но, тогда jar-ик надо добавить в архив).

Рекомендуется использовать Spring и Hibernate (можно c JPA).

Фронт приложение не нужно, только бекэнд часть.

*************************

#### Реализация:

Ниже приведено описание реализации поставленной задачи с соблюдением основных требований.

Проект основан на микро-сервисной архитектуре в виде 2 модулей:<br>
- модуль "аутентификации и авторизации"<br>
- модуль "управление сущностями животных пользователя"<br>
  (При обращении пользователя в модуль "управление сущностями животных пользователя" сперва происходит проверка переданного в headers токена на стороне модуля "аутентификации и авторизации".)<br>

Следует отметить, что данное архитектурное решение для текущего ТЗ избыточно, но обусловлено стремлением сделать проект масштабируемым (и показать технические навыки разработчика).
Для строгого соблюдения ТЗ применима архитектура "монолит".


В проекте используются:<br>

1. Актуальные версии библиотек (Spring Boot 2.4.3)
2. Авторизация - Spring Security
3. Все запросы идемпотентны
4. Сохранение в базу Postgresql (добавлены индексы на ключевых полях для оптимизации запросов)
5. Версионирование изменений баз данных (Flyway)
6. Логирование (архивирование) в локальную директорию (Log4j2)
7. Перехват и обработка http-ошибок и исключений 
8. Стандартизированные ответы
9. Тесты
10. Описание api-методов (SwaggerUI)
11. Коллекции для тестирования в Postman

#### Требования
- JDK 11
- mvn 3.8.2

#### Профили:
- dev - База данных PostgreSQL
- prod - База данных PostgreSQL
- test - для тестирования отдельная база (логирование в отдельную директорию)

#### Для сборки требуется:
  - СУБД Postgresql, 
  - созданы базы animals_db, auth_db (animals_db_test, auth_db_test)
  - пользователь(роль): animal, пароль: animal

Например, можно использовать следующие команды:

        psql postgres

        CREATE USER animal WITH PASSWORD 'animal';

        CREATE DATABASE animals_db TEMPLATE=template0 ENCODING 'UTF-8' LC_COLLATE 'ru_RU.UTF-8' LC_CTYPE 'ru_RU.UTF-8';

        CREATE DATABASE auth_db TEMPLATE=template0 ENCODING 'UTF-8' LC_COLLATE 'ru_RU.UTF-8' LC_CTYPE 'ru_RU.UTF-8';

         \q

В файлах конфигурации модулей application.yaml установить флаг, разрешающий выполнять миграции.<br>
Проверить допустимость для системы прочих настроек.<br>
Выполнить команду в корне проекта: 

    mvn clean package -DDB_USER=animal -DDB_PASSWORD=animal

Запустить созданные в модулях jar-файлы, выполнив команды из корня (например):<br>

        java -jar -DDB_USER=animal -DDB_PASSWORD=animal ./auth/target/auth-0.0.1-SNAPSHOT.jar

        java -jar -DDB_USER=animal -DDB_PASSWORD=animal ./animals/target/animals-0.0.1-SNAPSHOT.jar

#### В корне каждого модуля сохранен скомпилированный jar-файл
[animals-0.0.1-SNAPSHOT.jar](animals/animals-0.0.1-SNAPSHOT.jar)
[auth-0.0.1-SNAPSHOT.jar](auth/auth-0.0.1-SNAPSHOT.jar)

#### Коллекции для тестирования в Postman
[animal-project.postman_collection.json](animal-project.postman_collection.json)


#### Документация API Auth
[Auth Swagger Api Documentation](http://localhost:8020/auth/swagger-ui/)

| Method | Description | URL |
|----------------|---------|----------------|
| POST | Доступность логина | {URL}/auth/api/v1/validate/username |
| POST | Регистрация | {URL}/auth/api/v1/registration |
| POST | Вход в систему | {URL}/auth/api/v1/login |
| POST | Проверка токена | {URL}/auth/api/v1/verify/token |
| POST | Обновление токена | {URL}/auth/api/v1/refresh-token |
| POST | Разлогин | {URL}/auth/api/v1/logout |

#### Документация API Animals
[Animals Swagger Api Documentation](http://localhost:8030/animals/swagger-ui/)

| Method | Description | URL |
|----------------|---------|----------------|
| GET | Все животные пользователя | {URL}/animals/api/v1/username/{username} |
| GET | Животное по id | {URL}/animals/api/v1/{id} |
| POST | Создание животного | {URL}/animals/api/v1/ |
| PATCH | Частичное обновление животного (тип) | {URL}/animals/api/v1/{id}|
| PUT | Полное обновление животного | {URL}/animals/api/v1/{id}|
| DELETE | Удаление животного | {URL}/animals/api/v1/{id}|
