package antisocial.app.backend.controller;

import antisocial.app.backend.data.dto.PreassignedUrlDetailsDto;
import antisocial.app.backend.data.dto.PreassignedUrlToUploadVideoDto;
import antisocial.app.backend.data.dto.ResponseMessageDto;
import antisocial.app.backend.data.dto.VideosDto;
import antisocial.app.backend.service.IVideoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/video")
public class VideoController {

    private IVideoService videoService;

    public VideoController(IVideoService videoService) {
        this.videoService = videoService;
    }

    @PostMapping("aws/preassignedurl/put")
    public ResponseEntity<PreassignedUrlToUploadVideoDto> getPreassignedUrlToUploadVideo(
            @RequestBody PreassignedUrlDetailsDto preassignedUrlDetailsDto){

        String preassignedUrl = videoService.getPreassignedUrl(preassignedUrlDetailsDto);

        PreassignedUrlToUploadVideoDto preassignedUrlToUploadVideo = new PreassignedUrlToUploadVideoDto(preassignedUrl);

        return ResponseEntity.ok(preassignedUrlToUploadVideo);
    }

    /*
    @PostMapping("aws/preassignedurl/put")
    public CompletableFuture<ResponseEntity<PreassignedUrlToUploadVideoDto>> getPreassignedUrlToUploadVideo(
            @RequestBody PreassignedUrlDetailsDto preassignedUrlDetailsDto){

        return videoService.getPreassignedUrl(preassignedUrlDetailsDto).thenApply(preassignedUrl ->
                ResponseEntity.ok(new PreassignedUrlToUploadVideoDto(preassignedUrl)));
    }*/

    @PostMapping("aws/preassignedurl/get/{receiver}")
    public ResponseEntity<ResponseMessageDto> savePreassignedUrlDetailsToWatchVideo(
            @PathVariable String receiver, @RequestBody PreassignedUrlDetailsDto preassignedUrlDetailsDto){

        String preassignedUrl = videoService.getPreassignedUrl(preassignedUrlDetailsDto);

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String sender = user.getUsername();
        String videoName = preassignedUrlDetailsDto.getFileName();
        String bucketName = preassignedUrlDetailsDto.getBucketName();

        videoService.savePreassignedUrlDetails(bucketName, videoName, preassignedUrl, receiver, sender);

        ResponseMessageDto simpleResponse = new ResponseMessageDto("Video sent");

        return ResponseEntity.ok(simpleResponse);
    }


    /*@PostMapping("aws/preassignedurl/get/{receiver}")
    public CompletableFuture<ResponseEntity<ResponseMessageDto>> savePreassignedUrlDetailsToWatchVideo(
            @PathVariable String receiver, @RequestBody PreassignedUrlDetailsDto preassignedUrlDetailsDto){

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String sender = user.getUsername();
        String videoName = preassignedUrlDetailsDto.getFileName();
        String bucketName = preassignedUrlDetailsDto.getBucketName();

        return videoService.getPreassignedUrl(preassignedUrlDetailsDto).thenCompose(preassignedUrl
                -> videoService.savePreassignedUrlDetailsAsync(bucketName, videoName, preassignedUrl, receiver, sender))
                .thenApply(aVoid -> ResponseEntity.ok(new ResponseMessageDto("Video sent")));
    }*/

    @GetMapping("sent")
    public ResponseEntity<VideosDto> getSentVideosDetails(){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = user.getUsername();

        VideosDto sentVideosDetails = videoService.getSentVideosDetails(username);

        return ResponseEntity.ok(sentVideosDetails);
    }

    /*
    @GetMapping("sent")
    public CompletableFuture<ResponseEntity<VideosDto>> getSentVideosDetails(){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = user.getUsername();

        return videoService.getSentVideosDetails(username).thenApply(sentVideosDetails
                -> ResponseEntity.ok(sentVideosDetails));
    }*/

    @GetMapping("received")
    public ResponseEntity<VideosDto> getReceivedVideosDetails(){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = user.getUsername();

        VideosDto receivedVideosDetails = videoService.getReceivedVideosDetails(username);

        return ResponseEntity.ok(receivedVideosDetails);
    }

    /*
    @GetMapping("received")
    public CompletableFuture<ResponseEntity<VideosDto>> getReceivedVideosDetails(){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = user.getUsername();

        return videoService.getReceivedVideosDetails(username).thenApply(receivedVideosDetails ->
                ResponseEntity.ok(receivedVideosDetails));
    }*/
}
