package guru.qa.niffler.helper;

import com.github.javafaker.Faker;
import guru.qa.niffler.model.UserJson;

public class UserDataHelper {

    private static final Faker faker = new Faker();

    public static String getRandomUsername() {
        return faker.name().firstName();
    }

    public static String getRandomName() {
        return faker.name().username();
    }

    public static String getRandomSurname() {
        return faker.name().lastName();
    }

    public static UserJson getRandomUser() {
        return new UserJson(faker.name().firstName(), faker.internet().password(3, 10));
    }

    public static String getRandomCategory() {
        return faker.random().hex();
    }

    public static String getRandomSentence(int wordsCount) {
        return faker.lorem().sentence(wordsCount);
    }
}
