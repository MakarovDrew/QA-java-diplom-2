package stellar.action;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import stellar.BaseTest;
import stellar.models.User;

import static io.restassured.RestAssured.given;

public class UserActions extends BaseTest {
    private static final String REGISTER_URL = "/api/auth/register";
    private static final String USER_URL = "/api/auth/user";
    private static final String LOGIN_URL = "/api/auth/login";

    @Step("Создать пользователя")
    public ValidatableResponse create(User user) {
        return given()
                .spec(getSpec())
                .body(user)
                .when()
                .post(REGISTER_URL )
                .then();
    }

    @Step("Удалить пользователя")
    public ValidatableResponse delete(String accessToken) {
        return given()
                .spec(getSpec())
                .auth().oauth2(accessToken)
                .when()
                .delete(USER_URL).then();
    }

    @Step("Авторизация пользователя")
    public ValidatableResponse login(User user) {
        return given()
                .spec(getSpec())
                .body(user)
                .when()
                .post(LOGIN_URL)
                .then();
    }

    @Step("Изменение данных пользователя")
    public ValidatableResponse change(User user, String accessToken) {
        return given()
                .spec(getSpec())
                .auth().oauth2(accessToken)
                .body(user)
                .when()
                .patch(USER_URL)
                .then();
    }
}