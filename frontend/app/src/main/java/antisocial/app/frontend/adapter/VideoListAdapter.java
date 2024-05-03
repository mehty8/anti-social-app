package antisocial.app.frontend.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import antisocial.app.frontend.R;
import antisocial.app.frontend.data.dto.VideoDetailsToPlay;

public class VideoListAdapter extends RecyclerView.Adapter<VideoListViewHolder> implements IAdapter {

    private List<VideoDetailsToPlay> videos;
    private LayoutInflater layoutInflater;

    public VideoListAdapter(List<VideoDetailsToPlay> videos, Context context) {
        this.videos = videos;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public boolean isNeeded(String type) {
        return type.equals("Video");
    }

    @NonNull
    @Override
    public VideoListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_video_list, parent, false);
        return new VideoListViewHolder(view, layoutInflater.getContext());
    }

    @Override
    public void onBindViewHolder(@NonNull VideoListViewHolder holder, int position) {
        VideoDetailsToPlay video = videos.get(position);
        holder.bind(video);
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }
}
