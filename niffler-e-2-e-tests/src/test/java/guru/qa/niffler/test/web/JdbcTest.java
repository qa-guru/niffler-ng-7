package guru.qa.niffler.test.web;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.SpendDbClient;
import guru.qa.niffler.service.UserDbClient;
import guru.qa.niffler.service.UsersClient;
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
            "ddd16",
            "ddd17",
            "ddd18"
    })
    @ParameterizedTest
    void createUserTest(String name) {
        UsersClient uc = new UserDbClient();
        UserJson user = uc.createUser(
                name,
                "123"
        );
        System.out.println(user);
    }


    @Test
    void createUser() {

        UsersClient uc = new UserDbClient();
        UserJson user = uc.createUser(
                "o5",
                "123"
        );
        System.out.println(user);
    }

    @Test
    void incomeInvitationTesst() {
        UsersClient uc = new UserDbClient();
        UserJson requester = uc.createUser(
                "o19",
                "123"
        );
        uc.createIncomeInvitations(requester, 2);
    }


}