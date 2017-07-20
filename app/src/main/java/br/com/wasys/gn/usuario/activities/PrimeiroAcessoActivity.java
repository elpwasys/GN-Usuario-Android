package br.com.wasys.gn.usuario.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import br.com.wasys.gn.usuario.R;
import br.com.wasys.gn.usuario.helpers.Helper;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class PrimeiroAcessoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primeiro_acesso);
        ButterKnife.bind(this);
        if(!Helper.firstLogin(this))
        {
            Intent i = new Intent(this,MainActivity.class);
            startActivity(i);
        }
    }

    @OnClick({ R.id.btn_ja_sou_cadastrado, R.id.btn_primeiro_acesso })
    public void onClick(View v)
    {
        Intent i;
        switch (v.getId())
        {
            case R.id.btn_primeiro_acesso:
                i = new Intent(this,EsqueciMinhaSenhaActivity.class);
                startActivity(i);
                break;
            case R.id.btn_ja_sou_cadastrado:
                i = new Intent(this,LoginActivity.class);
                startActivity(i);
                break;
        }
    }
}
