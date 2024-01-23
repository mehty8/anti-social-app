package antisocial.app.frontend.service;

import antisocial.app.frontend.dto.FriendsNamesAndRequestsDTo;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface ApiService {

    @GET("friends")//changed to friend
    Call<FriendsNamesAndRequestsDTo> getFriendsNamesAndRequests(@Header("Authorization") String jwt);
}
