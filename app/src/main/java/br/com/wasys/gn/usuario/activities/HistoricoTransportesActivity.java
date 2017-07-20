package br.com.wasys.gn.usuario.activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import br.com.wasys.gn.usuario.R;
import br.com.wasys.gn.usuario.adapters.ScheduledTransportAdapter;
import br.com.wasys.gn.usuario.helpers.Helper;
import br.com.wasys.gn.usuario.models.Motorista;
import br.com.wasys.gn.usuario.services.TransportesAgendadosData;
import br.com.wasys.gn.usuario.services.TransportesConcluidosService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HistoricoTransportesActivity extends AppCompatActivity {

    public Context ctx;
    ListView list;
    private ArrayList<TransportesAgendadosData> records;
    private ScheduledTransportAdapter adapter;
    long colaborador_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico_transportes);
        this.ctx = this;
        setupActionBar();
        list = (ListView) findViewById(R.id.list_view_menu);
        records = new ArrayList<TransportesAgendadosData>();
        adapter = new ScheduledTransportAdapter(this, records);
        list.setAdapter(adapter);
        loadData();
    }

    public void loadData() {
        try {
            System.out.println("Chamando LoadData");
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(TransportesConcluidosService.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            TransportesConcluidosService service = retrofit.create(TransportesConcluidosService.class);
            colaborador_id = Long.parseLong(Helper.current_user(this).getColaborador());

            Call<ResponseBody> response = service.getTransportes(colaborador_id);

            response.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        JSONObject response_json = new JSONObject(response.body().string());
                        if (response_json.getString("status").equals("SUCCESS")) {
                            JSONArray results = response_json.getJSONObject("result").getJSONArray("concluidos");
                            records = new ArrayList<TransportesAgendadosData>(results.length());
                            for (int i = 0; i < results.length(); i++) {
                                JSONObject result = results.getJSONObject(i);
                                System.out.println(result.toString());
                                String nome_carro;
                                if(result.has("carro"))
                                {
                                    JSONObject carro_json = result.getJSONObject("carro");
                                    nome_carro = carro_json.getString("modelo");
                                }else
                                {
                                    nome_carro = "Sem carro";
                                }
                                TransportesAgendadosData obj_transporte = new TransportesAgendadosData(result.getString("id"), result.getString("dataInicial"), result.getString("tipo"), null, result.getString("tipoCarro"), result.getString("distanciaTotal"),nome_carro,"");
                                obj_transporte.setCodigo(result.getString("codigo"));
                                /*if (result.has("snapshot")) {
                                    obj_transporte.setSnapshot(result.getString("snapshot"));
                                }*/
                                if (result.has("motorista")) {
                                    JSONObject object = result.getJSONObject("motorista");
                                    Motorista motorista = new Motorista();
                                    motorista.id = object.getLong("id");
                                    motorista.nome = object.getString("nome");
                                    obj_transporte.setMotorista(motorista);
                                }
                                records.add(obj_transporte);
                            }
                            adapter.results = records;
                            adapter.notifyDataSetChanged();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                }

            });
        } catch (Exception e) {
        }
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

    public void setupActionBar() {
        android.support.v7.app.ActionBar app = getSupportActionBar();
        app.setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        app.setCustomView(R.layout.action_bar);
        app.setDisplayHomeAsUpEnabled(true);
        ((TextView)app.getCustomView().findViewById(R.id.actionbar_title)).setText("Históricos");
    }
}
