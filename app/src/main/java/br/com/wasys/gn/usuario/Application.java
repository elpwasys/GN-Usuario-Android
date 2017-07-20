package br.com.wasys.gn.usuario;

import br.com.elp.library.utils.PreferencesUtils;

/**
 * Created by pascke on 03/08/16.
 */
public class Application extends br.com.elp.library.Application {

    private static final String AUTHORIZATION_PREFERENCES_KEY = Application.class.getName() + ".authorization";

    public static String getAuthorization() {
        return PreferencesUtils.get(AUTHORIZATION_PREFERENCES_KEY);
    }

    public static void setAuthorization(String authorization) {
        PreferencesUtils.put(AUTHORIZATION_PREFERENCES_KEY, authorization);
    }
}
