package br.com.wasys.gn.usuario.endpoint;

import br.com.wasys.gn.usuario.models.Solicitacao;

import br.com.elp.library.http.Result;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by pascke on 21/08/16.
 */
public interface SolicitacaoEndpoint {

    @POST("solicitacao/salvar")
    Call<Result> salvar(@Body Solicitacao solicitacao);
}
