package stellar.UserTests;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import stellar.DataGeneration;
import stellar.models.User;
import stellar.action.UserActions;

@RunWith(Parameterized.class)
public class UserLoginInvalidTest {
    private UserActions userActions;
    private final String email;
    private final String password;
    private final int statusCode;
    private final String message;
    private static final User user;
    private String token;

    static {
        user = DataGeneration.generatingDataToCreateValidUser();
    }

    @Before
    public void createTestData() {
        userActions = new UserActions();
    }

    @After
    public void cleanup() {
        userActions.delete(token).statusCode(202);
    }

    public UserLoginInvalidTest(String email, String password, int statusCode, String message) {
        this.email = email;
        this.password = password;
        this.statusCode = statusCode;
        this.message = message;
    }

    @Parameterized.Parameters
    public static Object[][] values() {
        return new Object[][]{
                {user.getEmail(), "test", 401, "email or password are incorrect"},
                {"test", user.getPassword(), 401, "email or password are incorrect"},
        };
    }

    @Test
    @DisplayName("Авторизация пользователя с неправильными параметрами")
    public void userAuthorizationWithIncorrectParameters() {
        ValidatableResponse response = userActions.create(user);
        response.assertThat().statusCode(200);
        token = response.extract().path("accessToken");
        StringBuilder sb = new StringBuilder(token);
        sb.delete(0, 7);
        token = sb.toString();
        ValidatableResponse responseLogin = userActions.login(new User(email, password, user.getName()));
        responseLogin.assertThat().statusCode(statusCode);
        String messageFromResponse = responseLogin.extract().path("message");
        Assert.assertEquals(message, messageFromResponse);
    }
}