package br.com.wasys.gn.usuario;

import android.Manifest;

/**
 * Created by pascke on 15/08/16.
 */
public interface Permission {
    static final String[] PHONE = { Manifest.permission.READ_PHONE_STATE };
    static final String[] LOCATION = { Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION };
}
