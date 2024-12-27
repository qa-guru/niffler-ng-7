package guru.qa.niffler.test.web;

import guru.qa.niffler.jupiter.extension.UsersQueueExtensionForTwoParams;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import guru.qa.niffler.jupiter.extension.UsersQueueExtensionForTwoParams.StaticUser;
import guru.qa.niffler.jupiter.extension.UsersQueueExtensionForTwoParams.UserType;

@ExtendWith(UsersQueueExtensionForTwoParams.class)
public class ProfileTest {

    @Test
    void testWithEmptyUser0(@UserType(empty = true) StaticUser user0,
                                   @UserType(empty = false) StaticUser user1) throws InterruptedException {

        Thread.sleep(1000);
        System.out.println(user0);
        System.out.println(user1);
    }

    @Test
    void testWithEmptyUser1(@UserType(empty = true) StaticUser user0,
                                   @UserType(empty = false) StaticUser user1) throws InterruptedException {
        Thread.sleep(1000);
        System.out.println(user0);
        System.out.println(user1);
    }

    @Test
    void testWithEmptyUser3(@UserType(empty = false) StaticUser user0,
                                   @UserType(empty = false) StaticUser user1) throws InterruptedException {
        Thread.sleep(1000);
        System.out.println(user0);
        System.out.println(user1);
    }

}
