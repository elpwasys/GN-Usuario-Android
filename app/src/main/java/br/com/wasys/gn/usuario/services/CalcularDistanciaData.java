package br.com.wasys.gn.usuario.services;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


/**
 * Created by fernandamoncores on 6/22/16.
 */
public class CalcularDistanciaData implements Serializable {

    @SerializedName("origem")
    private String origem;

    @SerializedName("destino")
    private String destino;

    public CalcularDistanciaData(String origem, String destino)
    {
        this.origem = origem;
        this.destino = destino;

    }
}
