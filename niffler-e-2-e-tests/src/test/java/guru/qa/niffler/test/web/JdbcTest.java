package guru.qa.niffler.test.web;

import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.SpendDbClient;
import guru.qa.niffler.service.UserDbClient;
import guru.qa.niffler.utils.DataUtils;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class JdbcTest {

    @Test
    void txTest() {
        SpendDbClient spendDbClient = new SpendDbClient();
        SpendJson spend = spendDbClient.createSpend(
                new SpendJson(
                        null,
                        new Date(),
                        new CategoryJson(
                                null,
                                "cat-name-trr",
                                "taty",
                                false
                        ),
                        CurrencyValues.RUB,
                        1000.0,
                        "spend-name-tx",
                        "taty"
                )
        );
        System.out.println(spend);
    }

    @Test
    void successesAxTransaction() {
        UserDbClient userDbClient = new UserDbClient();
        String userName = DataUtils.randomUserName();
        UUID userID = UUID.randomUUID();
        String name = DataUtils.randomName();
        String surname = DataUtils.randomSurname();
        UserJson user = userDbClient.createUserSpringJdbc(
                new UserJson(
                        userID,
                        userName,
                        name,
                        surname,
                        name + " " + surname,
                        CurrencyValues.RUB,
                        null,
                        null));

        System.out.println(user);

    }

    @Test
    void notAccurateExTransaction() {
        UserDbClient userDbClient = new UserDbClient();
        String userName = DataUtils.randomUserName();
        String name = DataUtils.randomName();
        String surname = DataUtils.randomSurname();
        Record user = userDbClient.createUserSpringJdbc(
                new UserJson(
                        UUID.randomUUID(),
                        "taty",
                        name,
                        surname,
                        name + " " + surname,
                        CurrencyValues.RUB,
                        null,
                        null));

        System.out.println(user);
    }

    @Test
    void springJdbcTest() {
        UserDbClient uc = new UserDbClient();
        UserJson user = uc.createUserSpringJdbc(
                new UserJson(
                        null,
                        "dd5",
                        null,
                        null,
                        null,
                        CurrencyValues.RUB,
                        null,
                        null

                )
        );
        System.out.println(user);
    }

    @Test
    void jbcChainedTest() {
        UserDbClient uc = new UserDbClient();
        UserJson user = uc.createUserWithChainTx(
                new UserJson(
                        null,
                        "UserChained",
                        null,
                        null,
                        null,
                        CurrencyValues.RUB,
                        null,
                        null

                )
        );
        System.out.println(user);
    }
    //Тест не прошел, но пользователь создался

    @Test
    void checkUserInList() {
        UserDbClient uc = new UserDbClient();
        List<AuthUserEntity> users = uc.findAllUsers();
        List<String> userNames = new ArrayList<>();
        for (AuthUserEntity user : users) {
            userNames.add(user.getUsername());
        }
        System.out.println(userNames);
    }
}