package antisocial.app.frontend.page;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Set;

import antisocial.app.frontend.R;
import antisocial.app.frontend.SharedPreferencesManager;
import antisocial.app.frontend.adapter.UserFinderAdapter;


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
