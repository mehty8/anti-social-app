package antisocial.app.frontend.adapter;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
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
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        textView.setTextColor(ContextCompat.getColor(context, R.color.black));
        textView.setPadding(16, 16, 16, 16);
        textView.setTag(friendName);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String friendsName = (String) view.getTag();
                Intent intent = new Intent(context, VideoRecordActivity.class);
                intent.putExtra("username", friendsName);
                context.startActivity(intent);
            }
        });
    }
}
