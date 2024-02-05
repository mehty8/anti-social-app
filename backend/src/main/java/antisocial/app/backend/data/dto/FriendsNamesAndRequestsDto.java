package antisocial.app.backend.data.dto;

import java.util.List;
import java.util.Set;

public class FriendsNamesAndRequestsDto {
    private Set<String> friendsNames;

    private Set<String> requestsNames;

    public FriendsNamesAndRequestsDto(Set<String> friendsNames, Set<String> requestsNames) {
        this.friendsNames = friendsNames;
        this.requestsNames = requestsNames;
    }

    public Set<String> getFriendsNames() {
        return friendsNames;
    }

    public Set<String> getRequestsNames() {
        return requestsNames;
    }
}
