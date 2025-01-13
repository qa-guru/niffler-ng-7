package guru.qa.niffler.util;

import com.github.javafaker.Faker;

import java.util.Locale;

public class DataGenerator {
    private static final Faker faker = new Faker(Locale.US);

    public static String getRandomUsername() {
        return faker.name().username();
    }

    public static String getRandomPassword() {
        return faker.internet().password();
    }

    public static String getRandomCategoryName() {
        return faker.book().title();
    }
}
