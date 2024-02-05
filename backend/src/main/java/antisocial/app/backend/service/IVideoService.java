package antisocial.app.backend.service;

import antisocial.app.backend.data.dto.PreassignedUrlDetailsDto;
import antisocial.app.backend.data.dto.VideosDto;

import java.util.concurrent.CompletableFuture;

public interface IVideoService {

    String getPreassignedUrl(PreassignedUrlDetailsDto preassignedUrlDetailsDto);

    //CompletableFuture<String> getPreassignedUrl(PreassignedUrlDetailsDto preassignedUrlDetailsDto);
    void savePreassignedUrlDetails(String bucketName, String videoName, String preassignedURl,
                                   String usernameOfReceiver, String usernameOfSender);

    /*CompletableFuture<Void> savePreassignedUrlDetailsAsync(String bucketName, String videoName,
                                                           String preassignedURl, String usernameOfReceiver, String usernameOfSender);*/
    VideosDto getSentVideosDetails(String username);

    //CompletableFuture<VideosDto> getSentVideosDetails(String username);

    VideosDto getReceivedVideosDetails(String username);

    //CompletableFuture<VideosDto> getReceivedVideosDetails(String username);
}
