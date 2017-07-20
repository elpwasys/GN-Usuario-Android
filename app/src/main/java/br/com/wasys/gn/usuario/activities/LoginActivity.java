package br.com.wasys.gn.usuario.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import br.com.wasys.gn.usuario.R;
import br.com.wasys.gn.usuario.helpers.Helper;
import br.com.wasys.gn.usuario.services.LoginData;
import br.com.wasys.gn.usuario.services.UsuarioService;
import org.json.JSONObject;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "erro" ;
    public static final String PREFS_NAME = "AOP_PREFS";
    @Bind(R.id.activity_login_btn_esqueci_minha_senha) Button btn_activity_login_esqueci_minha_senha;
    @Bind(R.id.activity_login_txtEmail) EditText login;
    @Bind(R.id.activity_login_txtSenha) EditText senha;
    @Bind(R.id.txt_invalid_message) TextView message;
    private Context ctx;
    SharedPreferences settings;

    TextWatcher inputTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            message.setVisibility(View.INVISIBLE);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        this.ctx = this;
        btn_activity_login_esqueci_minha_senha.setPaintFlags(btn_activity_login_esqueci_minha_senha.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        login.addTextChangedListener(inputTextWatcher);
        senha.addTextChangedListener(inputTextWatcher);
        if(!Helper.firstLogin(this))
        {
            Intent i = new Intent(this,MainActivity.class);
            startActivity(i);
        }
    }

    public void showMessageInvalidLogin()
    {
        message.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.activity_login_btn_esqueci_minha_senha,R.id.activity_login_btn_entrar}) protected void onClick(View v) {
        switch(v.getId())
        {
            case R.id.activity_login_btn_esqueci_minha_senha:
                Intent intent = new Intent(this, EsqueciMinhaSenhaActivity.class);
                startActivity(intent);
                break;
            case R.id.activity_login_btn_entrar:
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(UsuarioService.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                UsuarioService service = retrofit.create(UsuarioService.class);

                Call<ResponseBody> response = service.autentica(new LoginData(login.getText().toString(), senha.getText().toString()));

                response.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try
                        {

                            JSONObject response_json = new JSONObject(response.body().string());
                            if(response_json.getString("status").equals("SUCCESS"))
                            {
                                message.setVisibility(View.GONE);
                                settings = ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = settings.edit();
                                if(Helper.firstLogin(ctx)) {
                                    editor.putString("login", response_json.toString());
                                    editor.commit();
                                    Intent intent = new Intent(ctx, TermosDeUsoActivity.class);
                                    Bundle extras = new Bundle();
                                    extras.putBoolean("from_termo",false);
                                    intent.putExtras(extras);
                                    startActivity(intent);
                                }else
                                {
                                    Intent intent = new Intent(ctx, MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }
                            }else
                            {
                               showMessageInvalidLogin();

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        System.out.println("Erro de request");
                        showMessageInvalidLogin();
                    }

                });
                break;
        }

    }


}
