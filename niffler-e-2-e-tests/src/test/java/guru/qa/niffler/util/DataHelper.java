package guru.qa.niffler.util;

import com.github.javafaker.Faker;

public class DataHelper {
    private static final Faker faker = new Faker();

    public static String randomUserName() {
        return faker.name().username();
    }
}
