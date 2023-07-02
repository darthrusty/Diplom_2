package praktikum.clients;

import praktikum.pojo.CreateUser;
import praktikum.pojo.LoginUser;

import io.restassured.response.ValidatableResponse;
import static io.restassured.RestAssured.given;

public class UserClient extends BaseClient {

    private static final String REGISTER_API_URL = "api/auth/register";
    private static final String LOGIN_API_URL    = "api/auth/login";
    private static final String CHANGE_API_URL   = "api/auth/user";

    public ValidatableResponse create(CreateUser createUser) {
        return given()
                .spec(getSpec())
                .body(createUser)
                .when()
                .post(REGISTER_API_URL)
                .then();
    }

    public ValidatableResponse login(LoginUser loginUser) {
        return given()
                .spec(getSpec())
                .body(loginUser)
                .when()
                .post(LOGIN_API_URL)
                .then();
    }

    public ValidatableResponse changeUserData(String accessToken, CreateUser createUser) {
        return given()
                .spec(getSpec())
                .header("Authorization", accessToken)
                .body(createUser)
                .when()
                .patch(CHANGE_API_URL)
                .then();
    }

    public ValidatableResponse delete(String accessToken) {
        return given()
                .spec(getSpec())
                .header("Authorization", accessToken)
                .when()
                .delete(CHANGE_API_URL)
                .then();
    }

}
