package guru.qa.niffler.test.grpc;

import guru.qa.grpc.niffler.UserListResponse;
import guru.qa.grpc.niffler.UserRequest;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.model.UserJson;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserDataGrpcTest extends BaseGrpcTest {


    @User(incomeInvitations = 1)
    @ApiLogin
    @Test
    void searchShouldReturnUser(UserJson user) {
        UserRequest request = UserRequest.newBuilder()
                .setUsername(user.username())
                .setSearchQuery(user.testData().incomeInvitations().getFirst().username())
                .build();
        UserListResponse response = blockingStubUserdata.getAllUsers(request);
        assertEquals(user.testData().incomeInvitations().getFirst().username(),
                response.getUsersList().getFirst().getUsername());
    }

}