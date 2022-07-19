package ru.example.animals.config.container;


import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@ActiveProfiles("test")
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestPropertySource(locations = {"classpath:application.properties"})
public class PostgresqlTestContainer extends PostgreSQLContainer<PostgresqlTestContainer> {

    private final static String DOCKER_IMAGE_NAME = "postgres:14-alpine";
    private static volatile PostgresqlTestContainer instance;

    private PostgresqlTestContainer() {
        super(DOCKER_IMAGE_NAME);
    }

    public static PostgresqlTestContainer getInstance() {
        if (instance == null) {
            synchronized (PostgresqlTestContainer.class) {
                instance = new PostgresqlTestContainer()
                        .withDatabaseName("animals_db_test")
                        .withUsername("animal")
                        .withPassword("animal");
            }
        }

        return instance;
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void stop() {
        super.stop();
    }

    public String getHikariDriver() {
        return "org.postgresql.Driver";
    }
}
