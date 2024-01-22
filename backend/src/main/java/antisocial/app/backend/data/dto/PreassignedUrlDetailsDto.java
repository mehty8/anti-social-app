package antisocial.app.backend.data.dto;

public class PreassignedUrlDetailsDto {
    private String fileName;
    private String httpMethod;
    private String bucketName;
    private int timeInMs;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getBucketName() {
        return bucketName;
    }
    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public int getTimeInMs() {
        return timeInMs;
    }

    public void setTimeInMs(int timeInMs) {
        this.timeInMs = timeInMs;
    }
}
