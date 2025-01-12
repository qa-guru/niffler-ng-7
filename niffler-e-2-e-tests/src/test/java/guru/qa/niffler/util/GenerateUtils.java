package guru.qa.niffler.util;

import com.github.javafaker.Faker;

import java.util.UUID;

public class GenerateUtils {
    private static final int PASSWORD_LENGTH = 8;

    private static final Faker faker = new Faker();

    public static String generateUsername() {
        return faker.name().username();
    }

    public static String generatePassword() {
        return faker.number().digits(PASSWORD_LENGTH);
    }

    public static UUID generateUUID() {
        return UUID.randomUUID();
    }

    public static String generateRandomCategory() {
        return faker.commerce().department();
    }
}
