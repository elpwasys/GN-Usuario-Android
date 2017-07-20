package br.com.wasys.gn.usuario.models;

import br.com.wasys.gn.usuario.google.Distance;
import br.com.wasys.gn.usuario.google.Duration;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by fernandamoncores on 4/14/16.
 */
public class Trecho implements Serializable {

    public Date data;
    public String horario;
    public Boolean pernoite;

    public Endereco inicio;
    public Endereco termino;

    public Distance distance;
    public Duration duration;
}