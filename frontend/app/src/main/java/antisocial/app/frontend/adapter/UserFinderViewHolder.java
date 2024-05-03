package antisocial.app.frontend.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import antisocial.app.frontend.MainActivity;
import antisocial.app.frontend.R;
import antisocial.app.frontend.SharedPreferencesManager;
import antisocial.app.frontend.data.dto.ResponseMessageDto;
import antisocial.app.frontend.service.ApiClient;
import antisocial.app.frontend.service.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserFinderViewHolder extends RecyclerView.ViewHolder{

    private TextView textView;
    private Context context;
    private SharedPreferencesManager sharedPreferencesManager;

    public UserFinderViewHolder(@NonNull View itemView, Context context) {
        super(itemView);
        this.textView = itemView.findViewById(R.id.textViewUserListItem);
        this.context = context;
        sharedPreferencesManager = new SharedPreferencesManager(context.getApplicationContext());
    }

    public void bind(String requestName){
        textView.setText(requestName);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        textView.setTextColor(ContextCompat.getColor(context, R.color.black));
        textView.setPadding(16, 16, 16, 16);
        textView.setTag(requestName);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String friendsName = (String) view.getTag();
                ApiService apiService = ApiClient.getApiServiceDynamic();
                Call<ResponseMessageDto> call = apiService.sendOrHandleFriendRequest("friendrequest", friendsName,"Bearer " + sharedPreferencesManager.getJwt());
                call.enqueue(new Callback<ResponseMessageDto>() {
                    @Override
                    public void onResponse(Call<ResponseMessageDto> call, Response<ResponseMessageDto> response) {
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(context, MainActivity.class);
                        context.startActivity(intent);
                    }

                    @Override
                    public void onFailure(Call<ResponseMessageDto> call, Throwable t) {
                        Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}
