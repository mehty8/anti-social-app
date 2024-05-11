package antisocial.app.backend.service;

import antisocial.app.backend.data.dto.FriendsNamesAndRequestsDto;

import java.util.Set;


public interface IFriendService {

    FriendsNamesAndRequestsDto getFriendsNamesAndRequests(String username);

    void handleFriendRequest(String receiver, String sender, String type);

    Set<String> findUsers(String usernameToSearch, String userUsername);

}
