package guru.qa.niffler.utils;

import com.github.javafaker.Faker;

import java.util.Random;

public class DataUtils {
    private static final Faker faker = new Faker();
    private static final Random random = new Random();

    public static String randomUserName() {
        return faker.name().username();
    }

    public static String randomPassword() {
        return faker.internet().password(4, 8);
    }

    public static String randomName() {
        return faker.name().name();
    }

    public static String randomSurname() {
        return faker.name().lastName();
    }

    public static String randomCategoryName() {
        return "Category_" + random.nextInt(1000);
    }

    public static String randomDescription(int wordsCount) {
        return faker.lorem().sentence(wordsCount);
    }
}