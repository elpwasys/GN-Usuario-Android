package br.com.wasys.gn.usuario.services;

/**
 * Created by fernandamoncores on 4/18/16.
 */

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface DirectionsService {

    public static final String BASE_URL = "http://maps.googleapis.com/";

    @GET("/maps/api/directions/json")
    public Call<ResponseBody> getJson(@Query("origin") String origin, @Query("destination") String destination);
}
