package br.com.wasys.gn.usuario.services;

import com.google.gson.annotations.SerializedName;

/**
 * Created by fernandamoncores on 4/14/16.
 */
public class CalcularIdaEVoltaData {

    @SerializedName("tipo")
    private String tipo;

    @SerializedName("tipoCarro")
    private String tipoCarro;

    @SerializedName("observacao")
    private String observacoes;

    @SerializedName("colaborador")
    private String colaborador_id;

    @SerializedName("origem")
    private String origem;

    @SerializedName("destino")
    private String destino;

    @SerializedName("retorno")
    private String retorno;

    @SerializedName("kms")
    private String kms;

    @SerializedName("pernoite_destino")
    private boolean pernoite_destino;

    @SerializedName("cidade_origem")
    private String cidade_origem;

    @SerializedName("cidade_destino")
    private String cidade_destino;

    public String isCidade_origem() {
        return cidade_origem;
    }

    public void setCidade_origem(String cidade_origem) {
        this.cidade_origem = cidade_origem;
    }

    public String isCidade_destino() {
        return cidade_destino;
    }

    public String getRetorno() {
        return retorno;
    }

    public void setRetorno(String retorno) {
        this.retorno = retorno;
    }

    public void setCidade_destino(String cidade_destino) {
        this.cidade_destino = cidade_destino;
    }

    public CalcularIdaEVoltaData(String tipo, String tipoCarro, String colaborador_id, String origem, String destino, boolean pernoite_destino, String cidade_origem, String cidade_destino, String retorno, String observacao) {

        setTipo(tipo);
        setTipoCarro(tipoCarro);
        setColaborador_id(colaborador_id);
        setOrigem(origem);
        setDestino(destino);
        setPernoite_destino(pernoite_destino);
        setCidade_origem(cidade_origem);
        setCidade_destino(cidade_destino);
        setRetorno(retorno);
        setObservacoes(observacao);

    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getTipoCarro() {
        return tipoCarro;
    }

    public void setTipoCarro(String tipoCarro) {
        this.tipoCarro = tipoCarro;
    }

    public String getColaborador_id() {
        return colaborador_id;
    }

    public void setColaborador_id(String colaborador_id) {
        this.colaborador_id = colaborador_id;
    }

    public String getOrigem() {
        return origem;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }

    public String getKms() {
        return kms;
    }

    public void setKms(String kms) {
        this.kms = kms;
    }

    public boolean isPernoite_destino() {
        return pernoite_destino;
    }

    public void setPernoite_destino(boolean pernoite_destino) {
        this.pernoite_destino = pernoite_destino;
    }

}
