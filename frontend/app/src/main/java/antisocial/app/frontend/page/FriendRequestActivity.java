package antisocial.app.frontend.page;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import antisocial.app.frontend.MainActivity;
import antisocial.app.frontend.R;
import antisocial.app.frontend.SharedPreferencesManager;
import antisocial.app.frontend.dto.ResponseMessageDto;
import antisocial.app.frontend.service.ApiClient;
import antisocial.app.frontend.service.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FriendRequestActivity extends AppCompatActivity {
    private SharedPreferencesManager sharedPreferencesManager;
    private List<String> friendRequestNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        sharedPreferencesManager = new SharedPreferencesManager(getApplicationContext());
        friendRequestNames = (List<String>) intent.getSerializableExtra("friendRequestNames");
        setContentView(R.layout.activity_friend_request);
        LinearLayout linearLayout = findViewById(R.id.containerLayout);
        loadFriendRequestNames(linearLayout);
    }

    private void loadFriendRequestNames(LinearLayout linearLayout){
        friendRequestNames.forEach(friendsName -> {
            TextView textView = new TextView(this);
            textView.setText(friendsName);
            textView.setBackgroundResource(R.drawable.rectangle_curvy_background);
            textView.setPadding(16, 16, 16, 16);
            textView.setTag(friendsName);


            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String friendsName = (String) view.getTag();
                    ApiService apiService = ApiClient.getApiServiceDynamic();
                    Call<ResponseMessageDto> call = apiService.sendOrAcceptFriendRequest("friendrequest", friendsName,"Bearer " + sharedPreferencesManager.getJwt());
                    call.enqueue(new Callback<ResponseMessageDto>() {
                        @Override
                        public void onResponse(Call<ResponseMessageDto> call, Response<ResponseMessageDto> response) {
                            Toast.makeText(FriendRequestActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(FriendRequestActivity.this, MainActivity.class);
                            startActivity(intent);
                        }

                        @Override
                        public void onFailure(Call<ResponseMessageDto> call, Throwable t) {
                            Toast.makeText(FriendRequestActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                    Intent intent = new Intent(FriendRequestActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            });

            linearLayout.addView(textView);

            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) textView.getLayoutParams();
            layoutParams.setMargins(0, 0, 0, 16);
            textView.setLayoutParams(layoutParams);

        });
    }
}
