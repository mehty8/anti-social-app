package antisocial.app.frontend.data.dto;

import java.util.List;
import java.util.Set;

public class FriendsNamesAndRequestsDTo {

    private Set<String> friendsNames;

    private Set<String> requestsNames;

    public FriendsNamesAndRequestsDTo(Set<String> friendsNames, Set<String> requestsNames) {
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
