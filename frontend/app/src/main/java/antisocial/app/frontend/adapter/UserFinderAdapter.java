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

public class UserFinderAdapter extends RecyclerView.Adapter<UserFinderViewHolder> implements IAdapter{

    private List<String> userNames;
    private LayoutInflater layoutInflater;

    public UserFinderAdapter(Set<String> userNames, Context context) {
        this.userNames = new ArrayList<>(userNames);
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public boolean isNeeded(String type) {
        return type.equals("User");
    }

    @NonNull
    @Override
    public UserFinderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_user_list, parent, false);
        return new UserFinderViewHolder(view, layoutInflater.getContext());
    }

    @Override
    public void onBindViewHolder(@NonNull UserFinderViewHolder holder, int position) {
        String friendName = userNames.get(position);
        holder.bind(friendName);
    }

    @Override
    public int getItemCount() {
        return userNames.size();
    }
}
