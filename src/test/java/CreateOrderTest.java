import io.qameta.allure.junit4.DisplayName;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import praktikum.pojo.CreateUser;
import praktikum.pojo.CreateOrder;
import praktikum.clients.UserClient;
import praktikum.clients.OrderClient;

public class CreateOrderTest {

    private UserClient userClient   = new UserClient();
    private OrderClient orderClient = new OrderClient();

    private String accessToken;
    private List<String> ingredients = new ArrayList<>();

    private String errorAuthorization = "You should be authorised";

    @Before
    public void createUser() {
        String id0;
        CreateUser createUser = new CreateUser();
        createUser.setEmail("testMailz22@mail.ru");
        createUser.setName("imyaryak");
        createUser.setPassword("password");

        accessToken = userClient.create(createUser)
                .statusCode(200)
                .body("success", Matchers.equalTo(true))
                .extract().jsonPath().get("accessToken");

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
    @DisplayName("Неудачное создание заказа неверным хэшем ингридиентов")
    public void createOrderWithWrongIngredientsId() {
        ingredients.add("");

        CreateOrder createOrder = new CreateOrder();
        createOrder.setIngredients(ingredients);

        orderClient.create(accessToken, createOrder)
                .statusCode(500);
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userClient.delete(accessToken);
        }
    }

}
