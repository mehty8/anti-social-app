package antisocial.app.backend.data.dto;

public class PreassignedUrlDetailsDto {

    private String fileName;
    private String httpMethod;
    private String bucketName;
    private int timeInMs;
    private String timeOfRecording;

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

    public String getTimeOfRecording(){ return timeOfRecording; }

}
