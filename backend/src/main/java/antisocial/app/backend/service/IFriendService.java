package antisocial.app.backend.service;

import antisocial.app.backend.data.dto.IResponseDto;

import java.util.Set;
import java.util.concurrent.CompletableFuture;


public interface IFriendService {

    CompletableFuture<IResponseDto> getFriendsNamesAndRequests(String username);

    CompletableFuture<Set<String>> findUsers(String usernameToSearch, String userUsername);

    CompletableFuture<Void> handleFriendRequest(String receiver, String sender, String type);

}
