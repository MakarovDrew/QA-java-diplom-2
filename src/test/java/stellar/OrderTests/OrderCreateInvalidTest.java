package stellar.OrderTests;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import stellar.action.OrderActions;
import stellar.models.Order;
import stellar.DataGeneration;

public class OrderCreateInvalidTest {
    private OrderActions orderActions;
    private Order order;

    @Before
    public void createTestData() {
        orderActions = new OrderActions();
        order = DataGeneration.creatingInvalidOrder();
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    public void creationOrderWithoutIngredients() {
        ValidatableResponse response = orderActions.create(new Order());
        response.assertThat().statusCode(400);
    }

    @Test
    @DisplayName("Создание заказа c неверным хешем ингредиентов")
    public void creatingOrderWithIncorrectIngredientHash() {
        ValidatableResponse response = orderActions.create(order);
        response.assertThat().statusCode(500);
    }
}