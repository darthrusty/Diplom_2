import io.qameta.allure.junit4.DisplayName;
import org.apache.commons.lang3.RandomStringUtils;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Test;
import praktikum.clients.UserClient;
import praktikum.pojo.CreateUser;

import static praktikum.constants.constants.errorData;
import static praktikum.constants.constants.errorExistingUser;

public class CreateUserTest extends BaseTest {

    private final UserClient userClient = new UserClient();
    private final String email = RandomStringUtils.randomAlphabetic(8) + "@yandex.ru";
    private final String password = RandomStringUtils.randomAlphabetic(8);
    private final String name = RandomStringUtils.randomAlphabetic(8);
    private String accessToken;

    @Test
    @DisplayName("Успешная регистрация")
    public void createUser() {
        CreateUser createUser = new CreateUser();
        createUser.setEmail(email);
        createUser.setName(name);
        createUser.setPassword(password);

        accessToken = userClient.create(createUser)
                .statusCode(200)
                .body("success", Matchers.equalTo(true))
                .extract().jsonPath().get("accessToken");
    }

    @Test
    @DisplayName("Неудачная регистрация с аналогичными параметрами")
    public void createTwinUser() {
        CreateUser createUser = new CreateUser();
        createUser.setEmail(email);
        createUser.setName(name);
        createUser.setPassword(password);

        accessToken = userClient.create(createUser)
                .statusCode(200)
                .body("success", Matchers.equalTo(true))
                .extract().jsonPath().get("accessToken");

        userClient.create(createUser)
                .statusCode(403)
                .body("success", Matchers.equalTo(false))
                .body("message", Matchers.equalTo(errorExistingUser));
    }

    @Test
    @DisplayName("Неудачная регистрация без почты")
    public void createUserWithoutName() {
        CreateUser createUser = new CreateUser();
        createUser.setEmail(email);
        createUser.setPassword(password);

        userClient.create(createUser)
                .statusCode(403)
                .body("success", Matchers.equalTo(false))
                .body("message", Matchers.equalTo(errorData));
    }

    @Test
    @DisplayName("Неудачная регистрация без пароля")
    public void createUserWithoutPassword() {
        CreateUser createUser = new CreateUser();
        createUser.setEmail(email);
        createUser.setPassword(password);

        userClient.create(createUser)
                .statusCode(403)
                .body("success", Matchers.equalTo(false))
                .body("message", Matchers.equalTo(errorData));
    }

    @Test
    @DisplayName("Неудачная регистрация без имени")
    public void createUserWithoutEmail() {
        CreateUser createUser = new CreateUser();
        createUser.setEmail(email);
        createUser.setName(name);

        userClient.create(createUser)
                .statusCode(403)
                .body("success", Matchers.equalTo(false))
                .body("message", Matchers.equalTo(errorData));
    }

    @After
    public void tearDown() {
        clearUp(userClient, accessToken);
    }

}
