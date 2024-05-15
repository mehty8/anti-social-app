package antisocial.app.backend.controller;

import antisocial.app.backend.errorHandling.exception.component.CatchException;
import antisocial.app.backend.data.dto.PreassignedUrlDetailsDto;
import antisocial.app.backend.data.dto.PreassignedUrlToUploadVideoDto;
import antisocial.app.backend.data.dto.ResponseMessageDto;
import antisocial.app.backend.data.dto.VideosDto;
import antisocial.app.backend.service.IVideoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> getPreassignedUrlToUploadVideo(
            @RequestBody PreassignedUrlDetailsDto preassignedUrlDetailsDto){

        try {
            String preassignedUrl = videoService.getPreassignedUrl(preassignedUrlDetailsDto);

            PreassignedUrlToUploadVideoDto preassignedUrlToUploadVideo = new PreassignedUrlToUploadVideoDto(preassignedUrl);

            return ResponseEntity.ok(preassignedUrlToUploadVideo);

        } catch (Exception exception){

            return catchException.catchException(exception, exception.getMessage(), null, this.getClass());
        }
    }

    @PostMapping("aws/preassignedurl/get/{receiver}")
    public ResponseEntity<ResponseMessageDto> savePreassignedUrlDetailsToWatchVideo(
            @PathVariable String receiver, @RequestBody PreassignedUrlDetailsDto preassignedUrlDetailsDto){

        try {
            String preassignedUrl = videoService.getPreassignedUrl(preassignedUrlDetailsDto);

            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String sender = user.getUsername();
            String videoName = preassignedUrlDetailsDto.getFileName();
            String bucketName = preassignedUrlDetailsDto.getBucketName();

            videoService.savePreassignedUrlDetails(bucketName, videoName, preassignedUrl, receiver, sender);

            ResponseMessageDto simpleResponse = new ResponseMessageDto("Video sent");

            return ResponseEntity.ok(simpleResponse);

        } catch (Exception exception){

            return catchException.catchException(exception, exception.getMessage(), HttpStatus.BAD_REQUEST, this.getClass());
        }
    }

    @GetMapping("sent")
    public ResponseEntity<?> getSentVideosDetails(){

        try {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String username = user.getUsername();

            VideosDto sentVideosDetails = videoService.getVideos(username, "Sent");

            return ResponseEntity.ok(sentVideosDetails);

        } catch (Exception exception){

            return catchException.catchException(exception, exception.getMessage(), HttpStatus.BAD_REQUEST, this.getClass());
        }
    }

    @GetMapping("received")
    public ResponseEntity<?> getReceivedVideosDetails(){

        try {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String username = user.getUsername();

            VideosDto receivedVideosDetails = videoService.getVideos(username, "Received");

            return ResponseEntity.ok(receivedVideosDetails);

        } catch (Exception exception){

            return catchException.catchException(exception, exception.getMessage(), HttpStatus.BAD_REQUEST, this.getClass());
        }
    }
}
