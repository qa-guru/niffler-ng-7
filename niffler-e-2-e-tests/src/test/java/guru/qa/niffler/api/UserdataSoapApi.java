package guru.qa.niffler.api;


import guru.qa.jaxb.userdata.*;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface UserdataSoapApi {

    @Headers(value = {
            "Content-type: text/xml",
            "Accept-Charset: utf-8"
    })
    @POST("ws")
    Call<UserResponse> currentUser(@Body CurrentUserRequest currentUserRequest);

    @Headers(value = {
            "Content-type: text/xml",
            "Accept-Charset: utf-8"
    })
    @POST("ws")
    Call<UsersResponse> allUsers(@Body AllUsersPageRequest allUsersPageRequest);

    @Headers(value = {
            "Content-type: text/xml",
            "Accept-Charset: utf-8"
    })
    @POST("ws")
    Call<UsersResponse> allFriends(@Body FriendsPageRequest friendsPageRequest);

    @Headers(value = {
            "Content-type: text/xml",
            "Accept-Charset: utf-8"
    })
    @POST("ws")
    Call<Void> removeFriend(@Body RemoveFriendRequest request);

    @Headers(value = {
            "Content-type: text/xml",
            "Accept-Charset: utf-8"
    })
    @POST("ws")
    Call<UsersResponse> addFriend(@Body AcceptInvitationRequest request);

    @Headers(value = {
            "Content-type: text/xml",
            "Accept-Charset: utf-8"
    })
    @POST("ws")
    Call<UsersResponse> declineFriend(@Body DeclineInvitationRequest request);

    @Headers(value = {
            "Content-type: text/xml",
            "Accept-Charset: utf-8"
    })
    @POST("ws")
    Call<UsersResponse> inviteFriend(@Body SendInvitationRequest request);
}