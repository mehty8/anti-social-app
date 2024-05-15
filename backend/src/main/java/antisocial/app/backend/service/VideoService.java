package antisocial.app.backend.service;

import antisocial.app.backend.data.dto.PreassignedUrlDetailsDto;
import antisocial.app.backend.data.dto.VideosDto;
import antisocial.app.backend.data.entity.PreassignedUrlEntity;
import antisocial.app.backend.data.entity.UserEntity;
import antisocial.app.backend.errorHandling.exception.VideoRequestException;
import antisocial.app.backend.repository.IPreassignedUrlRepository;
import antisocial.app.backend.repository.IUserRepository;
import antisocial.app.backend.service.videoRequest.VideoRequest;
import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.Date;
import java.util.List;

@Service
public class VideoService implements IVideoService {
    private AmazonS3 s3Client;
    private IUserRepository userRepository;
    private IPreassignedUrlRepository preassignedUrlRepository;
    private List<VideoRequest> videoRequests;


    public VideoService(AmazonS3 s3Client, IUserRepository userRepository,
                        IPreassignedUrlRepository preassignedUrlRepository,
                        List<VideoRequest> videoRequests) {
        this.s3Client = s3Client;
        this.userRepository = userRepository;
        this.preassignedUrlRepository = preassignedUrlRepository;
        this.videoRequests = videoRequests;
    }

    @Override
    public String getPreassignedUrl(PreassignedUrlDetailsDto preassignedUrlDetailsDto) {
        String fileName = preassignedUrlDetailsDto.getFileName();
        String bucketName = preassignedUrlDetailsDto.getBucketName();
        HttpMethod httpMethod = HttpMethod.valueOf(preassignedUrlDetailsDto.getHttpMethod());
        int timeInMs = preassignedUrlDetailsDto.getTimeInMs();
        Date date = new Date(System.currentTimeMillis() + timeInMs);

        URL url = s3Client.generatePresignedUrl(bucketName, fileName, date, httpMethod);

        return url.toString();
    }

    @Override
    public void savePreassignedUrlDetails(String bucketName, String videoName, String preassignedUrl,
                                          String usernameOfReceiver, String usernameOfSender) {

        UserEntity sender = userRepository.findByUsername(usernameOfSender).get();
        UserEntity receiver = userRepository.findByUsername(usernameOfReceiver).orElseThrow(() ->
                new BadCredentialsException("There is no such user"));

        PreassignedUrlEntity preassignedUrlEntity = new PreassignedUrlEntity();
        preassignedUrlEntity.setVideoName(videoName);
        preassignedUrlEntity.setPreassignedUrl(preassignedUrl);
        preassignedUrlEntity.setBucketName(bucketName);
        preassignedUrlEntity.setReceiver(receiver);
        preassignedUrlEntity.setSender(sender);
        preassignedUrlRepository.save(preassignedUrlEntity);

        receiver.addPreassignedUrlDetails(preassignedUrlEntity, "received");
        sender.addPreassignedUrlDetails(preassignedUrlEntity, "sent");

        userRepository.save(receiver);
        userRepository.save(sender);
    }

    //do something with this, create an interface with different handling classes for instance
    @Override
    public VideosDto getVideos(String username, String type) {
        UserEntity userEntity = userRepository.findByUsername(username).get();

        VideoRequest videoRequest = videoRequests.stream().filter(neededVideo -> neededVideo.isNeeded(type))
                        .findFirst().orElseThrow(() -> new VideoRequestException("There is no such video request"));

        videoRequest.setPreassignedUrlEntities(userEntity);

        List<PreassignedUrlEntity> expiredPreassignedUrls = videoRequest.getExpiredPreassignedUrls();

        deleteExpiredUrls(expiredPreassignedUrls);

        VideosDto videosDto = videoRequest.getVideos();

        return videosDto;
    }

    private void deleteExpiredUrls(List<PreassignedUrlEntity> expiredPreassignedUrls){
        expiredPreassignedUrls.forEach(PreassignedUrlDetails -> {
            UserEntity sender = PreassignedUrlDetails.getSender();
            UserEntity receiver = PreassignedUrlDetails.getReceiver();

            sender.removePreassignedUrlDetails(PreassignedUrlDetails, "sent");
            receiver.removePreassignedUrlDetails(PreassignedUrlDetails, "received");

            s3Client.deleteObject(new DeleteObjectRequest(PreassignedUrlDetails.getBucketName(),
                    PreassignedUrlDetails.getVideoName()));

            userRepository.save(sender);
            userRepository.save(receiver);
            preassignedUrlRepository.delete(PreassignedUrlDetails);
        });
    }
}

/*List<PreassignedUrlEntity> preassignedUrlDetails = type.equals("Sent")
                ? user.getSentPreassignedUrlsDetails()
                : user.getReceivedPreassignedUrlsDetails();

        deleteExpiredUrls(preassignedUrlDetails);

        VideosDto videosDetails = new VideosDto();

        preassignedUrlDetails.forEach(video -> {
            String senderOrReceiver = type.equals("Sent")
                    ? video.getReceiver().getUsername()
                    : video.getSender().getUsername();

            VideoDetailsToPlay videoDetailsToPlay = new VideoDetailsToPlay(video.getPreassignedUrl(),
                    video.getVideoName(), senderOrReceiver);

            videosDetails.getVideoDetailsToPlay().add(videoDetailsToPlay);
        });

        return videosDetails;*/
