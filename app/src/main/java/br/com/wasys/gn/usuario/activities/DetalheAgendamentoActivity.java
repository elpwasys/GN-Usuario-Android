package br.com.wasys.gn.usuario.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import br.com.wasys.gn.usuario.R;

import br.com.wasys.gn.usuario.helpers.CustomMapView;
import br.com.wasys.gn.usuario.models.Solicitacao;
import br.com.wasys.gn.usuario.services.DetalhesAgendamentoTransporteService;
import br.com.wasys.gn.usuario.services.DirectionsService;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetalheAgendamentoActivity extends AppCompatActivity {



    GoogleMap google_map;
    CustomMapView map_view;
    JSONObject result;
    JSONObject solicitacao;
    JSONArray trechos;
    JSONObject empresa;
    JSONObject centro_custo;
    @Bind(R.id.txt_motorista) TextView txt_motorista;
    @Bind(R.id.txt_distancia) TextView txt_distancia;
    @Bind(R.id.txt_status_confirmado) TextView txt_status_confirmado;
    @Bind(R.id.txt_meia_diaria) TextView txt_meia_diaria;
    @Bind(R.id.txt_tipo_transporte) TextView txt_tipo_transporte;
    @Bind(R.id.txt_codigo)  TextView txt_codigo;
    @Bind(R.id.txt_partida) TextView txt_partida;
    @Bind(R.id.txt_chegada) TextView txt_chegada;
    @Bind(R.id.txt_duracao) TextView txt_duracao;
    @Bind(R.id.txt_empresa) TextView txt_empresa;
    @Bind(R.id.txt_simei) TextView txt_simei;
    @Bind(R.id.txt_carro) TextView txt_carro;
    @Bind(R.id.txt_data_agendamento) TextView txt_data_agendamento;
    @Bind(R.id.txt_observacoes) TextView txt_observacoes;
    @Bind(R.id.txt_telefone) TextView txt_telefone;
    @Bind(R.id.txt_nota_motorista) TextView txt_nota_motorista;
    @Bind(R.id.txt_tempo) TextView txt_tempo;
    final ArrayList<LatLng> final_directions = new ArrayList<LatLng>();
    ProgressDialog progress;

    public Long id;
    public static final String KEY_ID = DetalheAgendamentoActivity.class.getName() + ".id";

    public static Intent newIntent(Context context, Long id) {
        Intent intent = new Intent(context, DetalheAgendamentoActivity.class);
        intent.putExtra(KEY_ID, id);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleActionBar("Transportes Agendados...");
        setContentView(R.layout.activity_detalhe_agendamento);
        MapsInitializer.initialize(this);
        ButterKnife.bind(this);
        initializeMaps(savedInstanceState);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            if (extras.containsKey(KEY_ID)) {
                id = extras.getLong(KEY_ID);
            }
        }
    }

    private void carregar(Long id) {
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
                progress.hide();
                try {
                    JSONObject response_json = new JSONObject(response.body().string());
                    System.out.println(response_json.toString());
                    if (response_json.getString("status").equals("SUCCESS")) {
                        result = response_json.getJSONObject("result");
                        initializeValues();
                    }

                } catch (Exception e) {
                    System.out.println("Erro:" + e.getMessage());
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println("Erro na request");
                progress.hide();

            }
        });
    }

    public void showProgressDialog()
    {
        progress = new ProgressDialog(this);
        progress.setMessage("Processando....");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        //progress.setIndeterminate(true);
        progress.show();

    }

    public void initializeMaps(Bundle savedInstanceState)
    {

        map_view = (CustomMapView) findViewById(R.id.mapa_google);
        map_view.onCreate(savedInstanceState);

        google_map = map_view.getMap();
        google_map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        google_map.setMyLocationEnabled(true);


        Geocoder coder = new Geocoder(this);

        try {
            //Address origem = coder.getFromLocationName("Rua Alcebíades Plaisant - Água Verde, Curitiba - State of Paraná, Brazil", 10).get(0);
            Address origem = coder.getFromLocationName(txt_partida.getText().toString(), 10).get(0);
            final LatLng lat_long_origem = new LatLng(origem.getLatitude(), origem.getLongitude());
            google_map.addMarker(new MarkerOptions().position(lat_long_origem).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_gold_final)));


            Address destino = coder.getFromLocationName(txt_chegada.getText().toString(), 10).get(0);
            final LatLng lat_long_destino = new LatLng(destino.getLatitude(), destino.getLongitude());
            google_map.addMarker(new MarkerOptions().position(lat_long_destino).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_dark_blue)));


            google_map.moveCamera(CameraUpdateFactory.newLatLngZoom(lat_long_origem, 6));

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(DirectionsService.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            DirectionsService service = retrofit.create(DirectionsService.class);

            //Call<ResponseBody> response = service.getJson("Rua Alcebíades Plaisant - Água Verde, Curitiba - State of Paraná, Brazil", "Rua Amazonas - Água Verde, Curitiba - State of Paraná, Brazil");
            Call<ResponseBody> response = service.getJson(txt_partida.getText().toString(), txt_chegada.getText().toString());


            response.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        JSONObject response_json = new JSONObject(response.body().string());
                        //System.out.println(response_json);
                        JSONArray routes = response_json.getJSONArray("routes");
                        for (int i = 0; i < routes.length(); i++) {
                            JSONArray legs = ((JSONObject) routes.get(i)).getJSONArray("legs");
                            List path = new ArrayList<HashMap<String, String>>();
                            for (int j = 0; j < routes.length(); j++) {
                                JSONArray steps = ((JSONObject) legs.get(j)).getJSONArray("steps");
                                for (int k = 0; k < steps.length(); k++) {
                                    String polyline = "";
                                    polyline = (String) ((JSONObject) ((JSONObject) steps.get(k)).get("polyline")).get("points");

                                    List<LatLng> list = decodePoly(polyline);
                                    for (int l = 0; l < list.size(); l++) {
                                        double lat_string = ((LatLng) list.get(l)).latitude;
                                        double long_string = ((LatLng) list.get(l)).longitude;
                                        LatLng obj_lat_long = new LatLng(lat_string, long_string);
                                        final_directions.add(obj_lat_long);
                                    }
                                }

                            }
                        }
                        Polyline line = google_map.addPolyline(new PolylineOptions().addAll(final_directions)
                                .width(5).color(Color.GRAY).geodesic(true));

                        LatLngBounds latLngBounds = new LatLngBounds.Builder()
                                .include(lat_long_origem)
                                .include(lat_long_destino)
                                .build();
                        google_map.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 75));

                    } catch (Exception e) {
                        System.out.println("Aqui no erro");
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        }catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            System.out.println("Aqui:"+p.latitude);
            poly.add(p);
        }

        return poly;
    }

    public void initializeValues()
    {

        try {
            System.out.println("retorno do malandro:"+result.toString());
            solicitacao = result.getJSONObject("solicitacao");
            trechos = solicitacao.getJSONArray("itens");
            String situacao = solicitacao.getString("situacao");
            if(result.has("motorista") && !StringUtils.equals(Solicitacao.Situacao.PENDENTE.name(), situacao))
            {
                txt_motorista.setText(result.getJSONObject("motorista").getString("nome"));
                txt_telefone.setText(result.getJSONObject("motorista").getString("celular"));
            }
            txt_data_agendamento.setText(solicitacao.getString("dataInicial"));
            setTitleActionBar(solicitacao.getString("dataInicial"));
            txt_status_confirmado.setText(situacao);
            txt_meia_diaria.setText(solicitacao.getString("tipo"));
            txt_codigo.setText(solicitacao.getString("codigo"));
            JSONObject trecho = trechos.getJSONObject(0);
            txt_partida.setText(trecho.getString("origem"));
            txt_chegada.setText(trecho.getString("destino"));
            txt_duracao.setText(solicitacao.getString("distanciaTotal"));
            empresa = result.getJSONObject("empresa");
            centro_custo = result.getJSONObject("centroCusto");
            txt_empresa.setText(empresa.getString("nome"));
            txt_simei.setText(centro_custo.getString("nome"));
            txt_distancia.setText(solicitacao.getString("distanciaTotal"));
            txt_tipo_transporte.setText(solicitacao.getString("tipoCarro"));
            if(result.has("observacoes"))
                txt_observacoes.setText(result.getString("observacoes"));
            else
                txt_observacoes.setText("Sem observações");
            txt_carro.setText(result.getString("carro"));
            txt_nota_motorista.setText(result.getString("media_motorista"));
            txt_tempo.setText(solicitacao.getString("duracao"));
        } catch (JSONException e) {
            e.printStackTrace();
            e.printStackTrace();
        }
    }



    @Override
    protected void onResume()
    {
        super.onResume();
        map_view.onResume();
        if (id != null) {
            carregar(id);
        } else {
            finish();
        }
    }


    protected void OnDestroy()
    {
        super.onDestroy();
        map_view.onDestroy();
    }


    protected void OnPause()
    {
        super.onPause();
        map_view.onPause();
    }

    public void setTitleActionBar(String title)
    {
        android.support.v7.app.ActionBar app = getSupportActionBar();
        app.setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        app.setCustomView(R.layout.action_bar);
        app.setDisplayHomeAsUpEnabled(true);
        ((TextView)app.getCustomView().findViewById(R.id.actionbar_title)).setText(title);
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
}
