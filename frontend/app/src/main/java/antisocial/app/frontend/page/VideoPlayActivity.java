package antisocial.app.frontend.page;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;

import antisocial.app.frontend.R;

public class VideoPlayActivity extends AppCompatActivity {

    private ExoPlayer exoPlayer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);
        Intent intent = getIntent();
        exoPlayer = new ExoPlayer.Builder(VideoPlayActivity.this).build();
        playVideo(intent.getStringExtra("url"));
    }


    private void playVideo(String url){

        PlayerView playerView = findViewById(R.id.player_view);
        playerView.setPlayer(exoPlayer);
        MediaItem mediaItem = MediaItem.fromUri(url);
        exoPlayer.setMediaItem(mediaItem);
        exoPlayer.prepare();
        exoPlayer.setPlayWhenReady(true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        exoPlayer.setPlayWhenReady(false);
        exoPlayer.release();
        exoPlayer = null;
    }
}
