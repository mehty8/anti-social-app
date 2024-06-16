package antisocial.app.frontend.adapter;

import static antisocial.app.frontend.service.HandleResponseFailure.responseError;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import antisocial.app.frontend.MainActivity;
import antisocial.app.frontend.R;
import antisocial.app.frontend.SharedPreferencesManager;
import antisocial.app.frontend.data.dto.ResponseMessageDto;
import antisocial.app.frontend.service.api.ApiClient;
import antisocial.app.frontend.service.api.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FriendRequestViewHolder extends RecyclerView.ViewHolder {
    private TextView textView;
    private Context context;
    private SharedPreferencesManager sharedPreferencesManager;

    public FriendRequestViewHolder(@NonNull View itemView, Context context) {
        super(itemView);
        this.textView = itemView.findViewById(R.id.textViewFriendRequestItem);
        this.context = context;
        sharedPreferencesManager = new SharedPreferencesManager(context.getApplicationContext());

        MaterialButton acceptButton = itemView.findViewById(R.id.buttonAccept);
        MaterialButton denyButton = itemView.findViewById(R.id.buttonDeny);

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleFriendRequest("acceptrequest", textView.getTag().toString());
            }
        });

        denyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleFriendRequest("denyrequest", textView.getTag().toString());
            }
        });
    }

    public void bind(String requestName){
        textView.setText(requestName);
        textView.setTag(requestName);
    }

    private void handleFriendRequest(String type, String requestsName){
        ApiService apiService = ApiClient.getApiServiceDynamic();
        Call<ResponseMessageDto> call = apiService.sendOrHandleFriendRequest(type, requestsName,
                "Bearer " + sharedPreferencesManager.getJwt());
        call.enqueue(new Callback<ResponseMessageDto>() {
            @Override
            public void onResponse(Call<ResponseMessageDto> call, Response<ResponseMessageDto> response) {
                if(response.isSuccessful()){
                    Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(context, MainActivity.class);
                    context.startActivity(intent);
                } else {
                    responseError(response, context, "");
                }
            }

            @Override
            public void onFailure(Call<ResponseMessageDto> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
