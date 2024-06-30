package antisocial.app.backend.controller;

import antisocial.app.backend.data.dto.*;
import antisocial.app.backend.errorHandling.exception.component.CatchException;
import antisocial.app.backend.service.IVideoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/video")
public class VideoController {

    private IVideoService videoService;

    private CatchException catchException;


    public VideoController(IVideoService videoService, CatchException catchException) {
        this.videoService = videoService;
        this.catchException = catchException;
    }


    @PostMapping("aws/preassignedurl/put")
    public CompletableFuture<ResponseEntity<IResponseDto>> getPreassignedUrlToUploadVideo(
            @RequestBody PreassignedUrlDetailsDto preassignedUrlDetailsDto){

        return videoService.getPreassignedUrl(preassignedUrlDetailsDto).thenApply(preassignedUrl -> {
            IResponseDto preassignedUrlToUploadVideo = new PreassignedUrlToUploadVideoDto(preassignedUrl);
            return ResponseEntity.ok(preassignedUrlToUploadVideo);
        }).exceptionally(exception
                -> catchException.catchException((Exception) exception, exception.getMessage(),
                null, this.getClass()));

    }

    @PostMapping("aws/preassignedurl/get/{receiver}")
    public CompletableFuture<ResponseEntity<IResponseDto>> savePreassignedUrlDetailsToWatchVideo(
            @PathVariable String receiver, @RequestBody PreassignedUrlDetailsDto preassignedUrlDetailsDto){

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String sender = user.getUsername();

        return videoService.getPreassignedUrl(preassignedUrlDetailsDto).thenCompose(preassignedUrl -> {
                    String videoName = preassignedUrlDetailsDto.getFileName();
                    String bucketName = preassignedUrlDetailsDto.getBucketName();

            return videoService.savePreassignedUrlDetails(bucketName, videoName, preassignedUrl, receiver, sender)
                    .thenApply(voided -> {
                        IResponseDto simpleResponse = new ResponseMessageDto("Video sent");

                        return ResponseEntity.ok(simpleResponse);
                    });
        }).exceptionally(exception
                -> catchException.catchException((Exception) exception, exception.getMessage(),
                HttpStatus.BAD_REQUEST, this.getClass()));

    }

    @GetMapping("sent")
    public CompletableFuture<ResponseEntity<IResponseDto>> getSentVideosDetails(){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = user.getUsername();

        return videoService.getVideos(username, "Sent").thenApply(sentVideosDetails
                -> ResponseEntity.ok(sentVideosDetails)).exceptionally(exception
                -> catchException.catchException((Exception) exception, exception.getMessage(),
                HttpStatus.BAD_REQUEST, this.getClass()));

    }

    @GetMapping("received")
    public CompletableFuture<ResponseEntity<IResponseDto>> getReceivedVideosDetails(){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = user.getUsername();

        return videoService.getVideos(username, "Received").thenApply(receivedVideosDetails
                -> ResponseEntity.ok(receivedVideosDetails)).exceptionally(exception
                -> catchException.catchException((Exception) exception, exception.getMessage(),
                HttpStatus.BAD_REQUEST, this.getClass()));

    }
}
