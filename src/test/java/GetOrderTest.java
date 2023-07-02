import io.qameta.allure.junit4.DisplayName;
import org.apache.commons.lang3.RandomStringUtils;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import praktikum.pojo.CreateUser;
import praktikum.clients.UserClient;
import praktikum.clients.OrderClient;

public class GetOrderTest {

    private UserClient userClient   = new UserClient();
    private OrderClient orderClient = new OrderClient();

    private String accessToken;

    private String errorAuthorization = "You should be authorised";

    @Before
    public void createUser() {
        CreateUser createUser = new CreateUser();
        createUser.setEmail(RandomStringUtils.randomAlphabetic(8) + "@yandex.ru");
        createUser.setName(RandomStringUtils.randomAlphabetic(8));
        createUser.setPassword(RandomStringUtils.randomAlphabetic(8));

        accessToken = userClient.create(createUser)
                .statusCode(200)
                .body("success", Matchers.equalTo(true))
                .extract().jsonPath().get("accessToken");
    }

    @Test
    @DisplayName("Успешное получение заказов c авторизацией")
    public void getOrdersWithAuthorization() {
        orderClient.getOrdersList(accessToken)
                .statusCode(200)
                .body("success", Matchers.equalTo(true));
    }

    @Test
    @DisplayName("Успешное получение заказов c авторизацией")
    public void getOrdersWithoutAuthorization() {
        orderClient.getOrdersList("")
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
