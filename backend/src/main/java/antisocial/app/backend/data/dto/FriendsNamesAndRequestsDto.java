package antisocial.app.backend.data.dto;

import java.util.List;

public class FriendsNamesAndRequestsDto {
    private List<String> friendsNames;

    private List<String> requestsNames;

    public FriendsNamesAndRequestsDto(List<String> friendsNames, List<String> requestsNames) {
        this.friendsNames = friendsNames;
        this.requestsNames = requestsNames;
    }

    public List<String> getFriendsNames() {
        return friendsNames;
    }

    public List<String> getRequestsNames() {
        return requestsNames;
    }
}
