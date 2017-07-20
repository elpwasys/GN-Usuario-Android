package br.com.wasys.gn.usuario.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import br.com.wasys.gn.usuario.R;
import br.com.wasys.gn.usuario.helpers.Helper;
import br.com.wasys.gn.usuario.services.DirectionsService;
import br.com.wasys.gn.usuario.services.EnviarAvaliacaoData;
import br.com.wasys.gn.usuario.services.EnviarAvaliacaoService;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AvaliacaoMotoristaActivity extends AppCompatActivity  {

    GoogleMap google_map;
    MapView map_view;
    @Bind(R.id.txt_codigo) TextView txt_codigo;
    @Bind(R.id.txt_partida) TextView txt_partida;
    @Bind(R.id.txt_chegada) TextView txt_chegada;
//    @Bind(R.id.txt_duracao) TextView txt_duracao;
    Context mContext;
    @Bind(R.id.lbl_valor) TextView lbl_valor;
    @Bind(R.id.lbl_franquia) TextView lbl_franquia;
    @Bind(R.id.txt_motorista) TextView txt_motorista;
    @Bind(R.id.txt_carro) TextView txt_carro;
    @Bind(R.id.txt_tipo_transporte) TextView txt_tipo_transporte;
    @Bind(R.id.txt_meia_diaria) TextView txt_meia_diaria;
    @Bind(R.id.txt_empresa) TextView txt_empresa;
    @Bind(R.id.txt_simei) TextView txt_simei;
    @Bind(R.id.ratingStars) RatingBar nota;
    @Bind(R.id.txt_notas) EditText comentarios;
    @Bind(R.id.txt_data_agendamento) TextView txt_data_agendamento;
    @Bind(R.id.txt_distancia) TextView txt_distancia;

    JSONObject response_json;

    final ArrayList<LatLng> final_directions = new ArrayList<LatLng>();
    ProgressDialog progress;

    public void showProgressDialog()
    {
        progress = new ProgressDialog(this);
        progress.setMessage("Carregando....");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        //progress.setIndeterminate(true);
        progress.show();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avaliacao_motorista);
        MapsInitializer.initialize(this);
        map_view = (MapView) this.findViewById(R.id.mapa_google_avaliacao_motorista);
        map_view.onCreate(savedInstanceState);
        google_map = map_view.getMap();
        mContext = this;
        google_map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        google_map.setMyLocationEnabled(true);
        initializeMaps(savedInstanceState);
        ButterKnife.bind(this);
        //nota.setRating(1l);
        loadData();
    }

    public void loadData()
    {
        showProgressDialog();
        //getIntent().getExtras()
        Bundle b = getIntent().getExtras();
        try{
            response_json = new JSONObject(b.get("obj_json").toString());
            System.out.println("Avaliação:"+response_json);
            txt_codigo.setText(response_json.getJSONObject("solicitacao").getString("codigo"));
            txt_tipo_transporte.setText(response_json.getJSONObject("solicitacao").getString("tipoCarro"));
            txt_partida.setText(response_json.getJSONObject("solicitacao").getJSONArray("realizados").getJSONObject(0).getString("origem"));
            txt_chegada.setText(response_json.getJSONObject("solicitacao").getJSONArray("realizados").getJSONObject(0).getString("destino"));
            txt_motorista.setText(response_json.getJSONObject("motorista").getString("nome"));
            txt_carro.setText(response_json.getJSONObject("carro").getString("modelo"));
            txt_meia_diaria.setText(response_json.getJSONObject("solicitacao").getString("tipo"));
            txt_data_agendamento.setText(response_json.getJSONObject("solicitacao").getString("dataInicial"));
            txt_distancia.setText(response_json.getJSONObject("solicitacao").getString("distanciaRealizada"));
            txt_empresa.setText(response_json.getJSONObject("empresa").getString("nome"));
            txt_simei.setText(response_json.getJSONObject("centro_custo").getString("nome"));
            lbl_valor.setText(response_json.getJSONObject("solicitacao").getString("valorMotoristaRealizado"));
            lbl_franquia.setText(response_json.getJSONObject("solicitacao").getString("distanciaRealizada"));
            progress.hide();
        }catch (Exception e)
        {
            progress.hide();

            e.printStackTrace();
        }
    }

    @OnClick(R.id.btn_enviar)
    public void Enviar(View v)
    {
        double nota_avaliacao = nota.getRating();

        if(nota_avaliacao > 0)
        {
            showProgressDialog();
            EnviarAvaliacaoData obj = new EnviarAvaliacaoData();

            obj.setNota(String.valueOf(nota_avaliacao));
            obj.setComentario(comentarios.getText().toString());
            obj.setColaborador(Helper.current_user(this).getColaborador());
            try{
                obj.setSolicitacao_id(response_json.getJSONObject("solicitacao").getString("id"));
            }catch (Exception e)
            {
                obj.setSolicitacao_id("-1");
            }

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(EnviarAvaliacaoService.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            EnviarAvaliacaoService service = retrofit.create(EnviarAvaliacaoService.class);

            Call<ResponseBody> response = service.enviar(obj);

            response.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {

                        JSONObject response_json = new JSONObject(response.body().string());
                        System.out.println("JSON RESPONSE:" + response_json.toString());
                        if (response_json.getString("status").equals("SUCCESS")) {
                            progress.hide();
                            Toast.makeText(mContext, "Avaliação enviada com sucesso!",Toast.LENGTH_LONG).show();
                            finish();
                        }else
                        {
                            Toast.makeText(mContext, "Erro de processamento!Favor preencher todos os campos!",Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        progress.hide();
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(mContext, "Erro de processamento!",Toast.LENGTH_LONG).show();

                }

            });
        }else
        {
            Toast.makeText(mContext, "É necessário você preencher ao menos uma estrela!",Toast.LENGTH_LONG).show();
        }





    }


    public void initializeMaps(Bundle savedInstanceState)
    {

        Geocoder coder = new Geocoder(this);

        try {
            Address origem = coder.getFromLocationName("Rua Castro - Água Verde, Curitiba - State of Paraná, Brazil", 10).get(0);
            //Address origem = coder.getFromLocationName(txt_partida.getText().toString(), 10).get(0);
            LatLng lat_long_origem = new LatLng(origem.getLatitude(), origem.getLongitude());
            google_map.addMarker(new MarkerOptions().position(lat_long_origem).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_gold_final)));


            //Address destino = coder.getFromLocationName(txt_chegada.getText().toString(), 10).get(0);
            Address destino = coder.getFromLocationName("Rua Amazonas - Água Verde, Curitiba - State of Paraná, Brazil".toString(), 10).get(0);

            LatLng lat_long_destino = new LatLng(destino.getLatitude(), destino.getLongitude());
            google_map.addMarker(new MarkerOptions().position(lat_long_destino).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_dark_blue)));

            google_map.moveCamera(CameraUpdateFactory.newLatLngZoom(lat_long_origem, 12));

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(DirectionsService.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            DirectionsService service = retrofit.create(DirectionsService.class);

            Call<ResponseBody> response = service.getJson("Rua Alcebíades Plaisant - Água Verde, Curitiba - State of Paraná, Brazil", "Rua Amazonas - Água Verde, Curitiba - State of Paraná, Brazil");
           // Call<ResponseBody> response = service.getJson(txt_partida.getText().toString(), txt_chegada.getText().toString());


            response.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        JSONObject response_json = new JSONObject(response.body().string());
                        //System.out.println(response_json);
                        JSONArray routes = response_json.getJSONArray("routes");
                        for (int i = 0; i < routes.length(); i++) {
                            System.out.println("Entrei no routes");
                            JSONArray legs = ((JSONObject) routes.get(i)).getJSONArray("legs");
                            List path = new ArrayList<HashMap<String, String>>();
                            for (int j = 0; j < routes.length(); j++) {
                                System.out.println("Entrei no legs");
                                JSONArray steps = ((JSONObject) legs.get(j)).getJSONArray("steps");
                                for (int k = 0; k < steps.length(); k++) {
                                    System.out.println("Entrei aqui no steps");
                                    String polyline = "";
                                    polyline = (String) ((JSONObject) ((JSONObject) steps.get(k)).get("polyline")).get("points");
                                    System.out.println("PolyLine:" + polyline);
                                    List<LatLng> list = decodePoly(polyline);
                                    for (int l = 0; l < list.size(); l++) {
                                        double lat_string = ((LatLng) list.get(l)).latitude;
                                        double long_string = ((LatLng) list.get(l)).longitude;
                                       // System.out.println("latitude,longitude" + lat_string + long_string);
                                        LatLng obj_lat_long = new LatLng(lat_string, long_string);
                                       // System.out.println("Antes");
                                        final_directions.add(obj_lat_long);
                                       // System.out.println("Depois");
                                    }
                                }

                            }
                        }
                        Polyline line = google_map.addPolyline(new PolylineOptions().addAll(final_directions)
                                .width(5).color(Color.GRAY).geodesic(true));
                    } catch (Exception e) {
                        System.out.println("Entrei em 1111");
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



    public void setTitleActionBar()
    {
        android.support.v7.app.ActionBar app = getSupportActionBar();
        app.setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        app.setCustomView(R.layout.action_bar);
        ((TextView)app.getCustomView().findViewById(R.id.actionbar_title)).setText("Avaliação");
    }

    @Override
    public void onResume() {
        map_view.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        map_view.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        map_view.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        map_view.onLowMemory();
    }
}
