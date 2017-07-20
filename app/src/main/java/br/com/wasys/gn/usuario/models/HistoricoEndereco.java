package br.com.wasys.gn.usuario.models;

/**
 * Created by fernandamoncores on 5/5/16.
 */
public class HistoricoEndereco {

    private String id;
    private String endereco;
    private String cidade;
    private String tipo;


    public HistoricoEndereco(String id,String endereco, String cidade, String tipo)
    {
        this.id = id;
        this.endereco = endereco;
        this.cidade = cidade;
        this.tipo = tipo;

    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }
}
