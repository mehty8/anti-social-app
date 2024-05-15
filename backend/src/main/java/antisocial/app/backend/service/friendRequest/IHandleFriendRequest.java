package antisocial.app.backend.service.friendRequest;

import antisocial.app.backend.data.entity.UserEntity;

public interface IHandleFriendRequest {

    boolean isNeeded(String type);

    void handleRequest(UserEntity receiver, UserEntity sender);
}
