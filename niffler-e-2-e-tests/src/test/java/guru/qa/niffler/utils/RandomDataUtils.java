package guru.qa.niffler.utils;

import com.github.javafaker.Faker;

public class RandomDataUtils {

    private static final Faker faker = new Faker();

    public static String getRandomUsername() {
        return faker.name().firstName();
    }

    public static String getRandomPassword() {
        return faker.internet().password(3, 10);
    }

    public static String getRandomName() {
        return faker.name().username();
    }

    public static String getRandomSurname() {
        return faker.name().lastName();
    }

    public static String getRandomCategory() {
        return faker.random().hex();
    }

    public static String getRandomSentence(int wordsCount) {
        return faker.lorem().sentence(wordsCount);
    }
}
