package stellar;

import com.github.javafaker.Faker;
import stellar.models.Order;
import stellar.models.User;

import java.util.ArrayList;
import java.util.List;

public class DataGeneration {

    public static User generatingDataToCreateValidUser() {
        Faker faker = new Faker();
        final String email = faker.internet().emailAddress();
        final String password = faker.internet().password(6, 9);
        final String name = faker.name().firstName();
        return new User(email, password, name);
    }

    public static Order creatingValidOrder() {
        List<String> order = new ArrayList<>();
        order.add("61c0c5a71d1f82001bdaaa72");
        order.add("61c0c5a71d1f82001bdaaa6e");
        order.add("61c0c5a71d1f82001bdaaa73");
        return new Order(order);
    }

    public static Order creatingInvalidOrder() {
        List<String> order = new ArrayList<>();
        order.add("1");
        order.add("2");
        order.add("3");
        return new Order(order);
    }
}