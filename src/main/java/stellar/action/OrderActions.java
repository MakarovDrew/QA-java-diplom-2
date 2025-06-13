package stellar.action;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import stellar.BaseTest;
import stellar.models.Order;

import static io.restassured.RestAssured.given;

public class OrderActions extends BaseTest {
    private static final String ORDER_URL  = "/api/orders";


    @Step("Создать заказ неавторизованным пользователем")
    public ValidatableResponse create(Order order) {
        return given()
                .spec(getSpec())
                .body(order)
                .when()
                .post(ORDER_URL)
                .then();
    }

    @Step("Создать заказ авторизованным пользователем")
    public ValidatableResponse create(Order order, String accessToken) {
        return given()
                .spec(getSpec())
                .auth().oauth2(accessToken)
                .body(order)
                .when()
                .post(ORDER_URL)
                .then();
    }

    @Step("Получить заказ")
    public ValidatableResponse get(String accessToken) {
        return given()
                .spec(getSpec())
                .auth().oauth2(accessToken)
                .when()
                .get(ORDER_URL)
                .then();
    }
}