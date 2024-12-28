package guru.qa.niffler.utils;

import com.github.javafaker.Faker;

public class DataUtils {
    private static final Faker faker = new Faker();

    public static String generateNewUserName(){
        return faker.name().username();
    }

    public static String generatePassword(){
        return faker.internet().password(4,8);
    }
}