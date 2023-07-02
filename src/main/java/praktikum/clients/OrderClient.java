package praktikum.clients;

import praktikum.pojo.CreateOrder;

import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class OrderClient extends BaseClient {

    private static final String ORDERS_API_URL      = "api/orders";
    private static final String INGREDIENTS_API_URL = "api/ingredients";

    public ValidatableResponse getIngredients() {
        return given()
                .spec(getSpec())
                .when()
                .get(INGREDIENTS_API_URL)
                .then();
    }

    public ValidatableResponse create(String accessToken, CreateOrder createOrder) {
        return given()
                .spec(getSpec())
                .header("Authorization", accessToken)
                .body(createOrder)
                .when()
                .post(ORDERS_API_URL)
                .then();
    }

    public ValidatableResponse getOrdersList(String accessToken) {
        return given()
                .spec(getSpec())
                .header("Authorization", accessToken)
                .when()
                .get(ORDERS_API_URL)
                .then();
    }

}
