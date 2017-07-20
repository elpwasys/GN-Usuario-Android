package br.com.wasys.gn.usuario.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import br.com.wasys.gn.usuario.R;
import br.com.wasys.gn.usuario.activities.DetalheAgendamentoActivity;
import br.com.wasys.gn.usuario.adapters.ScheduledTransportAdapter;
import br.com.wasys.gn.usuario.models.Motorista;
import br.com.wasys.gn.usuario.services.TransportesAgendadosData;
import br.com.wasys.gn.usuario.services.TransportesAgendadosService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by fernandamoncores on 3/28/16.
 */
public class ScheduledTransport extends Fragment implements AdapterView.OnItemClickListener {

    private ArrayList<TransportesAgendadosData> records;
    private ScheduledTransportAdapter adapter;
    SharedPreferences settings;
    ProgressDialog progress;

    public static final String PREFS_NAME = "AOP_PREFS";
    final Handler timerHandler = new Handler();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View android = inflater.inflate(R.layout.fragment_scheduled_transport, container, false);
        records = new ArrayList<TransportesAgendadosData>();
        adapter = new ScheduledTransportAdapter(getContext(), records);

        ListView list = (ListView) android.findViewById(R.id.list_view_menu);
        list.setAdapter(adapter);
        /*
         * Everton Luiz Pascke
         * Ajuste Lup 141
         *
        list.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        */
        list.setOnItemClickListener(this);

        setHasOptionsMenu(true);

        return android;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        loadData();
    }

    public void showProgressDialog()
    {
        progress = new ProgressDialog(getActivity());
        progress.setMessage("Processando....");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        //progress.setIndeterminate(true);
        progress.show();
    }


    public void loadData() {
        try {
            System.out.println("Chamando LoadData");
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(TransportesAgendadosService.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            //showProgressDialog();
            TransportesAgendadosService service = retrofit.create(TransportesAgendadosService.class);
            settings = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            String json_string = settings.getString("login", null);
            JSONObject response_json = null;
            long colaborador_id = -1;
            response_json = new JSONObject(json_string);
            System.out.println("JSON DE RETORNO:" + response_json.toString());
            JSONArray colaboradores = response_json.getJSONObject("usuario").getJSONArray("colaboradores");
            colaborador_id = Long.parseLong(colaboradores.getJSONObject(0).getString("id"));

            Call<ResponseBody> response = service.getTransportes(colaborador_id);

            response.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        JSONObject response_json = new JSONObject(response.body().string());
                        if (response_json.getString("status").equals("SUCCESS")) {
                            JSONArray results = response_json.getJSONObject("result").getJSONArray("solicitacoes");
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
                                TransportesAgendadosData obj_transporte = new TransportesAgendadosData(result.getString("id"), result.getString("dataInicial"), result.getString("tipo"), result.getString("situacao"), result.getString("tipoCarro"), result.getString("distanciaTotal"),nome_carro,result.getString("data_verbose"));
                                obj_transporte.setCodigo(result.getString("codigo"));
                                if (result.has("snapshot")) {
                                    obj_transporte.setSnapshot(result.getString("snapshot"));
                                }
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
                            //progress.hide();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                       // progress.hide();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    //progress.hide();
                }

            });
        } catch (Exception e) {


        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = DetalheAgendamentoActivity.newIntent(getContext(), id);
        startActivity(intent);

        /*
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DetalhesAgendamentoTransporteService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        showProgressDialog();
        DetalhesAgendamentoTransporteService service = retrofit.create(DetalhesAgendamentoTransporteService.class);
        Call<ResponseBody> response = service.detalhes(id);

        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject response_json = new JSONObject(response.body().string());
                    System.out.println(response_json.toString());
                    if (response_json.getString("status").equals("SUCCESS")) {
                        JSONObject result = response_json.getJSONObject("result");
                        System.out.println("Json antes de ir:"+result.toString());
                        Intent i = new Intent(getContext(), DetalheAgendamentoActivity.class);
                        progress.hide();
                        i.putExtra("result", result.toString());
                        startActivity(i);
                    }

                } catch (Exception e) {
                    System.out.println("Erro:" + e.getMessage());
                    progress.hide();
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println("Erro na request");
                progress.hide();

            }
        });
        */
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_scheduled_transport, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh_menu:
                loadData();
                return true;
            default:
                break;
        }
        return false;
    }
}
