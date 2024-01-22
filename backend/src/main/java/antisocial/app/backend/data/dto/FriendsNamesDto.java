package antisocial.app.backend.data.dto;

import java.util.List;

public class FriendsNamesDto {
    private List<String> friendsNames;

    public FriendsNamesDto(List<String> friendsNames) {
        this.friendsNames = friendsNames;
    }

    public List<String> getFriendsNames() {
        return friendsNames;
    }
}
