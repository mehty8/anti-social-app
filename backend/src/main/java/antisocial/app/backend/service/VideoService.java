package antisocial.app.backend.service;

import antisocial.app.backend.data.dto.PreassignedUrlDetailsDto;
import antisocial.app.backend.data.dto.VideoDetailsToPlay;
import antisocial.app.backend.data.dto.VideosDto;
import antisocial.app.backend.data.entity.PreassignedUrlEntity;
import antisocial.app.backend.data.entity.UserEntity;
import antisocial.app.backend.repository.IPreassignedUrlRepository;
import antisocial.app.backend.repository.IUserRepository;
import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class VideoService implements IVideoService {
    private AmazonS3 s3Client;
    private IUserRepository userRepository;
    private IPreassignedUrlRepository preassignedUrlRepository;

    public VideoService(AmazonS3 s3Client, IUserRepository userRepository, IPreassignedUrlRepository preassignedUrlRepository) {
        this.s3Client = s3Client;
        this.userRepository = userRepository;
        this.preassignedUrlRepository = preassignedUrlRepository;
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

    /*
    @Override
    @Async
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
    }*/

    @Override
    public void savePreassignedUrlDetails(String bucketName, String videoName, String preassignedUrl,
                                          String usernameOfReceiver, String usernameOfSender) {

        UserEntity receiver = userRepository.findByUsername(usernameOfReceiver).get();
        UserEntity sender = userRepository.findByUsername(usernameOfSender).get();

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

    /*
    @Override
    @Async
    public CompletableFuture<Void> savePreassignedUrlDetailsAsync(String bucketName, String videoName, String preassignedUrl,
                                                                  String usernameOfReceiver, String usernameOfSender){
        savePreassignedUrlDetails(bucketName, videoName, preassignedUrl, usernameOfReceiver, usernameOfSender);
        return CompletableFuture.completedFuture(null);
    }

    @Transactional
    public void savePreassignedUrlDetails(String bucketName, String videoName, String preassignedUrl,
                                          String usernameOfReceiver, String usernameOfSender) {

        UserEntity receiver = userRepository.findByUsername(usernameOfReceiver).get();
        UserEntity sender = userRepository.findByUsername(usernameOfSender).get();

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
    }*/

    @Override
    public VideosDto getSentVideosDetails(String username) {
        UserEntity user = userRepository.findByUsername(username).get();

        List<PreassignedUrlEntity> preassignedUrlDetails = user.getSentPreassignedUrlsDetails();

        deleteExpiredUrls(preassignedUrlDetails);

        VideosDto sentVideosDetails = new VideosDto();

        preassignedUrlDetails.forEach(video -> {
            VideoDetailsToPlay videoDetailsToPlay = new VideoDetailsToPlay(video.getPreassignedUrl(), video.getVideoName(), video.getReceiver().getUsername());
            sentVideosDetails.getVideoDetailsToPlay().add(videoDetailsToPlay);
        });

        return sentVideosDetails;
    }

    /*
    * @Override
    @Async
    public CompletableFuture<VideosDto> getSentVideosDetails(String username) {
        UserEntity user = userRepository.findByUsername(username).get();

        List<PreassignedUrlEntity> preassignedUrlDetails = user.getSentPreassignedUrlsDetails();

        return deleteExpiredUrlsAsync(preassignedUrlDetails).thenApply(aVoid -> {

            VideosDto sentVideosDetails = new VideosDto();

            preassignedUrlDetails.forEach(video -> {
                VideoDetailsToPlay videoDetailsToPlay = new VideoDetailsToPlay(video.getPreassignedUrl(), video.getVideoName(), video.getReceiver().getUsername());
                sentVideosDetails.getVideoDetailsToPlay().add(videoDetailsToPlay);
            });

            return sentVideosDetails;
        });
    }
*/

    @Override
    public VideosDto getReceivedVideosDetails(String username) {
        UserEntity user = userRepository.findByUsername(username).get();

        List<PreassignedUrlEntity> preassignedUrlDetails = user.getReceivedPreassignedUrlsDetails();

        deleteExpiredUrls(preassignedUrlDetails);

        VideosDto receivedVideosDetails = new VideosDto();

        preassignedUrlDetails.forEach(video -> {
            VideoDetailsToPlay videoDetailsToPlay = new VideoDetailsToPlay(video.getPreassignedUrl(), video.getVideoName(), video.getSender().getUsername());
            receivedVideosDetails.getVideoDetailsToPlay().add(videoDetailsToPlay);
        });

        return receivedVideosDetails;
    }


    /*@Override
    @Async
    public CompletableFuture<VideosDto> getReceivedVideosDetails(String username) {
        UserEntity user = userRepository.findByUsername(username).get();

        List<PreassignedUrlEntity> preassignedUrlDetails = user.getReceivedPreassignedUrlsDetails();

        return deleteExpiredUrlsAsync(preassignedUrlDetails).thenApply(aVoid -> {

            VideosDto receivedVideosDetails = new VideosDto();

            preassignedUrlDetails.forEach(video -> {
                VideoDetailsToPlay videoDetailsToPlay = new VideoDetailsToPlay(video.getPreassignedUrl(), video.getVideoName(), video.getSender().getUsername());
                receivedVideosDetails.getVideoDetailsToPlay().add(videoDetailsToPlay);
            });

            return receivedVideosDetails;
        });
    }*/

    private void deleteExpiredUrls(List<PreassignedUrlEntity> videoUrls){
        List<PreassignedUrlEntity> expiredPreassignedUrlsDetails = videoUrls.stream().filter(url
                -> url.getExpirationTime().isBefore(LocalDateTime.now())).toList();

        expiredPreassignedUrlsDetails.forEach(PreassignedUrlDetails -> {
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

    /*
    @Async
    public CompletableFuture<Void> deleteExpiredUrlsAsync(List<PreassignedUrlEntity> videoUrls){
        List<PreassignedUrlEntity> expiredPreassignedUrlsDetails = videoUrls.stream().filter(url
                -> url.getExpirationTime().isBefore(LocalDateTime.now())).toList();

        return deleteExpiredUrls(expiredPreassignedUrlsDetails);
    }

    @Async
    public CompletableFuture<Void> deleteExpiredUrls(List<PreassignedUrlEntity> expiredPreassignedUrlsDetails){
        return CompletableFuture.runAsync(() ->
                expiredPreassignedUrlsDetails.forEach(preassignedUrlDetails ->
                        s3Client.deleteObject(new DeleteObjectRequest(preassignedUrlDetails.getBucketName(),
                                preassignedUrlDetails.getVideoName()))
                )
        ).thenRun(() -> deleteExpiredUrlsFromDatabase(expiredPreassignedUrlsDetails));
    }

    @Transactional
    public void deleteExpiredUrlsFromDatabase(List<PreassignedUrlEntity> expiredPreassignedUrlsDetails){

        expiredPreassignedUrlsDetails.forEach(preassignedUrlDetails -> {
            UserEntity sender = preassignedUrlDetails.getSender();
            UserEntity receiver = preassignedUrlDetails.getReceiver();

            sender.removePreassignedUrlDetails(preassignedUrlDetails, "sent");
            receiver.removePreassignedUrlDetails(preassignedUrlDetails, "received");

            userRepository.save(sender);
            userRepository.save(receiver);
            preassignedUrlRepository.delete(preassignedUrlDetails);
        });
    }*/
}
