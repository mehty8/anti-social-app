package antisocial.app.frontend.page;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import antisocial.app.frontend.MainActivity;
import antisocial.app.frontend.R;
import antisocial.app.frontend.SharedPreferencesManager;
import antisocial.app.frontend.service.ToastCallBack;
import antisocial.app.frontend.service.VideoUploadService;

public class VideoUploadActivity extends AppCompatActivity {
    private SharedPreferencesManager sharedPreferencesManager;
    private VideoUploadService videoUploadService;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_upload);
        videoUploadService = new VideoUploadService(this);
        sharedPreferencesManager = new SharedPreferencesManager(getApplicationContext());
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        String temporaryVideoName = intent.getStringExtra("videoName");
        dialog(temporaryVideoName, username);
    }

    private void dialog(String temporaryVideoName, String username){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater layoutInflater = getLayoutInflater();
        View dialogView = layoutInflater.inflate(R.layout.dialog_name_video, null);
        builder.setView(dialogView);

        EditText editText = dialogView.findViewById(R.id.editTextVideoName);
        Button buttonSend = dialogView.findViewById(R.id.buttonSend);
        Button buttonCancel = dialogView.findViewById(R.id.buttonCancel);

        AlertDialog dialog = builder.create();

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String videoName = editText.getText().toString();
                videoUploadService.videoUpload(sharedPreferencesManager.getJwt(), videoName, username,
                        temporaryVideoName + ".mp4", new ToastCallBack() {
                            @Override
                            public void displayToast(String message) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(VideoUploadActivity.this, message, Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(VideoUploadActivity.this, MainActivity.class);
                                        startActivity(intent);
                                    }
                                });
                            }
                        });
                dialog.dismiss();
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VideoUploadActivity.this, MainActivity.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
