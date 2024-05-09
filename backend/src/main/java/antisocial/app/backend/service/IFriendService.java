package antisocial.app.backend.service;

import antisocial.app.backend.data.dto.FriendsNamesAndRequestsDto;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public interface IFriendService {
    FriendsNamesAndRequestsDto getFriendsNamesAndRequests(String username);

    void handleFriendRequest(String receiver, String sender, String type);

    //CompletableFuture<Void> acceptFriendRequestAsync(String receiver, String username);

    Set<String> findUsers(String usernameToSearch, String userUsername);

    //CompletableFuture<Set<String>> findUsers(String usernameToSearch, String userUsername);

}
