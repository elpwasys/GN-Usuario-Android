package br.com.wasys.gn.usuario.models;

import java.io.Serializable;

/**
 * Created by pascke on 08/08/16.
 */
public class Endereco implements Serializable {

    public String cidade;
    public String completo;
    public Double latitude;
    public Double longitude;

    public Endereco() {
    }

    public Endereco(String completo) {
        this.completo = completo;
    }

    public Endereco(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Endereco(String completo, Double latitude, Double longitude) {
        this(latitude, longitude);
        this.completo = completo;
    }

    public Endereco(String cidade, String completo, Double latitude, Double longitude) {
        this(completo, latitude, longitude);
        this.cidade = cidade;
    }
}