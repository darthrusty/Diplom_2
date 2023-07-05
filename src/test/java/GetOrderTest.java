import io.qameta.allure.junit4.DisplayName;
import org.apache.commons.lang3.RandomStringUtils;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.clients.OrderClient;
import praktikum.clients.UserClient;

public class GetOrderTest extends BaseTest {

    private final UserClient userClient = new UserClient();
    private final OrderClient orderClient = new OrderClient();
    private final String errorAuthorization = "You should be authorised";
    private final String email = RandomStringUtils.randomAlphabetic(8) + "@yandex.ru";
    private final String password = RandomStringUtils.randomAlphabetic(8);
    private final String name = RandomStringUtils.randomAlphabetic(8);
    private String accessToken;

    @Before
    public void createUser() {
        accessToken = startUp(userClient, email, password, name);
    }

    @Test
    @DisplayName("Успешное получение заказов c авторизацией")
    public void getOrdersWithAuthorization() {
        orderClient.getOrdersList(accessToken)
                .statusCode(200)
                .body("success", Matchers.equalTo(true));
    }

    @Test
    @DisplayName("Неудачное получение заказов без авторизации")
    public void getOrdersWithoutAuthorization() {
        orderClient.getOrdersList("")
                .statusCode(401)
                .body("success", Matchers.equalTo(false))
                .body("message", Matchers.equalTo(errorAuthorization));
    }

    @After
    public void tearDown() {
        clearUp(userClient, accessToken);
    }

}
