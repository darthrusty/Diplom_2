import io.qameta.allure.junit4.DisplayName;
import org.apache.commons.lang3.RandomStringUtils;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.clients.UserClient;
import praktikum.pojo.LoginUser;

import static praktikum.constants.constants.errorLoginMessage;

public class LoginUserTest extends BaseTest {

    private final UserClient userClient = new UserClient();
    private final String email = RandomStringUtils.randomAlphabetic(8) + "@yandex.ru";
    private final String password = RandomStringUtils.randomAlphabetic(8);
    private final String name = RandomStringUtils.randomAlphabetic(8);
    private String accessToken;

    @Before
    public void createUser() {
        accessToken = startUp(userClient, email, name, password);
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
        clearUp(userClient, accessToken);
    }

}
