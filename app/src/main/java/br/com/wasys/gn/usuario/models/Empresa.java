package br.com.wasys.gn.usuario.models;

/**
 * Created by fernandamoncores on 4/14/16.
 */
public class Empresa {

    private String id;
    private String nome;
    private String cpnj;

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

    public String getCpnj() {
        return cpnj;
    }

    public void setCpnj(String cpnj) {
        this.cpnj = cpnj;
    }
}
