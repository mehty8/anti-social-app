package antisocial.app.backend.service;

import antisocial.app.backend.data.dto.PreassignedUrlDetailsDto;
import antisocial.app.backend.data.dto.VideosDto;

public interface IVideoService {

    String getPreassignedUrl(PreassignedUrlDetailsDto preassignedUrlDetailsDto);
    void savePreassignedUrlDetails(String bucketName, String videoName, String preassignedURl, String usernameOfReceiver, String usernameOfSender);
    VideosDto getSentVideosDetails(String username);

    VideosDto getReceivedVideosDetails(String username);
}
