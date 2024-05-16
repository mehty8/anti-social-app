package antisocial.app.backend.service.videoRequest;

import antisocial.app.backend.data.entity.PreassignedUrlEntity;
import antisocial.app.backend.data.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class SentVideo extends VideoRequest{

    @Override
    public void setPreassignedUrlEntities(UserEntity userEntity) {
        preassignedUrlEntities = userEntity.getSentPreassignedUrlsDetails();
    }

    @Override
    public boolean isNeeded(String type) {
        return type.equals("Sent");
    }

    @Override
    protected String getSenderOrReceiver(PreassignedUrlEntity preassignedUrlEntity) {
        return preassignedUrlEntity.getReceiver().getUsername();
    }
}
