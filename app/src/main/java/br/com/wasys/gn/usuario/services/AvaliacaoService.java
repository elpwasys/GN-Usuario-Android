package br.com.wasys.gn.usuario.services;

import br.com.wasys.gn.usuario.helpers.Helper;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by fernandamoncores on 5/11/16.
 */
public interface AvaliacaoService {

    public static final String BASE_URL = Helper.BASE_URL+"solicitacao/";
    //public static final String BASE_URL = "http://192.168.0.16:8080/m5/mb/tarifa/";
    //public static final String BASE_URL = "http://192.168.1.6:8080/m5/mb/solicitacao/";
    //public static final String BASE_URL = "http://192.168.1.3:8080/m5/mb/solicitacao/";


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

    @POST("get_solicitacao_sem_avaliacao/{id}")
    Call<ResponseBody> getAvaliacao(@Path("id") long id);
}
