package stellar.OrderTests;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import stellar.action.OrderActions;
import stellar.action.UserActions;
import stellar.models.Order;
import stellar.models.User;
import stellar.DataGeneration;

public class OrderGetTest {
    private OrderActions orderActions;
    private Order order;
    private UserActions userActions;
    private User user;
    private String token;

    @Before
    public void createTestData() {
        orderActions = new OrderActions();
        userActions = new UserActions();
        user = DataGeneration.generatingDataToCreateValidUser();
        order = DataGeneration.creatingValidOrder();
    }

    @After
    public void cleanup() {
        userActions.delete(token).statusCode(202);
    }

    @Test
    @DisplayName("Получить заказ, когда пользователь авторизован")
    public void getOrderAuthUser() {
        ValidatableResponse responseCreateUser = userActions.create(user);
        responseCreateUser.assertThat().statusCode(200);
        token = responseCreateUser.extract().path("accessToken");
        StringBuilder sb = new StringBuilder(token);
        sb.delete(0, 7);
        token = sb.toString();
        ValidatableResponse responseCreateOrder = orderActions.create(order,token);
        responseCreateOrder.assertThat().statusCode(200);
        ValidatableResponse responseGetOrder = orderActions.get(token);
        responseGetOrder.assertThat().statusCode(200);
    }

    @Test
    @DisplayName("Получить заказ, когда пользователь неавторизован")
    public void getOrderUnauthorized() {
        ValidatableResponse responseCreateUser = userActions.create(user);
        responseCreateUser.assertThat().statusCode(200);
        token = responseCreateUser.extract().path("accessToken");
        StringBuilder sb = new StringBuilder(token);
        sb.delete(0, 7);
        token = sb.toString();
        ValidatableResponse responseCreateOrder = orderActions.create(order,token);
        responseCreateOrder.assertThat().statusCode(200);
        ValidatableResponse responseGetOrder = orderActions.get("");
        responseGetOrder.assertThat().statusCode(401);
    }
}