package antisocial.app.backend.data.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Id;
    private String username;
    private String password;
    @ManyToMany(fetch = FetchType.EAGER)
    private List<RoleEntity> roles;
    @OneToMany(mappedBy = "receiver")
    private List<PreassignedUrlEntity> receivedUrls;
    @OneToMany(mappedBy = "sender")
    private List<PreassignedUrlEntity> sentUrls;
    @ElementCollection
    private List<String> friendsNames;
    @ElementCollection
    private List<String> friendsRequests;

    public UserEntity() {
        roles = new ArrayList<>();
        friendsNames = new ArrayList<>();
        friendsRequests = new ArrayList<>();
    }

    public long getId() {
        return Id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public List<RoleEntity> getRoles() {
        return roles;
    }

    public List<PreassignedUrlEntity> getReceivedUrls() {
        return receivedUrls;
    }

    public List<PreassignedUrlEntity> getSentUrls() {
        return sentUrls;
    }

    public List<String> getFriendsNames() {
        return friendsNames;
    }

    public List<String> getFriendsRequests() {
        return friendsRequests;
    }


    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void addRole(RoleEntity roleEntity){
        this.roles.add(roleEntity);
    }

    public void addFriendRequest(String username){
        this.friendsRequests.add(username);
    }
    public void addFriendName(String username){
        this.friendsNames.add(username);
    }
    public void removeFriendRequest(String username){
        this.friendsRequests.remove(username);
    }

}

