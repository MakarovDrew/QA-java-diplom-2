package stellar.UserTests;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import stellar.DataGeneration;
import stellar.models.User;
import stellar.action.UserActions;

public class UserCreateTest {
    private UserActions userActions;
    private User user;
    private String token;

    @Before
    public void createTestData() {
        userActions = new UserActions();
        user = DataGeneration.generatingDataToCreateValidUser();
    }

    @After
    public void cleanup() {
        userActions.delete(token).statusCode(202);
    }

    @Test
    @DisplayName("создать уникального пользователя")
    public void userCanBeCreate() {
        ValidatableResponse response = userActions.create(user);
        response.assertThat().statusCode(200);
        token = response.extract().path("accessToken");
        StringBuilder sb = new StringBuilder(token);
        sb.delete(0, 7);
        token = sb.toString();
    }

    @Test
    @DisplayName("создать пользователя, который уже зарегистрирован")
    public void duplicateUserCannotBeCreated() {
        ValidatableResponse response = userActions.create(user);
        response.assertThat().statusCode(200);
        token = response.extract().path("accessToken");
        StringBuilder sb = new StringBuilder(token);
        sb.delete(0, 7);
        token = sb.toString();
        ValidatableResponse responseDuplicate = userActions.create(user);
        responseDuplicate.assertThat().statusCode(403);
    }
}