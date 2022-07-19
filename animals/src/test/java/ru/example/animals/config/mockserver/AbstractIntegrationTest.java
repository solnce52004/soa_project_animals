package ru.example.animals.config.mockserver;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.mockserver.client.MockServerClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.MockServerContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import ru.example.animals.config.container.ContainersEnvironment;

@ActiveProfiles("test")
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ContextConfiguration(initializers = {AbstractIntegrationTest.Initializer.class})
public abstract class AbstractIntegrationTest extends ContainersEnvironment {

    private static final String IMAGE_NAME = "jamesdbloom/mockserver:mockserver-5.13.2";

    @Container
    public static MockServerContainer mockServer = new MockServerContainer(DockerImageName.parse(IMAGE_NAME));

    protected MockServerClient mockServerClient = new MockServerClient(
            mockServer.getHost(),
            mockServer.getServerPort());

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "auth.api.base-verify-token-url=" + mockServer.getEndpoint()
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }

    @BeforeClass
    public static void setupContainer() {
        postgreSQLContainer.start();
        applyMigrate(postgreSQLContainer);
        mockServer.start();
    }

    @AfterClass
    public static void shutdown() {
        mockServer.stop();
        cleanMigrate();
        postgreSQLContainer.stop();
    }
}