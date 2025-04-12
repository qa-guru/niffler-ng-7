package guru.qa.niffler.service;


import guru.qa.grpc.niffler.*;
import guru.qa.niffler.data.projection.UserWithStatus;
import guru.qa.niffler.data.repository.UserRepository;
import guru.qa.niffler.grpc.CurrencyValues;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@GrpcService
public class GrpcUserdataService extends NifflerUserdataServiceGrpc.NifflerUserdataServiceImplBase {

    private final UserRepository userRepository;

    @Autowired
    public GrpcUserdataService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void getAllUsers(UserRequest request, StreamObserver<UserListResponse> responseObserver) {
        List<UserWithStatus> usersFromDb = request.getSearchQuery() == null
                ? userRepository.findByUsernameNot(request.getUsername())
                : userRepository.findByUsernameNot(request.getUsername(), request.getSearchQuery());

        List<UserResponse> users = usersFromDb.stream()
                .map(this::toUserResponse)
                .toList();

        UserListResponse response = UserListResponse.newBuilder()
                .addAllUsers(users)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }


    private UserResponse toUserResponse(UserWithStatus user) {
        UserResponse.Builder builder = UserResponse.newBuilder();

        builder.setId(user.id() != null ? user.id().toString() : "");
        builder.setUsername(user.username() != null ? user.username() : "");

        if (user.currency() != null) {
            builder.setCurrency(CurrencyValues.valueOf(user.currency().name()));
        }

        if (user.photoSmall() != null) {
            builder.setPhotoSmall(new String(user.photoSmall(), StandardCharsets.UTF_8));
        }
        if (user.status() != null) {
            builder.setFriendshipStatus(FriendshipStatus.valueOf(user.status().name()));
        }
        return builder.build();
    }
}