package antisocial.app.frontend.data.dto;

public class PreassignedUrlDetailsDto {

    private String fileName;
    private String httpMethod;
    private String bucketName;
    private int timeInMs;
    private String timeOfRecording;

    public PreassignedUrlDetailsDto(String fileName, String httpMethod, String bucketName,
                                    int timeInMs, String timeOfRecording) {
        this.fileName = fileName;
        this.httpMethod = httpMethod;
        this.bucketName = bucketName;
        this.timeInMs = timeInMs;
        this.timeOfRecording = timeOfRecording;

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

    public String getTimeOfRecording(){ return timeOfRecording; }
}
