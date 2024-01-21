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
    @OneToMany(mappedBy = "receiver")
    private List<PreassignedUrlEntity> receivedUrls;
    @OneToMany(mappedBy = "sender")
    private List<PreassignedUrlEntity> sentUrls;
    @ManyToMany(fetch = FetchType.EAGER)
    private List<RoleEntity> roles;
    @ElementCollection
    private List<String> friendsName;
    @ElementCollection
    private List<String> friendRequests;

    public UserEntity() {
        roles = new ArrayList<>();
        friendsName = new ArrayList<>();
        friendRequests = new ArrayList<>();
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

    public List<String> getFriendsNames() {
        return friendsName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getFriendRequests() {
        return friendRequests;
    }

    public List<PreassignedUrlEntity> getReceivedUrls() {
        return receivedUrls;
    }

    public List<PreassignedUrlEntity> getSentUrls() {
        return sentUrls;
    }
}

