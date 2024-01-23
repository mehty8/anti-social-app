package antisocial.app.frontend.service;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private final static String BASE_URL = "http://10.0.2.2:8080/";

    public static ApiService getApiServiceDynamic(){
        return new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
                .build().create(ApiService.class);
    }
}
