package guru.qa.niffler.test.web;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.SpendDbClient;
import guru.qa.niffler.service.UserDbClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Date;

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
                                "cat-name-trrr",
                                "taty",
                                false
                        ),
                        CurrencyValues.RUB,
                        1000.0,
                        "spend-name-txt",
                        "taty"
                )
        );
        System.out.println(spend);
    }


    @ValueSource(strings = {
            "ddd13",
            "ddd14",
            "ddd15"
    })
    @ParameterizedTest
    void springJdbcTest(String name) {
        UserDbClient uc = new UserDbClient();
        UserJson user = uc.createUser(
                name,
                "123"
        );
        System.out.println(user);
    }

//    @Test
//    void jbcChainedTest() {
//        UserDbClient uc = new UserDbClient();
//        UserJson user = uc.createUserWithChainTx(
//                new UserJson(
//                        null,
//                        "UserChained",
//                        null,
//                        null,
//                        null,
//                        CurrencyValues.RUB,
//                        null,
//                        null
//
//                )
//        );
//        System.out.println(user);
//    }
    //Тест не прошел, но пользователь создался

//    @Test
//    void checkUserInList() {
//        UserDbClient uc = new UserDbClient();
//        List<AuthUserEntity> users = uc.findAllUsers();
//        List<String> userNames = new ArrayList<>();
//        for (AuthUserEntity user : users) {
//            userNames.add(user.getUsername());
//        }
//        System.out.println(userNames);
//    }
}