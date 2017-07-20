package br.com.wasys.gn.usuario.endpoint;

import br.com.wasys.gn.usuario.result.RotaResult;
import br.com.wasys.gn.usuario.models.Solicitacao;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by pascke on 18/08/16.
 */
public interface RotaEndpoint {

    @POST("rota/calcular")
    Call<RotaResult> calcular(@Body Solicitacao solicitacao);
}
