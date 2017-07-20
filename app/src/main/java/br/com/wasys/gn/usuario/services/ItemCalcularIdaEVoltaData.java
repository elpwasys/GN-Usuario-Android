package br.com.wasys.gn.usuario.services;

import com.google.gson.annotations.SerializedName;

/**
 * Created by fernandamoncores on 4/14/16.
 */
public class ItemCalcularIdaEVoltaData {

    @SerializedName("origem")
    private String origem;
    @SerializedName("dataHora")
    private String dataHora;
    @SerializedName("destino")
    private String destino;
    @SerializedName("retornoOrigem")
    private Boolean retornarOrigem;
    @SerializedName("retorno")
    private String retorno;
    @SerializedName("pernoiteDestino")
    private boolean pernoiteDestino;
    @SerializedName("distancia")
    private double distancia;
    @SerializedName("valor")
    private double valor;

    public ItemCalcularIdaEVoltaData(String origem, String dataHora, String destino, boolean retornarOrigem, String retorno, boolean pernoiteDestino, double distancia, double valor)
    {
        setOrigem(origem);
        setDataHora(dataHora);
        setDestino(destino);
        setRetornarOrigem(retornarOrigem);
        setRetorno(retorno);
        setPernoiteDestino(pernoiteDestino);
        setDistancia(distancia);
        setValor(valor);
    }

    public String getOrigem() {
        return origem;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }

    public String getDataHora() {
        return dataHora;
    }

    public void setDataHora(String dataHora) {
        this.dataHora = dataHora;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public Boolean getRetornarOrigem() {
        return retornarOrigem;
    }

    public void setRetornarOrigem(Boolean retornarOrigem) {
        this.retornarOrigem = retornarOrigem;
    }

    public String getRetorno() {
        return retorno;
    }

    public void setRetorno(String retorno) {
        this.retorno = retorno;
    }

    public boolean isPernoiteDestino() {
        return pernoiteDestino;
    }

    public void setPernoiteDestino(boolean pernoiteDestino) {
        this.pernoiteDestino = pernoiteDestino;
    }

    public double getDistancia() {
        return distancia;
    }

    public void setDistancia(double distancia) {
        this.distancia = distancia;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }
}
