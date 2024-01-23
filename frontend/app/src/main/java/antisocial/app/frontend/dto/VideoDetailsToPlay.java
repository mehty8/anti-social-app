package antisocial.app.frontend.dto;

public class VideoDetailsToPlay {

    private String preassignedUrl;
    private String videoName;
    private String toOrFrom;

    public VideoDetailsToPlay(String preassignedUrl, String videoName, String toOrFrom) {
        this.preassignedUrl = preassignedUrl;
        this.videoName = videoName;
        this.toOrFrom = toOrFrom;
    }

    public String getPreassignedUrl() {
        return preassignedUrl;
    }

    public String getVideoName() {
        return videoName;
    }

    public String getToOrFrom() {
        return toOrFrom;
    }
}
