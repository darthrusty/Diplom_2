import io.qameta.allure.junit4.DisplayName;
import org.apache.commons.lang3.RandomStringUtils;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.clients.OrderClient;
import praktikum.clients.UserClient;
import praktikum.pojo.CreateOrder;
import praktikum.pojo.CreateUser;

import java.util.ArrayList;
import java.util.List;

import static praktikum.constants.constants.errorAuthorization;

public class CreateOrderTest extends BaseTest {

    private final UserClient userClient = new UserClient();
    private final OrderClient orderClient = new OrderClient();
    private final List<String> ingredients = new ArrayList<>();
    private final String email = RandomStringUtils.randomAlphabetic(8) + "@yandex.ru";
    private final String password = RandomStringUtils.randomAlphabetic(8);
    private final String name = RandomStringUtils.randomAlphabetic(8);
    private String accessToken;

    @Before
    public void createUser() {
        String id0;
        CreateUser createUser = new CreateUser();

        accessToken = startUp(userClient, email, password, name);

        id0 = orderClient.getIngredients()
                .statusCode(200)
                .body("success", Matchers.equalTo(true))
                .extract().jsonPath().get("data[0]._id");

        ingredients.add(id0);
    }

    @Test
    @DisplayName("Успешное создание заказа c авторизацией")
    public void createOrder() {
        CreateOrder createOrder = new CreateOrder();
        createOrder.setIngredients(ingredients);

        orderClient.create(accessToken, createOrder)
                .statusCode(200)
                .body("success", Matchers.equalTo(true));
    }

    @Test
    @DisplayName("Неудачное создание заказа без авторизации")
    public void createOrderWithoutAuthorization() {
        CreateOrder createOrder = new CreateOrder();
        createOrder.setIngredients(ingredients);

        orderClient.create("", createOrder)
                .statusCode(401)
                .body("success", Matchers.equalTo(false))
                .body("message", Matchers.equalTo(errorAuthorization));
    }

    @Test
    @DisplayName("Неудачное создание заказа без ингредиентов")
    public void createOrderWithoutIngredients() {
        CreateOrder createOrder = new CreateOrder();

        orderClient.create("", createOrder)
                .statusCode(400)
                .body("success", Matchers.equalTo(false));
    }

    @Test
    @DisplayName("Успешное создание заказа c ингредиентами")
    public void createOrderWithIngredients() {
        String id1;

        id1 = orderClient.getIngredients()
                .statusCode(200)
                .body("success", Matchers.equalTo(true))
                .extract().jsonPath().get("data[0]._id");

        ingredients.add(id1);

        CreateOrder createOrder = new CreateOrder();
        createOrder.setIngredients(ingredients);

        orderClient.create(accessToken, createOrder)
                .statusCode(200)
                .body("success", Matchers.equalTo(true));
    }

    @Test
    @DisplayName("Неудачное создание заказа с неверным хэшем ингридиентов")
    public void createOrderWithWrongIngredientsId() {
        ingredients.add("");

        CreateOrder createOrder = new CreateOrder();
        createOrder.setIngredients(ingredients);

        orderClient.create(accessToken, createOrder)
                .statusCode(500);
    }

    @After
    public void tearDown() {
        clearUp(userClient, accessToken);
    }

}
