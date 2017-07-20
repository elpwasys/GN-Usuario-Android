package br.com.wasys.gn.usuario.services;

import br.com.wasys.gn.usuario.helpers.Helper;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by fernandamoncores on 5/10/16.
 */
public interface CriarTransladoService {

    public static final String BASE_URL = Helper.BASE_URL+"solicitacao/";


    @Headers({
            "deviceSO: 1",
            "deviceIMEI: 1",
            "deviceModel: 1",
            "deviceWidth: 1",
            "deviceHeight: 1",
            "deviceSOVersion: 1",
            "deviceAppVersion: 1",
            "userID: 1",
            "Content-Type: application/json"
    })

    @POST("criar_servico")
    Call<ResponseBody> criar_translado(@Body CalcularTransladoData calcularTransladoData);
}
