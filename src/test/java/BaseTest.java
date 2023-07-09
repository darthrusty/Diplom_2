import io.qameta.allure.Step;
import org.hamcrest.Matchers;
import praktikum.clients.UserClient;
import praktikum.pojo.CreateUser;

public class BaseTest {

    @Step("Создание тестового пользователя")
    public String startUp(UserClient userClient, String email, String name, String password) {
        CreateUser createUser = new CreateUser();
        createUser.setEmail(email);
        createUser.setName(name);
        createUser.setPassword(password);

        return userClient.create(createUser)
                .statusCode(200)
                .body("success", Matchers.equalTo(true))
                .extract().jsonPath().get("accessToken");
    }

    @Step("Удаление тестового пользователя")
    public void clearUp(UserClient userClient, String accessToken) {
        if (accessToken != null) {
            userClient.delete(accessToken);
        }
    }

}
