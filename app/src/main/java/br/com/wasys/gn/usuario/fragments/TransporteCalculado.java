package br.com.wasys.gn.usuario.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import br.com.wasys.gn.usuario.Permission;
import br.com.wasys.gn.usuario.R;
import br.com.wasys.gn.usuario.activities.MainActivity;
import br.com.wasys.gn.usuario.adapters.CentroCustoAdapter;
import br.com.wasys.gn.usuario.adapters.EmpresasAdapter;
import br.com.wasys.gn.usuario.endpoint.CentroCustoEndpoint;
import br.com.wasys.gn.usuario.endpoint.Endpoint;
import br.com.wasys.gn.usuario.endpoint.GoogleMapsApiEndpoint;
import br.com.wasys.gn.usuario.google.Bounds;
import br.com.wasys.gn.usuario.google.DirectionResult;
import br.com.wasys.gn.usuario.google.Leg;
import br.com.wasys.gn.usuario.google.PolylineEncoding;
import br.com.wasys.gn.usuario.google.Route;
import br.com.wasys.gn.usuario.google.Step;
import br.com.wasys.gn.usuario.helpers.CustomMapView;
import br.com.wasys.gn.usuario.helpers.Helper;
import br.com.wasys.gn.usuario.models.CentroCusto;
import br.com.wasys.gn.usuario.models.CentroCustoListResult;
import br.com.wasys.gn.usuario.models.CentroCustoRequestBody;
import br.com.wasys.gn.usuario.models.Empresa;
import br.com.wasys.gn.usuario.services.EmpresaService;
import br.com.wasys.gn.usuario.services.SolicitarTransporteData;
import br.com.wasys.gn.usuario.services.SolicitarTransporteService;
import br.com.elp.library.utils.BitmapUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import br.com.elp.library.fragment.AppFragment;
import br.com.elp.library.http.Error;
import br.com.elp.library.utils.DateUtils;
import br.com.elp.library.utils.TypeUtils;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by fernandamoncores on 4/9/16.
 */
public class TransporteCalculado extends AppFragment {


    private GoogleMap mMap;
    private CustomMapView mMapView;
    private DirectionResult mDirectionResult;

    View dialogView;
    LayoutInflater inflater;
    ViewGroup container;
    ArrayList<Empresa> dados_empresa = new ArrayList<Empresa>();
    List<CentroCusto> mCentrosCustos = new ArrayList<CentroCusto>();
    EmpresasAdapter adater_empresa;
    CentroCustoAdapter mAdapterCentrosCustos;
    @Bind(R.id.lbl_valor)
    TextView lbl_valor;
    @Bind(R.id.txt_destino_bairro_cidade_estado)
    TextView txt_destino_bairro_cidade_estado;
    @Bind(R.id.txt_origem)
    TextView txt_origem;
    @Bind(R.id.txt_destino)
    TextView txt_destino;
    @Bind(R.id.txt_data_agendamento)
    TextView txt_data_agendamento;
    @Bind(R.id.txt_horario_agendamento)
    TextView txt_horario_agendamento;
    @Bind(R.id.txt_disponivel)
    TextView txt_disponivel;
    @Bind(R.id.txt_tipo_diaria)
    TextView txt_tipo_diaria;
    @Bind(R.id.txt_distancia)
    TextView txt_distancia;
    @Bind(R.id.spinner_empresa)
    Spinner spinner_empresa;
    @Bind(R.id.spinner_centro_de_custo)
    Spinner mSpinnerCentrosCustos;
    @Bind(R.id.txt_categoria) TextView txt_categoria;
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
    public String cidadeRetorno;
    public String colaborador_final_id;
    public String duracao;

    SharedPreferences settings;
    ProgressDialog progress;

    public static final String PREFS_NAME = "AOP_PREFS";
    final ArrayList<LatLng> final_directions = new ArrayList<LatLng>();

    private static final String TAG = TransporteCalculado.class.getSimpleName();
    private boolean retornarOrigem;

    private static final int REQUEST_PHONE_STATE = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View android = inflater.inflate(R.layout.fragment_transporte_calculado, container, false);
        MapsInitializer.initialize(getActivity());
        ButterKnife.bind(this, android);

        this.inflater = inflater;
        this.container = container;

//        Bundle extras = getActivity().getIntent().getExtras();
//        valor = extras.getString("valor");
        lbl_valor.setText("R$ " + valor);
//
//        destino = extras.getString("destino");
//
//        origem = extras.getString("origem");

        txt_origem.setText(origem);
        txt_destino.setText(destino);
        retornarOrigem = StringUtils.isNotBlank(retorno);
//
//        data = extras.getString("data");
        txt_data_agendamento.setText(data);
//
//        horario = extras.getString("hora");
        txt_horario_agendamento.setText(horario);
//
//        tipo_diaria = extras.getString("opcao_transporte");
        txt_tipo_diaria.setText(tipo_diaria);
//
//        distancia = extras.getString("distancia");
        txt_distancia.setText(distancia);
//
//        disponivel = extras.getString("disponivel");
        txt_disponivel.setText(disponivel);

        txt_categoria.setText(categoria_carro);
//
//        categoria_carro = extras.getString("categoria_carro");
//
//        observacoes = extras.getString("observacoes");


        //spinner_empresa.setPrompt("Selecione uma empresa");

        //

        return android;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        mMapView = (CustomMapView) view.findViewById(R.id.mapa_google);
        mMapView.onCreate(savedInstanceState);

        mMap = mMapView.getMap();
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setMyLocationEnabled(true);

        syncronizeMap();
        loadData();
        loadCentroCusto();
    }

    public void showProgressDialog()
    {
        progress = new ProgressDialog(getActivity());
        progress.setMessage("Processando....");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        //progress.setIndeterminate(true);
        progress.show();

    }

    public void loadData()
    {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(EmpresaService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        EmpresaService service = retrofit.create(EmpresaService.class);

        //Call<ResponseBody> response = service.getEmpresas(Long.parseLong(Helper.current_user(getActivity()).getColaborador()));
        Call<ResponseBody> response = service.getEmpresas(Long.parseLong(colaborador_final_id));

        System.out.println("Mandei o id:" + Long.parseLong(Helper.current_user(getActivity()).getColaborador()));

        showProgressDialog();
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
                        adater_empresa = new EmpresasAdapter(getActivity(), dados_empresa);
                        adater_empresa.notifyDataSetChanged();
                        spinner_empresa.setAdapter(adater_empresa);
                        progress.hide();
                    } else {
                        progress.hide();
                    }
                } catch (Exception e) {
                    progress.hide();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progress.hide();

            }

        });
    }

    public void loadCentroCusto() {

        if (!checkedSelfPermission(Permission.PHONE)) {
            ActivityCompat.requestPermissions(getActivity(), Permission.PHONE, REQUEST_PHONE_STATE);
            return;
        }

        CentroCustoRequestBody requestBody = new CentroCustoRequestBody();
        requestBody.dataInicial = DateUtils.parse(data, "dd-MM-yyyy");
        requestBody.colaboradorId = TypeUtils.parse(Long.class, colaborador_final_id);

        CentroCustoEndpoint endpoint = Endpoint.create(CentroCustoEndpoint.class);
        Call<CentroCustoListResult> call = endpoint.listar(requestBody);

        call.enqueue(new br.com.elp.library.http.Callback<CentroCustoListResult>() {
            @Override
            public void onSuccess(CentroCustoListResult result) {
                mCentrosCustos = result.centrosCustos;
                if (CollectionUtils.isNotEmpty(mCentrosCustos)) {
                    mAdapterCentrosCustos = new CentroCustoAdapter(getActivity(), mCentrosCustos);
                    mSpinnerCentrosCustos.setAdapter(mAdapterCentrosCustos);
                }
            }
            @Override
            public void onError(Error error) {
                Context context = getContext();
                Toast.makeText(context, error.getFormattedErrorMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void syncronizeMap() {

        String origin = origem;
        String waypoints = null;
        String destination = null;

        if (StringUtils.isBlank(retorno)) {
            destination = destino;
        }
        else {
            waypoints = destino;
            destination = retorno;
        }

        Map<String, String> parameters = new HashMap<>();
        parameters.put("origin", origin);
        parameters.put("destination", destination);
        parameters.put("mode", "driving");
        parameters.put("language", "pt-BR");
        //parameters.put("key", getString(R.string.google_maps_key));
        if (StringUtils.isNotBlank(waypoints)) {
            parameters.put("waypoints", waypoints);
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GoogleMapsApiEndpoint.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GoogleMapsApiEndpoint endpoint = retrofit.create(GoogleMapsApiEndpoint.class);
        Call<DirectionResult> directions = endpoint.directions(parameters);

        final Context context = getContext();
        directions.enqueue(new Callback<DirectionResult>() {
            @Override
            public void onResponse(Call<DirectionResult> call, Response<DirectionResult> response) {
                String message = null;
                if (response.isSuccessful()) {
                    DirectionResult directionResult = response.body();
                    populateMap(directionResult);
                }
                else {
                    message = getString(R.string.msg_falha_http_request_status, response.code());
                }
                if (StringUtils.isNotBlank(message)) {
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<DirectionResult> call, Throwable t) {
                Toast.makeText(context, ExceptionUtils.getRootCauseMessage(t), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void populateMap(DirectionResult directionResult) {
        Context context = getContext();
        if (DirectionResult.Status.OK.equals(directionResult.status)) {
            Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
            try {
                List<Address> addresses;
                // CIDADE ORIGEM
                if (StringUtils.isBlank(cidade_origem)) {
                    addresses = geocoder.getFromLocationName(origem, 1);
                    if (CollectionUtils.isNotEmpty(addresses)) {
                        cidade_origem = addresses.get(0).getLocality();
                    }
                }
                // CIDADE DESTINO
                if (StringUtils.isBlank(cidade_destino)) {
                    addresses = geocoder.getFromLocationName(destino, 1);
                    if (CollectionUtils.isNotEmpty(addresses)) {
                        cidade_destino = addresses.get(0).getLocality();
                    }
                }
                // CIDADE RETORNO
                if (StringUtils.isNotBlank(retorno)) {
                    addresses = geocoder.getFromLocationName(retorno, 1);
                    if (CollectionUtils.isNotEmpty(addresses)) {
                        cidadeRetorno = addresses.get(0).getLocality();
                    }
                }
                mDirectionResult = directionResult;
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                Route route = mDirectionResult.routes[0];
                for (int i = 0; i < route.legs.length; i++) {
                    Leg leg = route.legs[i];
                    PolylineOptions polylineOptions = new PolylineOptions()
                            .width(5)
                            .color(Color.GRAY)
                            .geodesic(true);
                    MarkerOptions startMarkerOptions = new MarkerOptions()
                            .title(leg.startAddress)
                            .position(new LatLng(leg.startLocation.lat, leg.startLocation.lng))
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_dark_blue));
                    mMap.addMarker(startMarkerOptions);
                    for (Step step : leg.steps) {
                        polylineOptions.addAll(PolylineEncoding.decode(step.polyline.points));
                    }
                    mMap.addPolyline(polylineOptions);
                    if (i == (route.legs.length - 1)) {
                        MarkerOptions endMarkerOptions = new MarkerOptions()
                                .title(leg.endAddress)
                                .position(new LatLng(leg.endLocation.lat, leg.endLocation.lng))
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_gold_final));
                        mMap.addMarker(endMarkerOptions);
                    }
                }
                Bounds bounds = route.bounds;
                builder.include(new LatLng(bounds.northeast.lat, bounds.northeast.lng));
                builder.include(new LatLng(bounds.southwest.lat, bounds.southwest.lng));
                LatLngBounds latLngBounds = builder.build();
                mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 75));
            } catch (IOException e) {
                Toast.makeText(context, ExceptionUtils.getRootCauseMessage(e), Toast.LENGTH_LONG).show();
            }
        }
        else {
            String message;
            if (StringUtils.isNotBlank(directionResult.errorMessage)) {
                message = directionResult.errorMessage;
            }
            else {
                message = getString(R.string.msg_falha_directions_api, directionResult.status.name());
            }
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }
    }

    @OnClick(R.id.btn_solicitar_transporte)
    public void onClickSolicitar() {

        final Context context = getContext();

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogView = inflater.inflate(R.layout.dialog_solicitar_transporte, container, false);
        TextView txt_mensagem = (TextView) dialogView.findViewById(R.id.txt_mensagem_dialog);
        txt_mensagem.setText("O valor de R$" + valor + " é estimado e pode ser necessária a inclusão de valores adicionais caso existam desvios de percurso, estacionamentos ou maior tempo do carro à disposição.");
        dialog.setContentView(dialogView);
        dialog.setTitle("Title...");
        settings = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String json_string = settings.getString("login", null);
        JSONObject response_json = null;
        String colaborador_id = "-1";
        try {
            response_json = new JSONObject(json_string);
            System.out.println("JSON DE RETORNO:" + response_json.toString());
            JSONArray colaboradores = response_json.getJSONObject("usuario").getJSONArray("colaboradores");
            colaborador_id = colaboradores.getJSONObject(0).getString("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        Button dialogButton = (Button) dialog.findViewById(R.id.btn_ok);
        Button dialogCancelButton = (Button) dialog.findViewById(R.id.btn_cancel);
        dialogCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        final String finalColaborador_id = colaborador_id;
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.snapshot(new GoogleMap.SnapshotReadyCallback() {
                    @Override
                    public void onSnapshotReady(Bitmap bitmap) {
                        // Obtem a imagem do mapa
                        String snapshot = null;
                        try {
                            snapshot = BitmapUtils.toBase64(bitmap);
                        } catch (IOException e) {
                            String text = "Erro ao obter a imagem do mapa!";
                            Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
                            Log.e(TAG, text, e);
                        }
                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(SolicitarTransporteService.BASE_URL)
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();

                        SolicitarTransporteService service = retrofit.create(SolicitarTransporteService.class);
                        Empresa obj_empresa = (Empresa) spinner_empresa.getSelectedItem();
                        CentroCusto obj_centro = (CentroCusto) mSpinnerCentrosCustos.getSelectedItem();
                        showProgressDialog();
                        Call<ResponseBody> response = service.solicitar(new SolicitarTransporteData(tipo_diaria, categoria_carro, observacoes, origem, destino, retornarOrigem, retorno, false, distancia, valor, colaborador_final_id, data + " " + horario,cidade_origem,cidade_destino, cidadeRetorno, obj_empresa.getId(),obj_centro.getId(),duracao, snapshot));
                        response.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                try {

                                    JSONObject response_json = new JSONObject(response.body().string());
                                    System.out.println("Teste:" + response_json.toString());
                                    if (response_json.getString("status").equals("SUCCESS")) {
                                        progress.hide();
                                        Toast.makeText(getActivity(), "Solicitação de Transporte criada com sucesso!", Toast.LENGTH_LONG).show();
                                        dialog.dismiss();
                                        Intent i = new Intent(getActivity(), MainActivity.class);
                                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        getActivity().startActivity(i);
                                    }
                                    else {
                                        JSONArray array = response_json.getJSONArray("messages");
                                        if (array != null) {
                                            String message = (String) array.get(0);
                                            if (!TextUtils.isEmpty(message)) {
                                                Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                                            }
                                        }
                                        progress.hide();
                                        dialog.dismiss();
                                    }
                                } catch (Exception e) {
                                    progress.hide();
                                    e.printStackTrace();
                                    Toast.makeText(getActivity(), "Houve um erro de processamento...", Toast.LENGTH_LONG).show();
                                }
                            }
                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                progress.hide();

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
        mMapView.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PHONE_STATE) {
            boolean granted = grantedResults(grantResults);
            if (granted) {
                loadCentroCusto();
            }
        }
        else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
