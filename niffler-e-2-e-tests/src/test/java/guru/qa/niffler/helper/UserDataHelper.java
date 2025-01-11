package guru.qa.niffler.helper;

import com.github.javafaker.Faker;
import guru.qa.niffler.model.UserJson;

public class UserDataHelper {

    private static final Faker faker = new Faker();

    public static UserJson getRandomUser() {
        return new UserJson(faker.name().firstName(), faker.internet().password(3, 10));
    }

    public static String getRandomCategory() {
        return faker.random().hex();
    }
}
