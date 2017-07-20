package br.com.wasys.gn.usuario.endpoint;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by pascke on 06/09/16.
 */
public interface MotoristaEndpoint {

    @GET("motorista/download/foto/{id}")
    Call<ResponseBody> downloadFoto(@Path("id") Long id);
}
