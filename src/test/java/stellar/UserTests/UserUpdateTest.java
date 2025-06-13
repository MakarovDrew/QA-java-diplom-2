package stellar.UserTests;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import stellar.DataGeneration;
import stellar.action.UserActions;
import stellar.models.User;

@RunWith(Parameterized.class)
public class UserUpdateTest {
    private UserActions userActions;
    private final String email;
    private final String password;
    private final String name;
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

    public UserUpdateTest(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    @Parameterized.Parameters
    public static Object[][] values() {
        return new Object[][]{
                {"123@yandex.ru", user.getPassword(), user.getName()},
                {user.getEmail(), "parol123", user.getName()},
                {user.getEmail(), user.getPassword(), "imya123"},
        };
    }

    @Test
    @DisplayName("Изменение данных пользователя с авторизацией")
    public void changingUserDataByAnAuthorizedUser() {
        ValidatableResponse response = userActions.create(user);
        response.assertThat().statusCode(200);
        User changeUser = new User(email, password, name);
        token = response.extract().path("accessToken");
        StringBuilder sb = new StringBuilder(token);
        sb.delete(0, 7);
        token = sb.toString();
        ValidatableResponse responseChange = userActions.change(changeUser, token);
        responseChange.assertThat().statusCode(200);
        ValidatableResponse responseAuth = userActions.login(changeUser);
        responseAuth.assertThat().statusCode(200);
    }

    @Test
    @DisplayName("Изменение данных пользователя без авторизации")
    public void changingUserDataByAnUnauthorizedUser() {
        ValidatableResponse response = userActions.create(user);
        response.assertThat().statusCode(200);
        User changeUser = new User(email, password, name);
        token = response.extract().path("accessToken");
        StringBuilder sb = new StringBuilder(token);
        sb.delete(0, 7);
        token = sb.toString();
        ValidatableResponse responseChange = userActions.change(changeUser, "");
        responseChange.assertThat().statusCode(401);
    }
}