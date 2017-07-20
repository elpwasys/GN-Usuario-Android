package br.com.wasys.gn.usuario.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import br.com.wasys.gn.usuario.R;

public class AvaliarMotoristaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleActionBar();
        setContentView(R.layout.activity_avaliar_motorista);
    }


    public void setTitleActionBar()
    {
        android.support.v7.app.ActionBar app = getSupportActionBar();
        app.setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        app.setCustomView(R.layout.action_bar);
        ((TextView)app.getCustomView().findViewById(R.id.actionbar_title)).setText("Avaliação");
    }
}
