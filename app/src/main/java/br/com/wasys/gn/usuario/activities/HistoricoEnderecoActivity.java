package br.com.wasys.gn.usuario.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import br.com.wasys.gn.usuario.adapters.HistoricoAdapter;
import br.com.wasys.gn.usuario.fragments.IdaEVolta;
import br.com.wasys.gn.usuario.helpers.Helper;
import br.com.wasys.gn.usuario.models.HistoricoEndereco;
import br.com.wasys.gn.usuario.services.HistoricoTransporteService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import br.com.wasys.gn.usuario.R;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HistoricoEnderecoActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {


    Context mContext;
    public Context ctx;
    ListView list;
    HistoricoAdapter adapter;
    ArrayList<HistoricoEndereco> dados = new ArrayList<HistoricoEndereco>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico_endereco);
        ButterKnife.bind(this);
        this.ctx = this;
        setupActionBar();
        list = (ListView) findViewById(R.id.list_view_historico);
        adapter = new HistoricoAdapter(this);
        list.setAdapter(adapter);
        list.setOnItemClickListener(this);
        loadData();
    }

    public void loadData()
    {
        System.out.println("Entrei aqui no Historico do Transporte");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(HistoricoTransporteService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        HistoricoTransporteService service = retrofit.create(HistoricoTransporteService.class);
        Call<ResponseBody> response = service.historico(Long.parseLong(Helper.current_user(this).getColaborador()));
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    System.out.println("Entrei aqui no Historico do Transporte1111");
                    JSONObject response_json = new JSONObject(response.body().string());
                    System.out.println("JSON RESPONSE Carro:" + response_json.toString());
                    if (response_json.getString("status").equals("SUCCESS")) {
                        JSONArray locais = response_json.getJSONObject("result").getJSONArray("favoritos");
                        for(int i=0;i<locais.length();i++)
                        {
                            String endereco = locais.getString(i);
                            HistoricoEndereco obj_endereco = new HistoricoEndereco("",endereco,"","Favorito");
                            dados.add(obj_endereco);
                        }

                        locais = response_json.getJSONObject("result").getJSONArray("historico_destinos");
                        for(int i=0;i<locais.length();i++)
                        {
                            String endereco = locais.getString(i);
                            HistoricoEndereco obj_endereco = new HistoricoEndereco("",endereco,"","Histórico de Destino");
                            dados.add(obj_endereco);
                        }

                        locais = response_json.getJSONObject("result").getJSONArray("historico_origens");
                        for(int i=0;i<locais.length();i++)
                        {
                            String endereco = locais.getString(i);
                            HistoricoEndereco obj_endereco = new HistoricoEndereco("",endereco,"", "Histórico de Origem");
                            dados.add(obj_endereco);
                        }

                        adapter.dados = dados;
                        adapter.notifyDataSetChanged();
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
        app.setCustomView(R.layout.action_bar);
        app.setDisplayHomeAsUpEnabled(true);
        ((TextView)app.getCustomView().findViewById(R.id.actionbar_title)).setText("Opções");
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent output = new Intent();
        output.putExtra("endereco", adapter.dados.get(position).getEndereco());
        setResult(IdaEVolta.HISTORICO_ORIGEM, output);
        finish();
    }
}
