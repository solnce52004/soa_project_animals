package ru.example.animals.config.container;

import org.flywaydb.core.Flyway;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@ActiveProfiles("test")
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@DirtiesContext//(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ContainersEnvironment {

    @Container
    public static PostgresqlTestContainer postgreSQLContainer = PostgresqlTestContainer.getInstance();
    public static Flyway flywayContainer;

    @DynamicPropertySource
    static void registerDynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.hikari.jdbc-url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.hikari.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.hikari.password", postgreSQLContainer::getPassword);
        registry.add("spring.datasource.hikari.driver-class-name", postgreSQLContainer::getHikariDriver);

        registry.add("spring.flyway.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.flyway.user", postgreSQLContainer::getUsername);
        registry.add("spring.flyway.password", postgreSQLContainer::getPassword);
    }

    public static void applyMigrate(PostgresqlTestContainer instance) {
        flywayContainer = Flyway.configure()
                .schemas("public")
                .dataSource(
                        instance.getJdbcUrl(),
                        instance.getUsername(),
                        instance.getPassword())
                .load();
        flywayContainer.info();
        flywayContainer.migrate();
    }

    public static void cleanMigrate() {
        flywayContainer.info();
        flywayContainer.clean();
    }
}
