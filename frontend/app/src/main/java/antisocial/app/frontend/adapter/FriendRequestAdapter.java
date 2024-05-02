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

public class FriendRequestAdapter extends RecyclerView.Adapter<FriendRequestViewHolder> implements IAdapter{

    private List<String> friendRequests;
    private LayoutInflater layoutInflater;

    public FriendRequestAdapter(Set<String> friendRequests, Context context) {
        this.friendRequests = new ArrayList<>(friendRequests);
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public boolean isNeeded(String type) {
        return type.equals("Request");
    }

    @NonNull
    @Override
    public FriendRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_friend_request, parent, false);
        return new FriendRequestViewHolder(view, layoutInflater.getContext());
    }

    @Override
    public void onBindViewHolder(@NonNull FriendRequestViewHolder holder, int position) {
        String requestName = friendRequests.get(position);
        holder.bind(requestName);
    }

    @Override
    public int getItemCount() {
        return friendRequests.size();
    }
}
