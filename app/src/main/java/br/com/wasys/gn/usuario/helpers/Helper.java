package br.com.wasys.gn.usuario.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.inputmethod.InputMethodManager;

import br.com.wasys.gn.usuario.BuildConfig;
import br.com.wasys.gn.usuario.models.Usuario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by fernandamoncores on 4/17/16.
 */
public class Helper {

    public static String BASE_URL = BuildConfig.BASE_URL;
    public static int MEIA_DIARIA = 0;
    public static int DIARIA = 1;
    public static int PERNOITE = 2;
    public static int TRANSLADO = 3;
    public static final String TAG = "erro" ;
    public static final String PREFS_NAME = "AOP_PREFS";

    public static void saveFirstLogin(Context c, String data)
    {
        SharedPreferences settings = c.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("login", data);
        editor.commit();
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public static Usuario current_user(Context c)
    {
        SharedPreferences settings = c.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String json_string = settings.getString("login", null);
        JSONObject response_json = null;
        Usuario obj_usuario = new Usuario();
        try {
            response_json = new JSONObject(json_string);
            obj_usuario.setEmail(response_json.getJSONObject("usuario").getString("email"));
            obj_usuario.setNome(response_json.getJSONObject("usuario").getString("nome"));
            JSONArray colaboradores = response_json.getJSONObject("usuario").getJSONArray("colaboradores");
            obj_usuario.setColaborador(colaboradores.getJSONObject(0).getString("id"));
            obj_usuario.setPermiteAgendarTerceiros(colaboradores.getJSONObject(0).getString("permiteAgendarTerceiros"));
            
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj_usuario;
    }

    public static boolean firstLogin(Context c)
    {
        SharedPreferences settings = c.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String first_login = settings.getString("login",null);
        if(first_login == null) {
            return true;
        }else
        {
            return false;
        }
    }






}
