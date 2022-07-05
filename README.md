
*************************

### Тестовое задание:

**Написать набор сервисов для SOA WEB-приложения.**.<br>
приложение должно реализовывать такие сервисы:

**1. Сервис авторизации:**<br>
***1.1. Сервис "Валидация":***

- Не зарегистрированный пользователь должен иметь возможность проверить доступность имени через сервис (валидации).

***1.2. Сервис "Регистрация":***<br>
создание пользователя (регистрация).
- должен создаваться пользователь с именем и паролем.
- имя должно быть уникальным.
- сразу после создания текущий пользователь должен авторизоваться в том же запросе.

***1.3. Сервис "Аутентификация":***

- созданный в системе пользователь должен иметь возможность авторизоваться, передав в сервис имя и пароль.
- Количество неудачных попыток авторизации - не должно превышать 10 за 1 час
- и сбрасываться при успешной авторизации.

***1.4. Сервис "Авторизация":***

- авторизованный пользователь должен иметь возможность создавать /редактировать/удалить животных

**2. Сервис "Animals":**

- создавать /редактировать/удалить животных<br>

    - Вид(из списка-справочника @Immutable),
    - дата рождения,
    - пол,
    - Кличка(уникальна)

- получить список своих животных (id, name?).
- получить детали любого животного по id.

**Также учесть:** 

- Фронт приложение не нужно, только бекэнд часть.
- Рекомендуется использовать Spring и Hibernate (можно c JPA).
- Все взаимодействие должно происходить с использованием JSON форамата данных.

- Все ошибки должны иметь номера и текстовую расшифровки.
- Ошибки, в случае возникновения, также должны передаваться в виде JSON объекта.

- В качестве базы данных можно взять PostgreSQL, Mongo или любую InMemory базу.
    - (но, тогда jar-ик надо добавить в архив)
    - Flyway/Liquibase?

*************************

Ниже приведено описание реализации поставленной задачи с соблюдением основных требований.
Работает однопоточно?

Доступные шаги использования:
1. 

Все запросы идемпотентны.

#### Реализация:
В кодовой базе проекта используются актуальные версии библиотек (Spring Boot 2.4.3).<br>
Основа проекта - Spring Boot.

1. Сохранение в базу (PostgreSQL)
2. Сохранение изменений состояний бизнес-сущностей в базу (Postgres) (добавлены индексы на ключевых полях для оптимизации запросов)
3. Версионирование изменений базы данных (Flyway)
4. Логирование (архивирование) в локальную директорию (Log4j2)
5. Обработка http-ошибок
6. Тесты
7. Описание api-методов (SwaggerUI)
8. UML-диаграмма?

#### Использованы паттерны проектированя (яркие примеры в сервисах-обработчиках запросов и переходов состояний):
1. Singleton
2. Strategy
3. Command
4. Register
5. Chain of responsibility

#### Требования
- JDK 11
- mvn 3.8.2

#### Профили:

- dev - По умолчанию, база данных H2 (In memory)
- prod - База данных PostgreSQL
- для тестирования - отдельная база (и логирование в отдельную директорию)/testcontainers

#### Для сборки требуется:
Выполнять в системе, где
  - (установлен docker???)
  - развернута СУБД Postgresql, 
  - созданы базы animals_db и auth_db 
  - пользователь(роль): animal, пароль: animal

Например, используя следующие команды

        psql postgres
        CREATE USER animal WITH PASSWORD 'animal';
        CREATE DATABASE animals_db TEMPLATE=template0 ENCODING 'UTF-8' LC_COLLATE 'ru_RU.UTF-8' LC_CTYPE 'ru_RU.UTF-8';
        CREATE DATABASE auth_db TEMPLATE=template0 ENCODING 'UTF-8' LC_COLLATE 'ru_RU.UTF-8' LC_CTYPE 'ru_RU.UTF-8';
         \q

В файлах конфигурации модулей установить флаг, разрешающий выполнять миграции.
Проверить допустимость для системы прочих настроек).
Выполнить команду в корне проекта: mvn clean package -DDB_USER=animal -DDB_PASSWORD=animal
Запустить созданные в модулях jar-файлы, выполнив команды из корня (например):

        java -jar -DDB_USER=animal -DDB_PASSWORD=animal ./auth/target/auth-0.0.1-SNAPSHOT.jar
        java -jar -DDB_USER=animal -DDB_PASSWORD=animal ./animals/target/animals-0.0.1-SNAPSHOT.jar


#### Документация API Auth
[Auth Swagger Api Documentation](http://localhost:8020/auth/swagger-ui/)

| Method | Description | URL |
|----------------|---------|----------------|
| GET | Залогиниться | {URL}/auth/api/v1/sign-in |
...

#### Документация API Animals
[Animals Swagger Api Documentation](http://localhost:8030/animals/swagger-ui/)

| Method | Description | URL |
|----------------|---------|----------------|
| GET | Получить | {URL}/animals/api/v1/{id} |
| POST | Создать | {URL}/animals/api/v1/ |
...
