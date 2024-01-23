package antisocial.app.frontend.dto;

import java.util.ArrayList;
import java.util.List;

public class VideosDto {

    private List<VideoDetailsToPlay> videoDetailsToPlays;

    public VideosDto() {
        this.videoDetailsToPlays = new ArrayList<>();
    }
    public List<VideoDetailsToPlay> getVideoDtos() {
        return videoDetailsToPlays;
    }
}
