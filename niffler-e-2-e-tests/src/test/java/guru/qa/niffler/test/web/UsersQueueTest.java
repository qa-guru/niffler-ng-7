package guru.qa.niffler.test.web;


import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension.StaticUser;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType;
import org.junit.jupiter.api.Test;

import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType.Type.EMPTY;
import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType.Type.WITH_FRIENDS;

@WebTest
class UsersQueueTest {

    @Test
    void testWithEmptyUser0(@UserType(WITH_FRIENDS) StaticUser user0,
                            @UserType(EMPTY) StaticUser user1) throws InterruptedException {
        Thread.sleep(1000);
        System.out.println(user0);
        System.out.println(user1);
    }
}