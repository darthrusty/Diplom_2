import io.qameta.allure.junit4.DisplayName;
import org.apache.commons.lang3.RandomStringUtils;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import praktikum.pojo.CreateUser;
import praktikum.clients.UserClient;

public class ChangeUserDataTest {

    private UserClient userClient = new UserClient();

    private String accessToken;
    private String email    = RandomStringUtils.randomAlphabetic(8) + "@yandex.ru";
    private String password = RandomStringUtils.randomAlphabetic(8);
    private String name     = RandomStringUtils.randomAlphabetic(8);

    private String errorAuthorization = "You should be authorised";
    private String errorBusyEmail     = "User with such email already exists";

    @Before
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
    @DisplayName("Успешное изменение с авторизацией")
    public void changeUser() {
        CreateUser createUser = new CreateUser();
        createUser.setEmail(email);
        createUser.setName(name);
        createUser.setPassword(password);

        userClient.changeUserData(accessToken, createUser)
                .statusCode(200)
                .body("success", Matchers.equalTo(true));
    }

    @Test
    @DisplayName("Неудачное изменение с занятой почтой")
    public void changeUserWithUsedEmail() {
        CreateUser createUser = new CreateUser();
        createUser.setEmail("test-data@yandex.ru");
        createUser.setName(name);
        createUser.setPassword(password);

        userClient.changeUserData(accessToken, createUser)
                .statusCode(403)
                .body("success", Matchers.equalTo(false))
                .body("message", Matchers.equalTo(errorBusyEmail));
    }

    @Test
    @DisplayName("Неудачное изменение без авторизацией")
    public void changeUserWithoutAuthorization() {
        CreateUser createUser = new CreateUser();
        createUser.setEmail(email);
        createUser.setName(name);
        createUser.setPassword(password);

        userClient.changeUserData("", createUser)
                .statusCode(401)
                .body("success", Matchers.equalTo(false))
                .body("message", Matchers.equalTo(errorAuthorization));
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userClient.delete(accessToken);
        }
    }

}
