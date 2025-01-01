package guru.qa.niffler.helpers.dataGeneration;

import lombok.Data;

import java.util.Random;

@Data
public class NewAccountDataGeneration {
    private String randomPassword = setRandomPassword();

    private String setRandomPassword(){
        char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder((100000 + rnd.nextInt(900000)));
        for (int i = 0; i < 3; i++)
            sb.append(chars[rnd.nextInt(chars.length)]);

        return sb.toString();
    }
}
