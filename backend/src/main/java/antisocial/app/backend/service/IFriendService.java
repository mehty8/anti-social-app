package antisocial.app.backend.service;

import antisocial.app.backend.data.dto.FriendsNamesAndRequestsDto;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public interface IFriendService {
    FriendsNamesAndRequestsDto getFriendsNamesAndRequests(String username);

    void sendFriendRequest(String username, String sender);

    void acceptFriendRequest(String receiver, String username);

    //CompletableFuture<Void> acceptFriendRequestAsync(String receiver, String username);

    Set<String> findUsers(String username, String usernameToExclude);

    //CompletableFuture<List<String>> findUsers(String username);

    List<String> getFriendsRequests(String username);
}
