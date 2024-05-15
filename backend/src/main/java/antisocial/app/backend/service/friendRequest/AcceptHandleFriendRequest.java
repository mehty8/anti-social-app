package antisocial.app.backend.service.friendRequest;

import antisocial.app.backend.data.entity.UserEntity;
import antisocial.app.backend.errorHandling.exception.FriendRequestException;
import org.springframework.stereotype.Component;

@Component
public class AcceptHandleFriendRequest implements IHandleFriendRequest {

    @Override
    public boolean isNeeded(String type) {
        return type.equals("Accept");
    }

    @Override
    public void handleRequest(UserEntity receiver, UserEntity sender) {
        boolean isSenderRemoved = receiver.removeFriendRequest(sender.getUsername());
        boolean isReceiverRemoved = sender.removeSentFriendRequest(receiver.getUsername());

        if(!isReceiverRemoved || !isSenderRemoved){
            throw new FriendRequestException("User did not receive friend request");
        }

        receiver.addFriendName(sender.getUsername());
        sender.addFriendName(receiver.getUsername());
    }
}
