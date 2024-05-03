package antisocial.app.frontend.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import antisocial.app.frontend.R;
import antisocial.app.frontend.data.dto.VideoDetailsToPlay;
import antisocial.app.frontend.page.VideoPlayActivity;

public class VideoListViewHolder extends RecyclerView.ViewHolder {

    private TextView textView;
    private Context context;

    public VideoListViewHolder(@NonNull View itemView, Context context) {
        super(itemView);
        this.textView = itemView.findViewById(R.id.textViewVideoListItem);
        this.context = context;
    }

    public void bind(VideoDetailsToPlay video){
        String preAssignedUrl = video.getPreassignedUrl();
        String videoName = video.getVideoName();
        String toOrFrom = video.getToOrFrom();
        String nameToDisplay = toOrFrom + ": " + videoName;
        textView.setText(nameToDisplay);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        textView.setTextColor(ContextCompat.getColor(context, R.color.black));
        textView.setPadding(16, 16, 16, 16);
        textView.setTag(preAssignedUrl);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String preassignedUrl = (String) view.getTag();
                Intent intent = new Intent(context, VideoPlayActivity.class);
                intent.putExtra("url", preassignedUrl);
                context.startActivity(intent);
            }
        });
    }
}
