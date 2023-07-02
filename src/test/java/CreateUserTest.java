import io.qameta.allure.junit4.DisplayName;
import org.apache.commons.lang3.RandomStringUtils;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Test;

import praktikum.pojo.CreateUser;
import praktikum.clients.UserClient;

public class CreateUserTest {

    private UserClient userClient = new UserClient();

    private String accessToken;
    private String email             = RandomStringUtils.randomAlphabetic(8) + "@yandex.ru";
    private String password          = RandomStringUtils.randomAlphabetic(8);
    private String name              = RandomStringUtils.randomAlphabetic(8);
    private String errorExistingUser = "User already exists";
    private String errorData         = "Email, password and name are required fields";

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
        if (accessToken != null) {
            userClient.delete(accessToken);
        }
    }

}
