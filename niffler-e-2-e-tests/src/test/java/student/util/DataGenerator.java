package student.util;

import com.github.javafaker.Faker;
import student.model.UserJson;

import java.util.Locale;

public class DataGenerator {

    private static final Faker faker = new Faker(Locale.US);
    public static final String userName = "mike";
    public static final String userPassword = "123456!@";

    public static final String category = "defaultCategory";


    public static UserJson getUser() {
        return new UserJson(faker.name().username(), faker.internet().password().substring(1, 7));
    }
}
