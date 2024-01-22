package antisocial.app.backend.data.dto;

import java.util.ArrayList;
import java.util.List;

public class VideosDto {
    private List<VideoDetailsToPlay> videoDetailsToPlay;

    public VideosDto() {
        this.videoDetailsToPlay = new ArrayList<>();
    }

    public List<VideoDetailsToPlay> getVideoDetailsToPlay() {
        return videoDetailsToPlay;
    }

    public void addVideoDetails(VideoDetailsToPlay videoDetailsToPlay){
        this.videoDetailsToPlay.add(videoDetailsToPlay);
    }
}
