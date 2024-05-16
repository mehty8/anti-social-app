package antisocial.app.frontend.page;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import antisocial.app.frontend.R;
import antisocial.app.frontend.adapter.IAdapter;
import antisocial.app.frontend.adapter.VideoListAdapter;
import antisocial.app.frontend.data.dto.VideoDetailsToPlay;
import antisocial.app.frontend.service.HandleAdapters;

public class VideosActivity extends AppCompatActivity {
    private List<VideoDetailsToPlay> videos;
    private HandleAdapters handleAdapters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        videos = (List<VideoDetailsToPlay>) intent.getSerializableExtra("videos");

        List<IAdapter> adapters = new ArrayList<>();
        adapters.add(new VideoListAdapter(videos, VideosActivity.this));
        handleAdapters = new HandleAdapters(adapters);

        setContentView(R.layout.activity_videos);

        RecyclerView recyclerViewVideoList = findViewById(R.id.recyclerViewVideoList);
        handleAdapters.setAdapter(recyclerViewVideoList, "Video", VideosActivity.this);

    }

}
