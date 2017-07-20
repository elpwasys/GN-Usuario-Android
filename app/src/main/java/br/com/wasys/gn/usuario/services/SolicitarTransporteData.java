package br.com.wasys.gn.usuario.services;

import com.google.gson.annotations.SerializedName;

/**
 * Created by fernandamoncores on 4/16/16.
 */
public class SolicitarTransporteData {

    @SerializedName("origem")
    private String origem;

    @SerializedName("tipo")
    private String tipo;

    @SerializedName("tipoCarro")
    private String tipoCarro;

    @SerializedName("observacoes")
    private String observacoes;

    @SerializedName("destino")
    private String destino;

    @SerializedName("retornar_origem")
    private boolean retornar_origem;

    @SerializedName("retorno")
    private String retorno;

    @SerializedName("pernoite_destino")
    private boolean pernoite_destino;

    @SerializedName("distancia")
    private String distancia;

    @SerializedName("valor")
    private String valor;

    @SerializedName("colaborador")
    private String colaborador;

    @SerializedName("data_hora")
    private String data_hora;

    @SerializedName("cidade_origem")
    private String cidade_origem;

    @SerializedName("cidade_destino")
    private String cidade_destino;

    @SerializedName("cidade_retorno")
    private String cidade_retorno;

    @SerializedName("empresa")
    private String empresa;

    @SerializedName("centro_de_custo")
    private String centro_de_custo;

    @SerializedName("duracao")
    private String duracao;

    @SerializedName("snapshot")
    private String snapshot;

    public SolicitarTransporteData(String tipo,String tipoCarro,String observacoes, String origem, String destino, boolean retornar_origem, String retorno, boolean pernoite_destino, String distancia, String valor,String colaborador, String data_hora, String cidade_origem, String cidade_destino, String cidade_retorno, String empresa, String centro_de_custo, String duracao, String snapshot)
    {
        this.tipo = tipo;
        this.tipoCarro = tipoCarro;
        this.observacoes = observacoes;
        this.origem = origem;
        this.destino = destino;
        this.retornar_origem = retornar_origem;
        this.retorno = retorno;
        this.pernoite_destino = pernoite_destino;
        this.distancia = distancia;
        this.valor = valor;
        this.colaborador = colaborador;
        this.data_hora = data_hora;
        this.cidade_origem = cidade_origem;
        this.cidade_destino = cidade_destino;
        this.cidade_retorno = cidade_retorno;
        this.empresa = empresa;
        this.centro_de_custo = centro_de_custo;
        this.duracao = duracao;
        this.snapshot = snapshot;
    }









}
