package guru.qa.niffler.helpers.dataGeneration;

import com.github.javafaker.Faker;

import java.util.ArrayList;
import java.util.List;

public class RandomDataUtils {
  private static final   Faker faker = new Faker();

    public static String getUserName(){
        return faker.name().username();
    }

    public static String getCategoryName(){
        return faker.elderScrolls().dragon();
    }

    public static String getName(){
        return faker.name().name();
    }

    public static String getSurname(){
        return faker.name().lastName();
    }

    public static String randomSentence(int wordsCount){
        List<String> s = new ArrayList<>();
        for (int i = 0; i < wordsCount; i++) {
            s.add(faker.hipster().word());
        }
        return s.toString();
    }
}
