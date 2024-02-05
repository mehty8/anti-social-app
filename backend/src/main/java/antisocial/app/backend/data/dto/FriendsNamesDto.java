package antisocial.app.backend.data.dto;

import java.util.List;
import java.util.Set;

public class FriendsNamesDto {
    private Set<String> friendsNames;

    public FriendsNamesDto(Set<String> friendsNames) {
        this.friendsNames = friendsNames;
    }

    public Set<String> getFriendsNames() {
        return friendsNames;
    }
}
