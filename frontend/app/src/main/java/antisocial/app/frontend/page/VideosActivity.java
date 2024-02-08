package antisocial.app.frontend.page;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import antisocial.app.frontend.R;
import antisocial.app.frontend.data.dto.VideoDetailsToPlay;

public class VideosActivity extends AppCompatActivity {
    private List<VideoDetailsToPlay> videos;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        videos = (List<VideoDetailsToPlay>) intent.getSerializableExtra("videos");
        setContentView(R.layout.activity_videos);
        LinearLayout linearLayout = findViewById(R.id.containerLayout);
        loadVideos(linearLayout, intent.getStringExtra("type"));

    }

    private void loadVideos(LinearLayout linearLayout, String type){
        videos.forEach(video -> {
            String videoName = video.getVideoName();
            String toOrFrom = video.getToOrFrom();
            String nameToDisplay = videoName + (type.equals("sent") ? "to " : "from ") + toOrFrom;
            TextView textView = new TextView(this);
            textView.setText(nameToDisplay);
            textView.setBackgroundResource(R.drawable.rectangle_curvy_background);
            textView.setPadding(16, 16, 16, 16);
            textView.setTag(video.getPreassignedUrl());

            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String preassignedUrl = (String) view.getTag();
                    Intent intent = new Intent(VideosActivity.this, VideoPlayActivity.class);
                    intent.putExtra("url", preassignedUrl);
                    startActivity(intent);
                }
            });

            linearLayout.addView(textView);

            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) textView.getLayoutParams();
            layoutParams.setMargins(0, 0, 0, 16);
            textView.setLayoutParams(layoutParams);

        });
    }
}
