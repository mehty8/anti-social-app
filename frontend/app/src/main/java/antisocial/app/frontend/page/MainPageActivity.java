package antisocial.app.frontend.page;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import antisocial.app.frontend.MainActivity;
import antisocial.app.frontend.R;
import antisocial.app.frontend.SharedPreferencesManager;
import antisocial.app.frontend.data.dto.FriendsNamesDto;
import antisocial.app.frontend.data.dto.VideosDto;
import antisocial.app.frontend.data.dto.ResponseMessageDto;
import antisocial.app.frontend.service.ApiClient;
import antisocial.app.frontend.service.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainPageActivity extends AppCompatActivity {
    private SharedPreferencesManager sharedPreferencesManager;
    private Set<String> friendsNames;
    private Set<String> friendRequests;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        sharedPreferencesManager = new SharedPreferencesManager(getApplicationContext());
        friendsNames = (Set<String>) intent.getSerializableExtra("friends");
        friendRequests = (Set<String>) intent.getSerializableExtra("requests");
        setContentView(R.layout.activity_main_page);
        LinearLayout linearLayoutRequests = findViewById(R.id.friendRequest);
        if(friendRequests.isEmpty()){
            setNoRequests(linearLayoutRequests);
        } else {
            loadRequests(linearLayoutRequests);
        }
        LinearLayout linearLayoutFriends = findViewById(R.id.containerLayout);
        loadFriends(linearLayoutFriends);

        Button buttonSentVideos = findViewById(R.id.buttonSentVideos);

        buttonSentVideos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getVideosActivity("sent");
            }
        });

        Button buttonReceivedVideos = findViewById(R.id.buttonReceivedVideos);

        buttonReceivedVideos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getVideosActivity("received");
            }
        });

        EditText editTexFriendName = findViewById(R.id.editTextFriendName);
        Button buttonFriendSearch = findViewById(R.id.buttonFindFriend);

        buttonFriendSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String friendName = editTexFriendName.getText().toString();
                ApiService apiService = ApiClient.getApiServiceDynamic();
                Call<FriendsNamesDto> call = apiService.getFriendsNames(friendName,"Bearer " + sharedPreferencesManager.getJwt());
                call.enqueue(new Callback<FriendsNamesDto>() {
                    @Override
                    public void onResponse(Call<FriendsNamesDto> call, Response<FriendsNamesDto> response) {
                        Set<String> friendRequestNames = response.body().getFriendsNames();
                        if(friendRequestNames.isEmpty()){
                            Toast.makeText(MainPageActivity.this, "No such user found", Toast.LENGTH_LONG).show();
                        } else {
                            Intent intentFriendRequest = new Intent(MainPageActivity.this, FriendRequestActivity.class);
                            intentFriendRequest.putExtra("friendRequestNames", new HashSet<>(friendRequestNames));
                            startActivity(intentFriendRequest);
                        }
                    }

                    @Override
                    public void onFailure(Call<FriendsNamesDto> call, Throwable t) {
                        Toast.makeText(MainPageActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

    }
    private void loadFriends(LinearLayout linearLayout){
        friendsNames.forEach(friendsName -> {
            TextView textView = new TextView(this);
            textView.setText(friendsName);
            textView.setBackgroundResource(R.drawable.rectangle_curvy_background);
            textView.setPadding(16, 16, 16, 16);
            textView.setTag(friendsName);


            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String friendsName = (String) view.getTag();
                    Intent intent = new Intent(MainPageActivity.this, VideoRecordActivity.class);
                    intent.putExtra("username", friendsName);
                    startActivity(intent);
                }
            });

            linearLayout.addView(textView);

            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) textView.getLayoutParams();
            layoutParams.setMargins(0, 0, 0, 16);
            textView.setLayoutParams(layoutParams);

        });
    }

    private void loadRequests(LinearLayout linearLayout){
        friendRequests.forEach(requestsName -> {
            TextView textView = new TextView(this);
            textView.setText(requestsName);
            textView.setBackgroundResource(R.drawable.rectangle_curvy_background);
            textView.setPadding(16, 16, 16, 16);
            textView.setTag(requestsName);

            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String requestsName = (String) view.getTag();
                    ApiService apiService = ApiClient.getApiServiceDynamic();
                    Call<ResponseMessageDto> call = apiService.sendOrAcceptFriendRequest("acceptrequest", requestsName, "Bearer " + sharedPreferencesManager.getJwt());
                    call.enqueue(new Callback<ResponseMessageDto>() {
                        @Override
                        public void onResponse(Call<ResponseMessageDto> call, Response<ResponseMessageDto> response) {
                            Toast.makeText(MainPageActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(MainPageActivity.this, MainActivity.class);
                            startActivity(intent);
                        }

                        @Override
                        public void onFailure(Call<ResponseMessageDto> call, Throwable t) {
                            Toast.makeText(MainPageActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });
            linearLayout.addView(textView);

            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) textView.getLayoutParams();
            layoutParams.setMargins(0, 0, 0, 16);
            textView.setLayoutParams(layoutParams);
        });
    }

    private void getVideosActivity(String type){
        ApiService apiService = ApiClient.getApiServiceDynamic();
        Call<VideosDto> call = apiService.getVideos(type, "Bearer " + sharedPreferencesManager.getJwt());
        call.enqueue(new Callback<VideosDto>() {
            @Override
            public void onResponse(Call<VideosDto> call, Response<VideosDto> response) {
                VideosDto videos = response.body();
                Intent intentVideos = new Intent(MainPageActivity.this, VideosActivity.class);
                intentVideos.putExtra("videos", new ArrayList<>(videos.getVideoDetailsToPlay()));
                intentVideos.putExtra("type", type);
                startActivity(intentVideos);
            }

            @Override
            public void onFailure(Call<VideosDto> call, Throwable t) {
                Toast.makeText(MainPageActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setNoRequests(LinearLayout linearLayout){
        TextView textView = new TextView(this);
        textView.setText("No Requests");
        textView.setPadding(16, 16, 16, 16);
        linearLayout.addView(textView);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) textView.getLayoutParams();
        layoutParams.setMargins(0, 0, 0, 16);
        textView.setLayoutParams(layoutParams);
    }
}
