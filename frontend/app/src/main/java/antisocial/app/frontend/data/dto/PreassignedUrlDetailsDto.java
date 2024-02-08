package antisocial.app.frontend.data.dto;

public class PreassignedUrlDetailsDto {

    private String fileName;
    private String httpMethod;
    private String bucketName;
    private int timeInMs;

    public PreassignedUrlDetailsDto(String fileName, String httpMethod, String bucketName, int timeInMs) {
        this.fileName = fileName;
        this.httpMethod = httpMethod;
        this.bucketName = bucketName;
        this.timeInMs = timeInMs;

    }

    public String getFileName() {
        return fileName;
    }
    public String getHttpMethod() {
        return httpMethod;
    }

    public String getBucketName() {
        return bucketName;
    }
    public int getTimeInMs() {
        return timeInMs;
    }
}
