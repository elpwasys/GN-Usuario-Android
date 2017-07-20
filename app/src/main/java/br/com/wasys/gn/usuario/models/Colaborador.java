package br.com.wasys.gn.usuario.models;

import java.io.Serializable;

/**
 * Created by pascke on 18/08/16.
 */
public class Colaborador implements Serializable {

    public Long id;

    public Colaborador() {

    }

    public Colaborador(Long id) {
        this.id = id;
    }
}
