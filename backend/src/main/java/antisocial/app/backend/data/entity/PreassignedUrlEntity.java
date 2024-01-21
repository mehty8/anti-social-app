package antisocial.app.backend.data.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class PreassignedUrlEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String videoName;
    private String preassignedUrl;

    private String bucketName;
    private LocalDateTime expirationTime;
    @ManyToOne
    private UserEntity receiver;
    @ManyToOne
    private UserEntity sender;

    public PreassignedUrlEntity() {
        this.expirationTime = LocalDateTime.now().plusHours(12);
    }

    public long getId() {
        return id;
    }

    public String getVideoName() {
        return videoName;
    }

    public String getPreassignedUrl() {
        return preassignedUrl;
    }

    public String getBucketName() {
        return bucketName;
    }

    public LocalDateTime getExpirationTime() {
        return expirationTime;
    }

    public UserEntity getReceiver() {
        return receiver;
    }

    public UserEntity getSender() {
        return sender;
    }


    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public void setPreassignedUrl(String preassignedUrl) {
        this.preassignedUrl = preassignedUrl;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public void setReceiver(UserEntity receiver) {
        this.receiver = receiver;
    }

    public void setSender(UserEntity sender) {
        this.sender = sender;
    }
}
