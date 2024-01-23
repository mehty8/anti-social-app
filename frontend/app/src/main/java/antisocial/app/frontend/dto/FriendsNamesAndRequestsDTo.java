package antisocial.app.frontend.dto;

import java.util.List;

public class FriendsNamesAndRequestsDTo {

    private List<String> friendsNames;

    private List<String> requestsNames;

    public FriendsNamesAndRequestsDTo(List<String> friendsNames, List<String> requestsNames) {
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
