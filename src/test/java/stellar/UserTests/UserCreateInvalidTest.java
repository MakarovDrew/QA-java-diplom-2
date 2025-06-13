package stellar.UserTests;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import stellar.models.User;
import stellar.action.UserActions;

@RunWith(Parameterized.class)
public class UserCreateInvalidTest {
    private UserActions userActions;
    private final String email;
    private final String password;
    private final String name;
    private final int statusCode;
    private final String message;

    @Before
    public void createTestData() {
        userActions = new UserActions();
    }

    public UserCreateInvalidTest(String email, String password, String name, int statusCode,String message) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.statusCode = statusCode;
        this.message = message;
    }

    @Parameterized.Parameters
    public static Object[][] values() {
        return new Object[][]{
                {null, "test", "test", 403,"Email, password and name are required fields"},
                {"test", null, "test", 403,"Email, password and name are required fields"},
                {"test", "test", null, 403,"Email, password and name are required fields"},
        };
    }

    @Test
    @DisplayName("Создание пользователя без обязательного параметра")
    public void creatingUserWithoutRequiredParameter() {
        ValidatableResponse response = userActions.create(new User(email, password,name));
        response.assertThat().statusCode(statusCode);
        String messageFromResponse = response.extract().path("message");
        Assert.assertEquals(message, messageFromResponse);
    }
}