package antisocial.app.backend.service;

import antisocial.app.backend.data.dto.IResponseDto;
import antisocial.app.backend.data.dto.PreassignedUrlDetailsDto;

import java.util.concurrent.CompletableFuture;


public interface IVideoService {

    CompletableFuture<String> getPreassignedUrl(PreassignedUrlDetailsDto preassignedUrlDetailsDto);

    CompletableFuture<Void> savePreassignedUrlDetails(String bucketName, String videoName, String preassignedURl,
                                   String usernameOfReceiver, String usernameOfSender);

    CompletableFuture<IResponseDto> getVideos(String username, String type);

}
