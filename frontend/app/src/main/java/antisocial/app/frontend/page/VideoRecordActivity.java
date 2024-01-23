package antisocial.app.frontend.page;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.video.MediaStoreOutputOptions;
import androidx.camera.video.Quality;
import androidx.camera.video.QualitySelector;
import androidx.camera.video.Recorder;
import androidx.camera.video.Recording;
import androidx.camera.video.VideoCapture;
import androidx.camera.video.VideoRecordEvent;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.util.Consumer;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import antisocial.app.frontend.MainActivity;
import antisocial.app.frontend.R;
import antisocial.app.frontend.SharedPreferencesManager;

public class VideoRecordActivity extends AppCompatActivity {
    ImageButton capture;
    ImageButton toggleFlash;
    ImageButton flipCamera;
    ExecutorService executorService;
    Recording recording = null;
    VideoCapture<Recorder> videoCapture = null;
    PreviewView previewView;
    int cameraFacing = CameraSelector.LENS_FACING_BACK;

    private final ActivityResultLauncher<String> activityResultLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(),
                    new ActivityResultCallback<Boolean>() {
                        @Override
                        public void onActivityResult(Boolean o) {
                            if (ActivityCompat.checkSelfPermission(VideoRecordActivity.this, Manifest.permission.CAMERA) ==
                                    PackageManager.PERMISSION_GRANTED) {
                                startCamera(cameraFacing);
                            }
                        }
                    });

    private SharedPreferencesManager sharedPreferencesManager;

    private String username;

    private static String TEMPORARY_VIDEO_NAME = "temporaryVideo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_record);

        capture = findViewById(R.id.capture);
        toggleFlash = findViewById(R.id.toggleFlash);
        flipCamera = findViewById(R.id.flipCamera);
        previewView = findViewById(R.id.preview);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        sharedPreferencesManager = new SharedPreferencesManager(getApplicationContext());

        capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(VideoRecordActivity.this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    activityResultLauncher.launch(Manifest.permission.CAMERA);
                } else if (ActivityCompat.checkSelfPermission(VideoRecordActivity.this, Manifest.permission.RECORD_AUDIO)
                        != PackageManager.PERMISSION_GRANTED) {
                    activityResultLauncher.launch(Manifest.permission.RECORD_AUDIO);
                } else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P && ActivityCompat.checkSelfPermission(VideoRecordActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    activityResultLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                } else {
                    captureVideo();
                }
            }
        });

        flipCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cameraFacing == CameraSelector.LENS_FACING_BACK) {
                    cameraFacing = CameraSelector.LENS_FACING_FRONT;
                } else {
                    cameraFacing = CameraSelector.LENS_FACING_BACK;
                }
                startCamera(cameraFacing);
            }
        });

        if (ActivityCompat.checkSelfPermission(VideoRecordActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            activityResultLauncher.launch(Manifest.permission.CAMERA);
        } else {
            startCamera(cameraFacing);
        }

        executorService = Executors.newSingleThreadExecutor();

    }

    public void startCamera(int cameraFacing) {
        ListenableFuture<ProcessCameraProvider> processCameraProviderListenableFuture =
                ProcessCameraProvider.getInstance(VideoRecordActivity.this);
        processCameraProviderListenableFuture.addListener(new Runnable() {
            @Override
            public void run() {
                try {
                    ProcessCameraProvider provider = processCameraProviderListenableFuture.get();
                    Preview preview = new Preview.Builder().build();
                    preview.setSurfaceProvider(previewView.getSurfaceProvider());
                    Recorder recorder = new Recorder.Builder().setQualitySelector(QualitySelector.from(Quality.HIGHEST))
                            .build();
                    videoCapture = VideoCapture.withOutput(recorder);


                    provider.unbindAll();

                    CameraSelector cameraSelector = new CameraSelector.Builder().requireLensFacing(cameraFacing).build();

                    Camera camera = provider.bindToLifecycle(VideoRecordActivity.this, cameraSelector, preview, videoCapture);

                    toggleFlash.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            toggleFlash(camera);
                        }
                    });

                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, ContextCompat.getMainExecutor(VideoRecordActivity.this));
    }

    private void toggleFlash(Camera camera){
        if(camera.getCameraInfo().hasFlashUnit()){
            if(camera.getCameraInfo().getTorchState().getValue() == 0){
                camera.getCameraControl().enableTorch(true);
                toggleFlash.setImageResource(R.drawable.baseline_flash_off_24);
            } else{
                camera.getCameraControl().enableTorch(false);
                toggleFlash.setImageResource(R.drawable.baseline_flash_on_24);
            }
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(VideoRecordActivity.this, "flash is not available", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        executorService.shutdown();
    }

    private void captureVideo() {
        capture.setImageResource(R.drawable.baseline_stop_circle_24);
        Recording recording1 = recording;
        if (recording1 != null) {
            recording1.stop();
            recording = null;
            return;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, TEMPORARY_VIDEO_NAME);
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4");
        contentValues.put(MediaStore.Video.Media.RELATIVE_PATH, "Movies/CameraX-Recorder");

        MediaStoreOutputOptions options = new MediaStoreOutputOptions.Builder(getContentResolver(),
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI).setContentValues(contentValues).build();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            //activityResultLauncher.launch(Manifest.permission.RECORD_AUDIO);
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        recording = videoCapture.getOutput().prepareRecording(VideoRecordActivity.this, options).withAudioEnabled()
                .start(ContextCompat.getMainExecutor(VideoRecordActivity.this), new Consumer<VideoRecordEvent>() {
                    @Override
                    public void accept(VideoRecordEvent videoRecordEvent) {
                        if(videoRecordEvent instanceof VideoRecordEvent.Start){
                            capture.setImageResource(R.drawable.baseline_stop_circle_24);
                        } else if(videoRecordEvent instanceof VideoRecordEvent.Finalize){
                            if(!((VideoRecordEvent.Finalize) videoRecordEvent).hasError()){
                                String message = "Video Captured successfully";
                                Toast.makeText(VideoRecordActivity.this, message, Toast.LENGTH_LONG).show();
                            } else {
                                recording.close();
                                recording = null;
                                String message = "Error " + ((VideoRecordEvent.Finalize) videoRecordEvent).getError();
                                Toast.makeText(VideoRecordActivity.this, message, Toast.LENGTH_SHORT).show();
                                capture.setImageResource(R.drawable.baseline_fiber_manual_record_24);
                                Intent intent = new Intent(VideoRecordActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                            capture.setImageResource(R.drawable.baseline_fiber_manual_record_24);
                            Intent intent = new Intent(VideoRecordActivity.this, VideoUploadActivity.class);
                            intent.putExtra("username", username);
                            intent.putExtra("videoName", TEMPORARY_VIDEO_NAME);
                            startActivity(intent);
                        }
                    }
                });
    }
}
