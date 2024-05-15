package antisocial.app.backend.service.videoRequest;

import antisocial.app.backend.data.dto.VideoDetailsToPlay;
import antisocial.app.backend.data.dto.VideosDto;
import antisocial.app.backend.data.entity.PreassignedUrlEntity;
import antisocial.app.backend.data.entity.UserEntity;

import java.time.LocalDateTime;
import java.util.List;

public abstract class VideoRequest {

    protected List<PreassignedUrlEntity> preassignedUrlEntities;


    public abstract void setPreassignedUrlEntities(UserEntity userEntity);

    public abstract boolean isNeeded(String type);


    public List<PreassignedUrlEntity> getExpiredPreassignedUrls(){
        List<PreassignedUrlEntity> expiredPreassignedUrlsDetails = preassignedUrlEntities.stream().filter(url
                -> url.getExpirationTime().isBefore(LocalDateTime.now())).toList();

        return expiredPreassignedUrlsDetails;
    }

    public VideosDto getVideos(){
        VideosDto videosDetails = new VideosDto();

        preassignedUrlEntities.forEach(preassignedUrlEntity -> {
            String senderOrReceiver = getSenderOrReceiver(preassignedUrlEntity);

            VideoDetailsToPlay videoDetailsToPlay = new VideoDetailsToPlay(preassignedUrlEntity.getPreassignedUrl(),
                    preassignedUrlEntity.getVideoName(), senderOrReceiver);

            videosDetails.getVideoDetailsToPlay().add(videoDetailsToPlay);
        });

        return videosDetails;
    }


    protected abstract String getSenderOrReceiver(PreassignedUrlEntity preassignedUrlEntity);
}
