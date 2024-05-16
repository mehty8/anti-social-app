package antisocial.app.backend.service.friendRequest;

import antisocial.app.backend.data.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class SendHandleFriendRequest implements IHandleFriendRequest {

    @Override
    public boolean isNeeded(String type) {
        return type.equals("Send");
    }

    @Override
    public void handleRequest(UserEntity receiver, UserEntity sender) {
        receiver.addFriendRequest(sender.getUsername());
        sender.addSentFriendRequest(receiver.getUsername());
    }
}
