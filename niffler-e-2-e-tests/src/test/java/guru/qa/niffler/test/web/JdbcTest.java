package guru.qa.niffler.test.web;

import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.UsersDbClient;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class JdbcTest {


    @Test
    public void successfulTaTxTest() {
        UsersDbClient usersDbClient = new UsersDbClient();
        String username = RandomDataUtils.getRandomUsername();
        System.out.println("!!!!!!!! username = " + username);

        UserJson user = usersDbClient.createCorrectUserSpringJdbc(
                new UserJson(
                        null,
                        username,
                        null,
                        null,
                        null,
                        CurrencyValues.RUB,
                        null,
                        null,
                        null
                )
        );
        System.out.println("!!!!!!!! " + user);
        assertTrue(usersDbClient.findByUsername(username).isPresent());
    }

    @Test
    public void unsuccessfulTaTxTest() {
        UsersDbClient usersDbClient = new UsersDbClient();
        String username = RandomDataUtils.getRandomUsername();
        System.out.println("!!!!!!!! username = " + username);

        try {
            UserJson user = usersDbClient.createIncorrectUserSpringJdbc(
                    new UserJson(
                            null,
                            username,
                            null,
                            null,
                            null,
                            CurrencyValues.RUB,
                            null,
                            null,
                            null
                    )
            );
            System.out.println("!!!!!!!! " + user);
        }catch (Exception e){
            //NOP
        }
    }


    @Test
    void springChainedManagerWithIncorrectDataTest() {
        UsersDbClient userDbClient = new UsersDbClient();
        String username = RandomDataUtils.getRandomUsername();
        UserJson user = userDbClient.createWithChainedTxManager(
                new UserJson(
                        null,
                        username,
                        null,
                        null,
                        "Chained Manager Negative Test",
                        CurrencyValues.RUB,
                        null,
                        null,
                        null

                ));

        System.out.println(user);
    }
}
