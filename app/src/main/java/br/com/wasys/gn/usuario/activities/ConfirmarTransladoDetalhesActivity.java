package br.com.wasys.gn.usuario.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import br.com.wasys.gn.usuario.adapters.CentroCustoAdapter;
import br.com.wasys.gn.usuario.adapters.EmpresasAdapter;
import br.com.wasys.gn.usuario.helpers.CustomMapView;
import br.com.wasys.gn.usuario.helpers.Helper;
import br.com.wasys.gn.usuario.models.CentroCusto;
import br.com.wasys.gn.usuario.models.Empresa;
import br.com.wasys.gn.usuario.services.CalcularTransladoData;
import br.com.wasys.gn.usuario.services.CentroDeCustoService;
import br.com.wasys.gn.usuario.services.CriarTransladoService;
import br.com.wasys.gn.usuario.services.DirectionsService;
import br.com.wasys.gn.usuario.services.EmpresaService;
import br.com.elp.library.utils.BitmapUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ConfirmarTransladoDetalhesActivity extends AppCompatActivity {

    GoogleMap google_map;
    CustomMapView map_view;
    View dialogView;
    LayoutInflater inflater;

    Context mContext;
    ViewGroup container;
    ArrayList<Empresa> dados_empresa = new ArrayList<Empresa>();
    ArrayList<CentroCusto> dados_centro_de_custo = new ArrayList<CentroCusto>();
    EmpresasAdapter adater_empresa;
    CentroCustoAdapter adapter_centro_de_custo;
    @Bind(br.com.wasys.gn.usuario.R.id.lbl_valor)
    TextView lbl_valor;
    @Bind(br.com.wasys.gn.usuario.R.id.txt_destino_bairro_cidade_estado)
    TextView txt_destino_bairro_cidade_estado;
    @Bind(br.com.wasys.gn.usuario.R.id.txt_origem)
    TextView txt_origem;
    @Bind(br.com.wasys.gn.usuario.R.id.txt_destino)
    TextView txt_destino;
    @Bind(br.com.wasys.gn.usuario.R.id.txt_data_agendamento)
    TextView txt_data_agendamento;
    @Bind(br.com.wasys.gn.usuario.R.id.txt_horario_agendamento)
    TextView txt_horario_agendamento;
    @Bind(br.com.wasys.gn.usuario.R.id.txt_disponivel)
    TextView txt_disponivel;
    @Bind(br.com.wasys.gn.usuario.R.id.txt_tipo_diaria)
    TextView txt_tipo_diaria;
    @Bind(br.com.wasys.gn.usuario.R.id.txt_distancia)
    TextView txt_distancia;
    @Bind(br.com.wasys.gn.usuario.R.id.spinner_empresa)
    Spinner spinner_empresa;
    @Bind(br.com.wasys.gn.usuario.R.id.spinner_centro_de_custo)
    Spinner spinner_centro_de_custo;

    public String valor;
    public String destino;
    public String origem;
    public String retorno;
    public String data;
    public String horario;
    public String tipo_diaria;
    public String distancia;
    public String disponivel;
    public String observacoes;
    public String categoria_carro;
    public String cidade_origem;
    public String cidade_destino;
    public String colaborador_final_id;
    SharedPreferences settings;
    CalcularTransladoData obj;

    public static final String PREFS_NAME = "AOP_PREFS";
    final ArrayList<LatLng> final_directions = new ArrayList<LatLng>();

    private static final String TAG = ConfirmarTransladoDetalhesActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapsInitializer.initialize(this);
        setContentView(br.com.wasys.gn.usuario.R.layout.activity_confirmar_translado_detalhes);
        ButterKnife.bind(this);
        this.mContext = this;
        //initializeMaps(savedInstanceState);
        obj = (CalcularTransladoData) getIntent().getSerializableExtra("obj");
        System.out.println("Destino:" + obj.destino);

        colaborador_final_id = obj.getColaborador();

        txt_destino.setText(obj.getDestino());
//
//        origem = extras.getString("origem");

        txt_origem.setText(obj.getOrigem());
//
//        data = extras.getString("data");
        txt_data_agendamento.setText(obj.getData());
//
//        horario = extras.getString("hora");
        txt_horario_agendamento.setText(obj.getHora());
//
//        tipo_diaria = extras.getString("opcao_transporte");
        txt_tipo_diaria.setText("Translado");
//
//        distancia = extras.getString("distancia");
        txt_distancia.setText(obj.getDistancia());
//
//        disponivel = extras.getString("disponivel");
        txt_disponivel.setText(obj.getHora());
//
        lbl_valor.setText(obj.getValor());
        initializeMaps(savedInstanceState);
        setupActionBar();
        loadDataEmpresa();
        System.out.println("Pelo amor de Deus:" + colaborador_final_id);


    }

    public void initializeMaps(Bundle savedInstanceState) {
        System.out.println("Entrei em 1");
        map_view = (CustomMapView) findViewById(br.com.wasys.gn.usuario.R.id.mapa_google);
        System.out.println("Entrei em 2");
        map_view.onCreate(savedInstanceState);
        System.out.println("Entrei em 3");

        google_map = map_view.getMap();
        System.out.println("Entrei em 4");
        System.out.println("Pegando o tipo do mapa:" + google_map.getMapType());


        google_map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        google_map.setMyLocationEnabled(true);
        System.out.println("Entrei em 5");

        Geocoder coder = new Geocoder(this);

        try {
//            //Address origem = coder.getFromLocationName("Rua Alcebíades Plaisant - Água Verde, Curitiba - State of Paraná, Brazil", 10).get(0);
            Address origem = coder.getFromLocationName(txt_origem.getText().toString(), 10).get(0);
            final LatLng lat_long_origem = new LatLng(origem.getLatitude(), origem.getLongitude());
            google_map.addMarker(new MarkerOptions().position(lat_long_origem).icon(BitmapDescriptorFactory.fromResource(br.com.wasys.gn.usuario.R.drawable.marker_gold_final)));
//
//
            Address destino = coder.getFromLocationName(txt_destino.getText().toString(), 10).get(0);
            final LatLng lat_long_destino = new LatLng(destino.getLatitude(), destino.getLongitude());
            google_map.addMarker(new MarkerOptions().position(lat_long_destino).icon(BitmapDescriptorFactory.fromResource(br.com.wasys.gn.usuario.R.drawable.marker_dark_blue)));
//
//
            google_map.moveCamera(CameraUpdateFactory.newLatLngZoom(lat_long_origem, 6));

            Geocoder gcd = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = gcd.getFromLocation(origem.getLatitude(), origem.getLongitude(), 1);
            if (addresses.size() > 0)
            {
                cidade_origem = addresses.get(0).getLocality();
            }

            Geocoder gcd1 = new Geocoder(this, Locale.getDefault());
            List<Address> addresses1 = gcd.getFromLocation(destino.getLatitude(), destino.getLongitude(), 1);
            if (addresses.size() > 0)
            {
                cidade_destino = addresses1.get(0).getLocality();
            }

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(DirectionsService.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            DirectionsService service = retrofit.create(DirectionsService.class);

            //Call<ResponseBody> response = service.getJson("Rua Alcebíades Plaisant - Água Verde, Curitiba - State of Paraná, Brazil", "Rua Amazonas - Água Verde, Curitiba - State of Paraná, Brazil");
            Call<ResponseBody> response = service.getJson(txt_origem.getText().toString(), txt_destino.getText().toString());


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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadDataEmpresa()
    {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(EmpresaService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        EmpresaService service = retrofit.create(EmpresaService.class);

        Call<ResponseBody> response = service.getEmpresas(Long.parseLong(Helper.current_user(this).getColaborador()));
        System.out.println("Mandei o id:" + Long.parseLong(Helper.current_user(this).getColaborador()));

        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {

//                    Empresa prompt = new Empresa();
//                    prompt.setId("99999");
//                    prompt.setNome("Selecione uma empresa");
//                    dados_empresa.add(new Empresa());
                    System.out.println("Fiz a requisição");
                    JSONObject response_json = new JSONObject(response.body().string());
                    System.out.println("JSON RESPONSE:" + response_json.toString());
                    if (response_json.getString("status").equals("SUCCESS")) {
                        JSONArray empresas_json = response_json.getJSONObject("result").getJSONArray("empresas");
                        for (int i = 0; i < empresas_json.length(); i++) {
                            JSONObject obj_pessoa_json = empresas_json.getJSONObject(i);
                            Empresa obj_empresa = new Empresa();
                            obj_empresa.setId(obj_pessoa_json.getString("id"));
                            obj_empresa.setNome(obj_pessoa_json.getString("razao_social"));
                            obj_empresa.setCpnj(obj_pessoa_json.getString("cnpj"));
                            dados_empresa.add(obj_empresa);
                        }
                        adater_empresa = new EmpresasAdapter(mContext, dados_empresa);
                        adater_empresa.notifyDataSetChanged();
                        spinner_empresa.setAdapter(adater_empresa);

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

    @OnItemSelected(br.com.wasys.gn.usuario.R.id.spinner_empresa)
    public void selected(AdapterView<?> parentView, View selectedItemView, int position, long id){

        loadDataCentroDeCusto(Long.parseLong(colaborador_final_id));

    }

    public void loadDataCentroDeCusto(long empresa_id)
    {
        System.out.println("Entrei na funcao aqui pra carregar o centro de custo");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(CentroDeCustoService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CentroDeCustoService service = retrofit.create(CentroDeCustoService.class);

        //Call<ResponseBody> response = service.getCentros(Long.parseLong(Helper.current_user(getActivity()).getColaborador()));

        Call<ResponseBody> response = service.getCentros(empresa_id);
        System.out.println("Movimentei o Spinner");

        System.out.println("ID do Colaborador:"+colaborador_final_id);
        System.out.println("ID do Colaborador 1:"+obj.getColaborador());
        System.out.println("ID do Colaborador 2:"+Helper.current_user(this).getColaborador());
        System.out.println("Mandei o id:" + empresa_id);

        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    dados_centro_de_custo.clear();
                    System.out.println("Fiz a requisição");
                    JSONObject response_json = new JSONObject(response.body().string());
                    System.out.println("JSON RESPONSE:" + response_json.toString());
                    if (response_json.getString("status").equals("SUCCESS")) {
                        JSONArray empresas_json = response_json.getJSONObject("result").getJSONArray("centros");
                        for (int i = 0; i < empresas_json.length(); i++) {
                            JSONObject obj_pessoa_json = empresas_json.getJSONObject(i);
                            CentroCusto obj = new CentroCusto();
                            obj.setId(obj_pessoa_json.getString("id"));
                            obj.setNome(obj_pessoa_json.getString("nome"));
                            dados_centro_de_custo.add(obj);
                        }
                        adapter_centro_de_custo = new CentroCustoAdapter(mContext, dados_centro_de_custo);
                        adapter_centro_de_custo.notifyDataSetChanged();
                        spinner_centro_de_custo.setAdapter(adapter_centro_de_custo);
                    } else {

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println("Deu falha no centro");

            }

        });

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
            poly.add(p);
        }

        return poly;
    }

    public void setupActionBar()
    {
        android.support.v7.app.ActionBar app = getSupportActionBar();
        app.setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        app.setCustomView(br.com.wasys.gn.usuario.R.layout.action_bar);
        app.setDisplayHomeAsUpEnabled(true);
        ((TextView)app.getCustomView().findViewById(br.com.wasys.gn.usuario.R.id.actionbar_title)).setText("Confirmar Translado");
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

    @OnClick(br.com.wasys.gn.usuario.R.id.btn_solicitar_transporte)
    public void btnClick(View v)
    {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogView = getLayoutInflater().inflate(br.com.wasys.gn.usuario.R.layout.dialog_solicitar_transporte, container, false);
        TextView txt_mensagem = (TextView) dialogView.findViewById(br.com.wasys.gn.usuario.R.id.txt_mensagem_dialog);
        txt_mensagem.setText("O valor de R$" + obj.getValor() + " é estimado e pode ser necessária a inclusão de valores adicionais caso existam desvios de percurso, estacionamentos ou maior tempo do carro à disposição.");
        dialog.setContentView(dialogView);
        dialog.setTitle("Title...");


        Button dialogButton = (Button) dialog.findViewById(br.com.wasys.gn.usuario.R.id.btn_ok);
        Button dialogCancelButton = (Button) dialog.findViewById(br.com.wasys.gn.usuario.R.id.btn_cancel);
        dialogCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                google_map.snapshot(new GoogleMap.SnapshotReadyCallback() {
                    @Override
                    public void onSnapshotReady(Bitmap bitmap) {
                        // Obtem a imagem do mapa
                        String snapshot = null;
                        Context context = getBaseContext();
                        try {
                            snapshot = BitmapUtils.toBase64(bitmap);
                            obj.setSnapshot(snapshot);
                        } catch (IOException e) {
                            String text = "Erro ao obter a imagem do mapa!";
                            Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
                            Log.e(TAG, text, e);
                        }
                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(CriarTransladoService.BASE_URL)
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();
                        CriarTransladoService service = retrofit.create(CriarTransladoService.class);
                        obj.setEmpresa(spinner_empresa.getSelectedItemId()+"");
                        obj.setCentro_de_custo(spinner_centro_de_custo.getSelectedItemId()+"");
                        obj.setCidade_origem(cidade_origem);
                        Call<ResponseBody> response = service.criar_translado(obj);
                        response.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                try {

                                    JSONObject response_json = new JSONObject(response.body().string());
                                    System.out.println("Teste:" + response_json.toString());
                                    if (response_json.getString("status").equals("SUCCESS")) {
                                        Toast.makeText(mContext, "Solicitação de Transporte criada com sucesso!", Toast.LENGTH_LONG).show();
                                        dialog.dismiss();
                                        Intent i = new Intent(mContext, MainActivity.class);
                                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        mContext.startActivity(i);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Toast.makeText(mContext, "Houve um erro de processamento...", Toast.LENGTH_LONG).show();
                                }
                            }
                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {

                            }
                        });
                    }
                });

            }
        });
        dialog.show();


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
