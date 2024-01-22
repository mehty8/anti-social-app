package antisocial.app.backend.data.dto;

public class PreassignedUrlToUploadVideoDto {

    private String preassignedUrl;

    public PreassignedUrlToUploadVideoDto(String preassignedUrl) {
        this.preassignedUrl = preassignedUrl;
    }

    public String getPreassignedUrl() {
        return preassignedUrl;
    }
}
