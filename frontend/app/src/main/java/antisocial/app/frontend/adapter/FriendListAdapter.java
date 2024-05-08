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

public class FriendListAdapter extends RecyclerView.Adapter<FriendListViewHolder> implements IAdapter{

    private List<String> friendNames;
    private LayoutInflater layoutInflater;

    public FriendListAdapter(Set<String> friendNames, Context context) {
        this.friendNames = new ArrayList<>(friendNames);
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public boolean isNeeded(String type) {
        return type.equals("Friend");
    }

    @NonNull
    @Override
    public FriendListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_friend_list, parent, false);
        return new FriendListViewHolder(view, layoutInflater.getContext());
    }

    @Override
    public void onBindViewHolder(@NonNull FriendListViewHolder holder, int position) {
        String friendName = friendNames.get(position);
        holder.bind(friendName);
    }

    @Override
    public int getItemCount() {
        return friendNames.size();
    }
}
