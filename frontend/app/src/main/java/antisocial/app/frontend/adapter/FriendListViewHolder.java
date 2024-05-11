package antisocial.app.frontend.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import antisocial.app.frontend.R;
import antisocial.app.frontend.page.VideoRecordActivity;

public class FriendListViewHolder extends RecyclerView.ViewHolder{

    private TextView textView;
    private Context context;

    public FriendListViewHolder(@NonNull View itemView, Context context) {
        super(itemView);
        this.textView = itemView.findViewById(R.id.textViewFriendListItem);
        this.context = context;
    }

    public void bind(String friendName){
        textView.setText(friendName);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        textView.setPadding(16, 16, 16, 16);
        textView.setTag(friendName);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, VideoRecordActivity.class);
                intent.putExtra("username", friendName);
                context.startActivity(intent);
            }
        });
    }
}
