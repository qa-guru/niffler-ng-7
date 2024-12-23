package quru.qa.niffler.helper;

import com.github.javafaker.Faker;
import quru.qa.niffler.model.UserJson;

public class UserDataHelper {

    public static UserJson getRandomUser() {
        Faker faker = new Faker();
        return new UserJson(faker.name().firstName(), faker.internet().password(3, 10));
    }

    public static String getRandomCategory() {
        Faker faker = new Faker();
        return faker.random().hex();
    }
}
