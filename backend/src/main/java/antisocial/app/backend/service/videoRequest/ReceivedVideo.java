package antisocial.app.backend.service.videoRequest;

import antisocial.app.backend.data.entity.PreassignedUrlEntity;
import antisocial.app.backend.data.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class ReceivedVideo extends VideoRequest{


    @Override
    public void setPreassignedUrlEntities(UserEntity userEntity) {
        preassignedUrlEntities = userEntity.getReceivedPreassignedUrlsDetails();
    }

    @Override
    public boolean isNeeded(String type) {
        return type.equals("Received");
    }

    @Override
    protected String getSenderOrReceiver(PreassignedUrlEntity preassignedUrlEntity) {
        return preassignedUrlEntity.getSender().getUsername();
    }
}
