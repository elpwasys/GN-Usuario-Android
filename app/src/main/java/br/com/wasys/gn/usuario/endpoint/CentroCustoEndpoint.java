package br.com.wasys.gn.usuario.endpoint;

import br.com.wasys.gn.usuario.models.CentroCustoRequestBody;
import br.com.wasys.gn.usuario.models.CentroCustoListResult;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by pascke on 18/08/16.
 */
public interface CentroCustoEndpoint {

    @POST("centroCusto/listar")
    Call<CentroCustoListResult> listar(@Body CentroCustoRequestBody body);
}
