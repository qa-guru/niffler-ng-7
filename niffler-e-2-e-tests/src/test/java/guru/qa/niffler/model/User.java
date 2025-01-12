package guru.qa.niffler.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {
    public static final String REGESTED_USERNAME = "vika";
    public static final String REGESTED_PASSWORD = "12345";

    private String username;
    private String password;

    public static User getRegestedUser() {
        return new User(REGESTED_USERNAME, REGESTED_PASSWORD);
    }
}
