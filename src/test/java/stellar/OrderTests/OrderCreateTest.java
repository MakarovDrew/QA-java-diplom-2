package stellar.OrderTests;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import stellar.action.OrderActions;
import stellar.action.UserActions;
import stellar.models.Order;
import stellar.models.User;
import stellar.DataGeneration;

public class OrderCreateTest {
    private OrderActions orderActions;
    private Order order;
    private UserActions userActions;
    private User user;


    @Before
    public void createTestData() {
        orderActions = new OrderActions();
        userActions = new UserActions();
        user = DataGeneration.generatingDataToCreateValidUser();
        order = DataGeneration.creatingValidOrder();
    }

    @Test
    @DisplayName("Создание валидного заказа без авторизации")
    public void createOrderWithoutAuthorization() {
        ValidatableResponse response = orderActions.create(order);
        response.assertThat().statusCode(200);
    }

    @Test
    @DisplayName("Создание валидного заказа с авторизацией")
    public void createOrderWithAuthorization() {
        ValidatableResponse responseCreateUser = userActions.create(user);
        responseCreateUser.assertThat().statusCode(200);
        String token;
        token = responseCreateUser.extract().path("accessToken");
        StringBuilder sb = new StringBuilder(token);
        sb.delete(0, 7);
        token = sb.toString();
        ValidatableResponse responseCreateOrder = orderActions.create(order,token);
        responseCreateOrder.assertThat().statusCode(200);
    }
}