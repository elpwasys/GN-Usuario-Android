package br.com.wasys.gn.usuario.services;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by fernandamoncores on 5/10/16.
 */
public class CalcularTransladoData implements Serializable {

    @SerializedName("colaborador")
    private String colaborador;

    @SerializedName("servico")
    private String servico;

    @SerializedName("origem")
    private String origem;

    @SerializedName("tipoCarro")
    private String tipoCarro;

    @SerializedName("hora")
    private String hora;

    @SerializedName("data")
    private String data;

    @SerializedName("snapshot")
    private String snapshot;

    @SerializedName("destino")
    public String destino;

    @SerializedName("distancia")
    public String distancia;

    @SerializedName("valor")
    public String valor;

    @SerializedName("duracao")
    public String duracao;

    @SerializedName("empresa")
    public String empresa;

    @SerializedName("cidade_origem")
    public String cidade_origem;

    public String getCidade_origem() {
        return cidade_origem;
    }

    public void setCidade_origem(String cidade_origem) {
        this.cidade_origem = cidade_origem;
    }

    @SerializedName("centro_de_custo")
    public String centro_de_custo;

    public String getColaborador() {
        return colaborador;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getCentro_de_custo() {
        return centro_de_custo;
    }

    public void setCentro_de_custo(String centro_de_custo) {
        this.centro_de_custo = centro_de_custo;
    }

    public void setColaborador(String colaborador) {
        this.colaborador = colaborador;
    }

    public String getServico() {
        return servico;
    }

    public void setServico(String servico) {
        this.servico = servico;
    }

    public String getOrigem() {
        return origem;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }

    public String getTipoCarro() {
        return tipoCarro;
    }

    public void setTipoCarro(String tipoCarro) {
        this.tipoCarro = tipoCarro;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public String getDistancia() {
        return distancia;
    }

    public void setDistancia(String distancia) {
        this.distancia = distancia;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getDuracao() {
        return duracao;
    }

    public void setDuracao(String duracao) {
        this.duracao = duracao;
    }

    public String getSnapshot() {
        return snapshot;
    }

    public void setSnapshot(String snapshot) {
        this.snapshot = snapshot;
    }

    //{"status":"SUCCESS","result":{"distancia":396,"valor":0.01,"duracao":"5:05"}}

    public CalcularTransladoData(String colaborador, String servico, String origem, String tipo_carro, String hora, String data)
    {

        this.colaborador = colaborador;
        this.servico = servico;
        this.origem = origem;
        this.tipoCarro = tipo_carro;
        this.hora = hora;
        this.data = data;
    }
}
