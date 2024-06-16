package antisocial.app.frontend.service;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;

import antisocial.app.frontend.data.dto.ResponseMessageDto;
import retrofit2.Response;

public class HandleResponseFailure {

    public static void responseError(Response<?> response, Context context, String additionalMessage){
        try {
            ResponseMessageDto responseBody = new Gson().fromJson(response.errorBody().string(),
                    ResponseMessageDto.class);

            String errorMessage = responseBody.getMessage() + (!additionalMessage.isEmpty() ? additionalMessage : "");

            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();
        } catch (IOException exception) {

            throw new RuntimeException(exception);
        }
    }
}
