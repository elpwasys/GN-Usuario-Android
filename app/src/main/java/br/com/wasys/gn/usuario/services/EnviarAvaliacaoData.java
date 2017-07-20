package br.com.wasys.gn.usuario.services;

import com.google.gson.annotations.SerializedName;

/**
 * Created by fernandamoncores on 5/11/16.
 */
public class EnviarAvaliacaoData {

    @SerializedName("colaborador")
    private String colaborador;

    @SerializedName("nota")
    private String nota;

    @SerializedName("comentario")
    private String comentario;

    @SerializedName("solicitacao_id")
    private String solicitacao_id;

    public String getSolicitacao_id() {
        return solicitacao_id;
    }

    public void setSolicitacao_id(String solicitacao_id) {
        this.solicitacao_id = solicitacao_id;
    }

    public String getColaborador() {
        return colaborador;
    }

    public void setColaborador(String colaborador) {
        this.colaborador = colaborador;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
}
