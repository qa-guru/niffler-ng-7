package student.util;

import com.github.javafaker.Faker;

import java.util.Locale;

public class RandomDataUtils {
    private static final Faker faker = new Faker(Locale.US);

    public static String randomUsername(){
        return faker.name().username();
    }
    public static String randomName(){
        return faker.name().name();
    }
    public static String randomSurname(){
        return faker.name().lastName();
    }
    public static String randomCategoryName(){
        return faker.company().industry();
    }
    public static String randomSentence(){
        return faker.shakespeare().hamletQuote();
    }
}
