package br.com.wasys.gn.usuario.services;

import br.com.wasys.gn.usuario.models.Motorista;

/**
 * Created by fernandamoncores on 4/14/16.
 */
public class TransportesAgendadosData {

    private String id;
    private String dataInicial;
    private String tipo;
    private String tipoCarro;
    private String distancia;
    private String codigo;
    private String nome_carro;
    private String data_verbose;
    private String snapshot;
    private String situacao;
    private byte[] foto;
    private Motorista motorista;

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public TransportesAgendadosData(String id, String dataInicial, String tipo, String situacao, String tipoCarro,String distancia, String nome_carro, String data_verbose)
    {
        setId(id);
        setDataInicial(dataInicial);
        setTipo(tipo);
        setSituacao(situacao);
        setTipoCarro(tipoCarro);
        setDistancia(distancia);
        setNome_carro(nome_carro);
        setData_verbose(data_verbose);
    }

    public String getData_verbose() {
        return data_verbose;
    }

    public void setData_verbose(String data_verbose) {
        this.data_verbose = data_verbose;
    }

    public String getNome_carro() {
        return nome_carro;
    }

    public void setNome_carro(String nome_carro) {
        this.nome_carro = nome_carro;
    }

    public String getDistancia() {
        return distancia;
    }

    public void setDistancia(String distancia) {
        this.distancia = distancia;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDataInicial() {
        return dataInicial;
    }

    public void setDataInicial(String dataInicial) {
        this.dataInicial = dataInicial;
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

    public String getSnapshot() {
        return snapshot;
    }

    public void setSnapshot(String snapshot) {
        this.snapshot = snapshot;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public Motorista getMotorista() {
        return motorista;
    }

    public void setMotorista(Motorista motorista) {
        this.motorista = motorista;
    }
}
