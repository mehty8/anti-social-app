package antisocial.app.frontend.page;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Set;
import java.util.stream.Collectors;

import antisocial.app.frontend.MainActivity;
import antisocial.app.frontend.R;
import antisocial.app.frontend.SharedPreferencesManager;
import antisocial.app.frontend.adapter.IAdapter;
import antisocial.app.frontend.adapter.UserFinderAdapter;
import antisocial.app.frontend.data.dto.ResponseMessageDto;
import antisocial.app.frontend.service.ApiClient;
import antisocial.app.frontend.service.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FriendRequestActivity extends AppCompatActivity {
    private SharedPreferencesManager sharedPreferencesManager;
    private Set<String> userNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        sharedPreferencesManager = new SharedPreferencesManager(getApplicationContext());
        userNames = (Set<String>) intent.getSerializableExtra("friendRequestNames");
        setContentView(R.layout.activity_friend_request);

        RecyclerView recyclerViewFriendRequest = findViewById(R.id.recyclerViewUserFinder);
        recyclerViewFriendRequest.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.Adapter userFinderAdapter = new UserFinderAdapter(userNames, FriendRequestActivity.this);
        recyclerViewFriendRequest.setAdapter(userFinderAdapter);
    }
}
