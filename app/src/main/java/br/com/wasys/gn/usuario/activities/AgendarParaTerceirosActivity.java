package br.com.wasys.gn.usuario.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

import br.com.wasys.gn.usuario.adapters.PessoaAdapter;
import br.com.wasys.gn.usuario.helpers.Helper;
import br.com.wasys.gn.usuario.models.Pessoa;
import br.com.wasys.gn.usuario.services.PessoaService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AgendarParaTerceirosActivity extends AppCompatActivity {


    Context mContext;
    PessoaAdapter adapter;
    ArrayList<Pessoa> dados = new ArrayList<Pessoa>();
    private int opcao_menu = 0;
    @Bind(br.com.wasys.gn.usuario.R.id.spinner_pessoas) Spinner spinner_pessoas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(br.com.wasys.gn.usuario.R.layout.activity_agendar_para_terceiros);
        this.mContext = this;
        ButterKnife.bind(this);
        Bundle b = getIntent().getExtras();
        opcao_menu = b.getInt("opcao");
        setupActionBar();
        loadData();

    }

    public void loadData()
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(PessoaService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PessoaService service = retrofit.create(PessoaService.class);

        long id = Long.parseLong(Helper.current_user(this).getColaborador());
        Call<ResponseBody> response = service.getPessoas(id);
        System.out.println("Mandei o id:" + id);

        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {

                    System.out.println("Fiz a requisição");
                    JSONObject response_json = new JSONObject(response.body().string());
                    System.out.println("JSON RESPONSE:" + response_json.toString());
                    if (response_json.getString("status").equals("SUCCESS")) {
                        JSONArray colaboradores_json = response_json.getJSONObject("result").getJSONArray("colaboradores");
                        for (int i = 0; i < colaboradores_json.length(); i++) {
                            JSONObject obj_pessoa_json = colaboradores_json.getJSONObject(i);
                            Pessoa obj_pessoa = new Pessoa();
                            obj_pessoa.setId(obj_pessoa_json.getString("id"));
                            obj_pessoa.setNome(obj_pessoa_json.getString("nome"));
                            dados.add(obj_pessoa);
                        }
                        adapter = new PessoaAdapter(mContext, dados);
                        adapter.notifyDataSetChanged();
                        spinner_pessoas.setAdapter(adapter);
                    } else {

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }

        });
    }


    public void setupActionBar() {
        android.support.v7.app.ActionBar app = getSupportActionBar();
        app.setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        app.setCustomView(br.com.wasys.gn.usuario.R.layout.action_bar);
        app.setDisplayHomeAsUpEnabled(true);
        ((TextView)app.getCustomView().findViewById(br.com.wasys.gn.usuario.R.id.actionbar_title)).setText("Agendar para terceiros");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick({br.com.wasys.gn.usuario.R.id.btn_aplicar, br.com.wasys.gn.usuario.R.id.btn_para_mim_mesmo})
    public void Agendar(View v)
    {
        Intent i;
        if (opcao_menu < 3)
            i = new Intent(this, EscolherTransporteActivity.class);
        else
            i = new Intent(this, TransladoActivity.class);
        Bundle b = new Bundle();
        b.putInt("opcao", opcao_menu);
        switch (v.getId())
        {
            case br.com.wasys.gn.usuario.R.id.btn_para_mim_mesmo:
                b.putString("colaborador_id", Helper.current_user(this).getColaborador());
                break;
            case br.com.wasys.gn.usuario.R.id.btn_aplicar:
                b.putString("colaborador_id", adapter.dados.get(spinner_pessoas.getSelectedItemPosition()).getId());
                break;

        }
        i.putExtras(b);
        startActivity(i);
    }

}
