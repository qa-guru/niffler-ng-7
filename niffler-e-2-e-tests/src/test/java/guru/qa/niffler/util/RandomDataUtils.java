package guru.qa.niffler.util;

import com.github.javafaker.Faker;

import java.util.Locale;

public class RandomDataUtils {
    private static final Faker faker = new Faker(Locale.US);

    public static String randomUsername() {
        return faker.name().username();
    }
    public static String randomName() {
        return faker.name().name();
    }
    public static String randomSurname() {
        return faker.superhero().name();
    }
    public static String randomPassword() {
        return faker.internet().password();
    }

    public static String randomCategoryName() {
        return faker.book().title() + System.currentTimeMillis();
    }
    public static String randomSentence() {
        return faker.elderScrolls().creature();
    }
}
