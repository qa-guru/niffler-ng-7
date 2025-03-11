package guru.qa.niffler.test.web;

import guru.qa.niffler.jupiter.extension.UsersClientExtention;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.SpendDbClient;
import guru.qa.niffler.service.UserDbClient;
import guru.qa.niffler.service.UsersClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Date;

import static guru.qa.niffler.utils.DataUtils.*;

@ExtendWith(UsersClientExtention.class)
public class JdbcTest {
    private UsersClient uc;

    @Test
    void txTest() {
        SpendDbClient spendDbClient = new SpendDbClient();
        String username = randomUserName();
        SpendJson spend = spendDbClient.createSpend(
                new SpendJson(
                        null,
                        new Date(),
                        new CategoryJson(
                                null,
                                randomName(),
                                username,
                                false
                        ),
                        CurrencyValues.RUB,
                        1000,
                        randomDescription(2),
                        username
                )
        );
        System.out.println(spend);
    }


    @Test
    void createUser() {

        UsersClient uc = new UserDbClient();
        UserJson user = uc.createUser(
                randomUserName(),
                "123"
        );
        System.out.println(user);
    }

    @Test
    void incomeInvitationTesst() {
        UserJson requester = uc.createUser(
                randomUserName(),
                "123"
        );
        uc.addIncomeInvitation(requester, 2);
    }


}