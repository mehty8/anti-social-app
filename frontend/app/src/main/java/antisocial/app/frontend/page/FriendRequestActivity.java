package antisocial.app.frontend.page;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import antisocial.app.frontend.R;
import antisocial.app.frontend.adapter.IAdapter;
import antisocial.app.frontend.adapter.UserFinderAdapter;
import antisocial.app.frontend.service.HandleAdapters;


public class FriendRequestActivity extends AppCompatActivity {
    private Set<String> userNames;
    private HandleAdapters handleAdapters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        userNames = (Set<String>) intent.getSerializableExtra("friendRequestNames");

        List<IAdapter> adapters = new ArrayList<>();
        adapters.add(new UserFinderAdapter(userNames, FriendRequestActivity.this));
        handleAdapters = new HandleAdapters(adapters);

        setContentView(R.layout.activity_friend_request);

        RecyclerView recyclerViewFriendRequest = findViewById(R.id.recyclerViewUserFinder);
        handleAdapters.setAdapter(recyclerViewFriendRequest, "User", FriendRequestActivity.this);
    }
}
