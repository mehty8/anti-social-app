package antisocial.app.frontend.service;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

import antisocial.app.frontend.ToastCallBack;
import antisocial.app.frontend.dto.PreassignedUrlToUploadVideo;
import antisocial.app.frontend.dto.PreassignedUrlDetailsDto;
import antisocial.app.frontend.dto.ResponseMessageDto;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoUploadService {

    private static String BUCKET_NAME = "antisocial-video-oneway-sending";
    private static int TIME_IN_MS_TO_UPLOAD = 1000 * 60;
    private static int TIME_IN_MS_TO_GET = 1000 * 60 * 60 * 12;
    private Context context;

    public VideoUploadService(Context context) {
        this.context = context;
    }

    public void videoUpload(String jwt, String videoName, String username, String temporaryVideoName, ToastCallBack toastCallback){
        getPreassignedUrl(jwt, videoName, username, temporaryVideoName, toastCallback);
    }

    private void getPreassignedUrl(String jwt, String videoName, String username, String temporaryVideoName, ToastCallBack toastCallback) {

        PreassignedUrlDetailsDto preassignedUrlDetailsDto = new PreassignedUrlDetailsDto(videoName,
                "PUT", BUCKET_NAME, TIME_IN_MS_TO_UPLOAD);
        ApiService apiService = ApiClient.getApiServiceDynamic();
        Call<PreassignedUrlToUploadVideo> call = apiService.getPreassignedUrlToUploadVideo("Bearer " + jwt, preassignedUrlDetailsDto);
        call.enqueue((new Callback<PreassignedUrlToUploadVideo>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<PreassignedUrlToUploadVideo> call, Response<PreassignedUrlToUploadVideo> response) {
                String url = response.body().getPreassignedUrl();
                uploadVideo(url, videoName, username, jwt, temporaryVideoName, toastCallback);
            }

            @Override
            public void onFailure(Call<PreassignedUrlToUploadVideo> call, Throwable t) {
                //Handle error
            }
        }));
    }

    private void uploadVideo(String url, String videoName, String username, String jwt, String temporaryVideoName, ToastCallBack toastCallBack){
        File videoFilePath = new File(getFilePath(temporaryVideoName));

        ApiService apiService = ApiClient.getApiServiceDynamic();

        RequestBody requestBody = RequestBody.create(MediaType.parse("video/mp4"), videoFilePath);
        Call<Void> call = apiService.uploadVideoToS3Bucket(url, requestBody);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                try {
                    deleteFile(temporaryVideoName);
                } catch (Exception ex){
                    Log.e("videoUploadService", "error occurred" + ex.getMessage() );
                } finally {
                    savePreassignedUrl(videoName, username, jwt, toastCallBack);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
                //Handle error
            }
        });

    }

    private String getFilePath(String videoName) {
        String filePath = "";
        Uri contentUri = getUri(videoName);

        if (contentUri != null) {
            String[] projection = {MediaStore.MediaColumns.DATA};
            try (Cursor cursor = context.getContentResolver().query(contentUri, projection, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                    filePath = cursor.getString(columnIndex);
                } else {
                    // Handle error or log a message
                }
            }
        }

        return filePath;
    }

    private void deleteFile(String videoName){
        Uri contentUri = getUri(videoName);
        if(contentUri != null){
            try{
                int success = context.getContentResolver().delete(contentUri, null, null);
                if(success > 0){
                    Toast.makeText(context, "delete successfull", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, "NOT DELETED", Toast.LENGTH_LONG).show();
                }
            } catch (SecurityException ex){
                Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("VideoDeletProblem", "Error caused by " + ex.getMessage());
                ex.printStackTrace();
            }

        }
    }

    private Uri getUri(String videoName){
        Uri queryUri = MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
        String[] projection = {MediaStore.Video.Media._ID};

        String selection = MediaStore.Video.Media.DISPLAY_NAME + "=?";
        String[] selectionArgs = new String[]{videoName};

        try (Cursor cursor = context.getContentResolver().query(queryUri, projection, selection, selectionArgs,null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int idColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID);
                long videoId = cursor.getLong(idColumnIndex);

                return ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, videoId);
            } else {

                return null;
            }
        }
    }

    private void savePreassignedUrl(String videoName, String username, String jwt, ToastCallBack toastcallback) {

        PreassignedUrlDetailsDto preassignedUrlDetailsDto = new PreassignedUrlDetailsDto(videoName,
                "GET", BUCKET_NAME, TIME_IN_MS_TO_GET);
        ApiService apiService = ApiClient.getApiServiceDynamic();
        Call<ResponseMessageDto> call = apiService.getPreassignedUrlToWatch(username,"Bearer " + jwt, preassignedUrlDetailsDto);
        call.enqueue((new Callback<ResponseMessageDto>() {
            @Override
            public void onResponse(Call<ResponseMessageDto> call, Response<ResponseMessageDto> response) {
                toastcallback.displayToast(response.body().getMessage());
            }

            @Override
            public void onFailure(Call<ResponseMessageDto> call, Throwable t) {
                toastcallback.displayToast(t.getMessage());
            }
        }));
    }
}
