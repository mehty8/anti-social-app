package antisocial.app.frontend.page;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import antisocial.app.frontend.R;
import antisocial.app.frontend.adapter.UserFinderAdapter;
import antisocial.app.frontend.adapter.VideoListAdapter;
import antisocial.app.frontend.data.dto.VideoDetailsToPlay;

public class VideosActivity extends AppCompatActivity {
    private List<VideoDetailsToPlay> videos;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        videos = (List<VideoDetailsToPlay>) intent.getSerializableExtra("videos");
        setContentView(R.layout.activity_videos);

        RecyclerView recyclerViewVideoList = findViewById(R.id.recyclerViewVideoList);
        recyclerViewVideoList.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.Adapter videoListAdapter = new VideoListAdapter(videos, VideosActivity.this);
        recyclerViewVideoList.setAdapter(videoListAdapter);

    }

}
