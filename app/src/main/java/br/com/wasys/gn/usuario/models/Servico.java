package br.com.wasys.gn.usuario.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by fernandamoncores on 5/10/16.
 */
public class Servico {

    @SerializedName("id")
    private String id;
    @SerializedName("nome")
    private String nome;

    public Servico(){}

    public Servico(String id, String nome)
    {
        this.id = id;
        this.nome = nome;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
