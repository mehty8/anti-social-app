package antisocial.app.backend.service;

import antisocial.app.backend.data.dto.FriendsNamesAndRequestsDto;

import java.util.List;

public interface IFriendService {
    FriendsNamesAndRequestsDto getFriendsNamesAndRequests(String username);

    void sendFriendRequest(String username, String sender);

    void acceptFriendRequest(String receiver, String username);

    List<String> findUsers(String username);

    List<String> getFriendsRequests(String username);
}
