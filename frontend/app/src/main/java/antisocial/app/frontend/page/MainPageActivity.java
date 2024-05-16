package antisocial.app.frontend.page;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import antisocial.app.frontend.MainActivity;
import antisocial.app.frontend.R;
import antisocial.app.frontend.SharedPreferencesManager;
import antisocial.app.frontend.adapter.FriendListAdapter;
import antisocial.app.frontend.adapter.FriendRequestAdapter;
import antisocial.app.frontend.adapter.IAdapter;
import antisocial.app.frontend.data.dto.FriendsNamesDto;
import antisocial.app.frontend.data.dto.VideosDto;
import antisocial.app.frontend.service.api.ApiClient;
import antisocial.app.frontend.service.api.ApiService;
import antisocial.app.frontend.service.HandleAdapters;
import antisocial.app.frontend.service.HandleResponseFailure;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainPageActivity extends AppCompatActivity {

    private final ActivityResultLauncher<String[]> activityResultLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result ->
            {
                for (String permission : result.keySet()) {
                    if (!result.get(permission)) {
                        Toast.makeText(this, "All permissions needed to record video",
                                Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
            });
    private SharedPreferencesManager sharedPreferencesManager;
    private Set<String> friendsNames;
    private Set<String> friendRequests;
    private List<IAdapter> adapters;
    private HandleResponseFailure handleResponseFailure;
    private HandleAdapters handleAdapters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getVideoPermissions();

        Intent intent = getIntent();
        sharedPreferencesManager = new SharedPreferencesManager(getApplicationContext());
        friendsNames = (Set<String>) intent.getSerializableExtra("friends");
        friendRequests = (Set<String>) intent.getSerializableExtra("requests");

        adapters = new ArrayList<>();
        IAdapter requestAdapter = new FriendRequestAdapter(friendRequests, MainPageActivity.this);
        IAdapter friendAdapter = new FriendListAdapter(friendsNames, MainPageActivity.this);
        adapters.add(requestAdapter);
        adapters.add(friendAdapter);

        handleResponseFailure = new HandleResponseFailure();

        handleAdapters = new HandleAdapters(adapters);


        setContentView(R.layout.activity_main_page);


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
                        if(response.isSuccessful()){
                            if(friendRequestNames.isEmpty()){
                                Toast.makeText(MainPageActivity.this, "No such user found", Toast.LENGTH_LONG).show();
                            } else {
                                Intent intentFriendRequest = new Intent(MainPageActivity.this, FriendRequestActivity.class);
                                intentFriendRequest.putExtra("friendRequestNames", new HashSet<>(friendRequestNames));
                                startActivity(intentFriendRequest);
                            }
                        } else {
                            handleResponseFailure.responseError(response, MainPageActivity.this, "");
                        }
                    }
                    @Override
                    public void onFailure(Call<FriendsNamesDto> call, Throwable t) {
                        Toast.makeText(MainPageActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        RecyclerView recyclerViewFriendRequest = findViewById(R.id.recyclerViewFriendRequests);
        handleAdapters.setAdapter(recyclerViewFriendRequest, "Request", MainPageActivity.this);
        RecyclerView recyclerViewFriendList = findViewById(R.id.recyclerViewFriends);
        handleAdapters.setAdapter(recyclerViewFriendList, "Friend", MainPageActivity.this);

        Button refreshButton = findViewById(R.id.buttonRefresh);

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentMainActivity = new Intent(MainPageActivity.this, MainActivity.class);
                startActivity(intentMainActivity);
            }
        });

    }


    private void getVideoPermissions(){
        activityResultLauncher.launch(new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO
        });
    }

    private void getVideosActivity(String type){
        ApiService apiService = ApiClient.getApiServiceDynamic();
        Call<VideosDto> call = apiService.getVideos(type, "Bearer " + sharedPreferencesManager.getJwt());
        call.enqueue(new Callback<VideosDto>() {
            @Override
            public void onResponse(Call<VideosDto> call, Response<VideosDto> response) {
                if(response.isSuccessful()){
                    VideosDto videos = response.body();
                    Intent intentVideos = new Intent(MainPageActivity.this, VideosActivity.class);
                    intentVideos.putExtra("videos", new ArrayList<>(videos.getVideoDetailsToPlay()));
                    intentVideos.putExtra("type", type);
                    startActivity(intentVideos);
                } else {
                    handleResponseFailure.responseError(response, MainPageActivity.this, "");
                }
            }

            @Override
            public void onFailure(Call<VideosDto> call, Throwable t) {
                Toast.makeText(MainPageActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
