package antisocial.app.frontend.adapter;

import static antisocial.app.frontend.service.CheckJwtExpiration.jwtExpired;

import android.content.Context;
import android.content.Intent;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;

import antisocial.app.frontend.R;
import antisocial.app.frontend.SharedPreferencesManager;
import antisocial.app.frontend.page.VideoRecordActivity;

public class FriendListViewHolder extends RecyclerView.ViewHolder{

    private TextView textView;
    private Context context;
    private SharedPreferencesManager sharedPreferencesManager;

    public FriendListViewHolder(@NonNull View itemView, Context context) {
        super(itemView);
        this.textView = itemView.findViewById(R.id.textViewFriendListItem);
        this.context = context;
        this.sharedPreferencesManager = new SharedPreferencesManager(context.getApplicationContext());
    }

    public void bind(String friendName){
        textView.setText(friendName);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        textView.setPadding(16, 16, 16, 16);
        textView.setTag(friendName);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(jwtExpired(sharedPreferencesManager.getJwt(), -60)){
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        LayoutInflater layoutInflater = LayoutInflater.from(context);
                        View dialogView = layoutInflater.inflate(R.layout.dialog_logout, null);
                        builder.setView(dialogView);

                        AlertDialog dialog = builder.create();

                        dialog.show();
                    } else {
                        Intent intent = new Intent(context, VideoRecordActivity.class);
                        intent.putExtra("username", friendName);
                        context.startActivity(intent);
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
