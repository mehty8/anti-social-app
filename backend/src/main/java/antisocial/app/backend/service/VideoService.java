package antisocial.app.backend.service;

import antisocial.app.backend.data.dto.IResponseDto;
import antisocial.app.backend.data.dto.PreassignedUrlDetailsDto;
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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

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
    public CompletableFuture<String> getPreassignedUrl(PreassignedUrlDetailsDto preassignedUrlDetailsDto) {

        return CompletableFuture.supplyAsync(() -> {
            String fileName = preassignedUrlDetailsDto.getFileName();
            String bucketName = preassignedUrlDetailsDto.getBucketName();
            HttpMethod httpMethod = HttpMethod.valueOf(preassignedUrlDetailsDto.getHttpMethod());
            int timeInMs = preassignedUrlDetailsDto.getTimeInMs();
            Date date = new Date(System.currentTimeMillis() + timeInMs);

            URL url = s3Client.generatePresignedUrl(bucketName, fileName, date, httpMethod);

            return url.toString();
        });
    }

    @Override
    public CompletableFuture<Void> savePreassignedUrlDetails(String bucketName, String videoName, String preassignedUrl,
                                          String usernameOfReceiver, String usernameOfSender) {

        return CompletableFuture.runAsync(() -> {
            UserEntity sender = userRepository.findByUsername(usernameOfSender)
                    .orElseThrow(() -> new UsernameNotFoundException("There is no such user"));
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
        });
    }
    
    @Override
    public CompletableFuture<IResponseDto> getVideos(String username, String type) {

        return CompletableFuture.supplyAsync(() -> {
            UserEntity userEntity = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("There is no such user"));

            VideoRequest videoRequest = videoRequests.stream().filter(neededVideo -> neededVideo.isNeeded(type))
                    .findFirst().orElseThrow(() -> new VideoRequestException("There is no such video request"));
            videoRequest.setPreassignedUrlEntities(userEntity);

            List<PreassignedUrlEntity> expiredPreassignedUrls = videoRequest.getExpiredPreassignedUrls();
            deleteExpiredUrls(expiredPreassignedUrls);

            IResponseDto videosDto = videoRequest.getVideos();

            return videosDto;
        });
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
