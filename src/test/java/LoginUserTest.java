import io.qameta.allure.junit4.DisplayName;
import org.apache.commons.lang3.RandomStringUtils;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import praktikum.pojo.CreateUser;
import praktikum.pojo.LoginUser;
import praktikum.clients.UserClient;
public class LoginUserTest {

    private UserClient userClient = new UserClient();

    private String accessToken;
    private String email             = RandomStringUtils.randomAlphabetic(8) + "@yandex.ru";
    private String password          = RandomStringUtils.randomAlphabetic(8);
    private String errorLoginMessage = "email or password are incorrect";

    @Before
    public void createUser() {
        CreateUser createUser = new CreateUser();
        createUser.setEmail(email);
        createUser.setName(RandomStringUtils.randomAlphabetic(8));
        createUser.setPassword(password);

        accessToken = userClient.create(createUser)
                .statusCode(200)
                .body("success", Matchers.equalTo(true))
                .extract().jsonPath().get("accessToken");
    }

    @Test
    @DisplayName("Успешная авторизация")
    public void loginUser() {
        LoginUser loginUser = new LoginUser();
        loginUser.setEmail(email);
        loginUser.setPassword(password);

        userClient.login(loginUser)
                .statusCode(200)
                .body("success", Matchers.equalTo(true));
    }

    @Test
    @DisplayName("Неудачная авторизация с неправильной почтой")
    public void loginUserWithWrongEmail() {
        LoginUser loginUser = new LoginUser();
        loginUser.setEmail("a" + email);
        loginUser.setPassword(password);

        userClient.login(loginUser)
                .statusCode(401)
                .body("success", Matchers.equalTo(false))
                .body("message", Matchers.equalTo(errorLoginMessage));
    }

    @Test
    @DisplayName("Неудачная авторизация с неправильным паролем")
    public void loginUserWithWrongPassword() {
        LoginUser loginUser = new LoginUser();
        loginUser.setEmail(email);
        loginUser.setPassword("a" + password);

        userClient.login(loginUser)
                .statusCode(401)
                .body("success", Matchers.equalTo(false))
                .body("message", Matchers.equalTo(errorLoginMessage));
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userClient.delete(accessToken);
        }
    }

}
