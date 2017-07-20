package br.com.wasys.gn.usuario.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import br.com.wasys.gn.usuario.R;
import br.com.wasys.gn.usuario.adapters.GooglePlacesAutocompleteAdapter;
import br.com.wasys.gn.usuario.adapters.ServicoAdapter;
import br.com.wasys.gn.usuario.helpers.Helper;
import br.com.wasys.gn.usuario.models.Servico;
import br.com.wasys.gn.usuario.services.CalcularTransladoData;
import br.com.wasys.gn.usuario.services.CalcularTransladoService;
import br.com.wasys.gn.usuario.services.TransladoServicosService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TransladoActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    Context c;
    ServicoAdapter adapter;
    ArrayList<Servico> dados = new ArrayList<Servico>();
    @Bind(R.id.spinner_servicos) Spinner spinner_servicos;
    private int mYear, mMonth, mDay, mHour, mMinute;
    @Bind(R.id.txt_data_agendamento)
    EditText txt_data_agendamento;
    @Bind(R.id.checkbox_executivo)
    RadioButton checkbox_executivo;
    @Bind(R.id.txt_horario_agendamento) EditText txt_horario_agendamento;
    @Bind(R.id.txt_origem) AutoCompleteTextView txt_origem;
    ProgressDialog progress;
    String colaborador_final_id;

    public static final int HISTORICO_ORIGEM = 664;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translado);
        ButterKnife.bind(this);
        this.c = this;
        txt_origem.setOnItemClickListener(this);
        txt_origem.setAdapter(new GooglePlacesAutocompleteAdapter(this, R.layout.autocomplete_list_item));
        setupActionBar();
        loadData();
    }

    public void showProgressDialog()
    {
        progress = new ProgressDialog(this);
        progress.setMessage("Carregando....");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        //progress.setIndeterminate(true);
        progress.show();

    }


    public void showCalendar()
    {
        final Calendar c = Calendar.getInstance();
        //c.add(Calendar.DATE, 1);
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        txt_data_agendamento.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        // txt_data_agendamento.setText(sd1.format(new Date(year,monthOfYear,dayOfMonth)));

                    }
                }, mYear, mMonth, mDay);
        DatePicker dp = datePickerDialog.getDatePicker();
        dp.setMinDate(c.getTimeInMillis());
        datePickerDialog.show();
    }

    public void showHourPicker()
    {
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, AlertDialog.THEME_TRADITIONAL,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        txt_horario_agendamento.setText(hourOfDay + ":" + minute);
                    }
                }, mHour, mMinute, true);
        timePickerDialog.show();
    }

    @OnFocusChange({R.id.txt_data_agendamento, R.id.txt_horario_agendamento})
    public void changeValue(View v, boolean hasFocus)
    {
        if(hasFocus)
        {
            switch (v.getId())
            {
                case R.id.txt_data_agendamento:
                    showCalendar();
                    break;
                case R.id.txt_horario_agendamento:
                    showHourPicker();
                    break;
            }

        }
    }


    public void loadData()
    {
        showProgressDialog();
        System.out.println("Entrei aquiiiiiii no servicos");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(TransladoServicosService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TransladoServicosService service = retrofit.create(TransladoServicosService.class);

        Call<ResponseBody> response = service.solicitar(Long.parseLong(Helper.current_user(this).getColaborador()));
        System.out.println("Mandei o id:" + Long.parseLong(Helper.current_user(this).getColaborador()));

        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {

                    System.out.println("Fiz a requisição");
                    JSONObject response_json = new JSONObject(response.body().string());
                    System.out.println("JSON RESPONSE:" + response_json.toString());
                    if (response_json.getString("status").equals("SUCCESS")) {
                        JSONArray colaboradores_json = response_json.getJSONObject("result").getJSONArray("servicos");
                        for (int i = 0; i < colaboradores_json.length(); i++) {
                            JSONObject obj_pessoa_json = colaboradores_json.getJSONObject(i);
                            Servico obj_servico = new Servico();
                            obj_servico.setId(obj_pessoa_json.getString("id"));
                            obj_servico.setNome(obj_pessoa_json.getString("nome"));
                            dados.add(obj_servico);
                        }
                        adapter = new ServicoAdapter(c,dados);
                        adapter.notifyDataSetChanged();
                        spinner_servicos.setAdapter(adapter);
                        progress.hide();
                    } else {

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    progress.hide();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                progress.hide();

                t.printStackTrace();
                System.out.println("Deu erro de requisição");

            }

        });

        Bundle b = getIntent().getExtras();
        colaborador_final_id = b.getString("colaborador_id", "");

    }

    public void setupActionBar()
    {
        android.support.v7.app.ActionBar app = getSupportActionBar();
        app.setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        app.setCustomView(R.layout.action_bar);
        app.setDisplayHomeAsUpEnabled(true);
        ((TextView)app.getCustomView().findViewById(R.id.actionbar_title)).setText("Adicionar Translado");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onItemClick(AdapterView adapterView, View view, int position, long id) {
        String str = (String) adapterView.getItemAtPosition(position);
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }


    @OnClick(R.id.btn_calcular)
    public void btnClick(View v)
    {

        showProgressDialog();
        System.out.println("Entrei aquiiiiiii no servicos");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(CalcularTransladoService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        String categoria_carro = "INTERMEDIARIO";
        if(checkbox_executivo.isChecked())
            categoria_carro = "EXECUTIVO";
        CalcularTransladoService service = retrofit.create(CalcularTransladoService.class);
        String colaborador_id = null;

        if (colaborador_final_id.isEmpty())
            colaborador_id = Helper.current_user(this).getColaborador();
        else
            colaborador_id = colaborador_final_id;

        final CalcularTransladoData obj = new CalcularTransladoData(colaborador_id,spinner_servicos.getSelectedItemId()+"",txt_origem.getText().toString(),categoria_carro ,txt_horario_agendamento.getText().toString(),txt_data_agendamento.getText().toString());



        Call<ResponseBody> response = service.calcular(obj);
        System.out.println("Mandei o id:" + Long.parseLong(Helper.current_user(this).getColaborador()));

        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {

                    System.out.println("Fiz a requisição");
                    JSONObject response_json = new JSONObject(response.body().string());
                    System.out.println("JSON RESPONSE:" + response_json.toString());
                    if (response_json.getString("status").equals("SUCCESS")) {

                        obj.distancia = response_json.getJSONObject("result").getString("distancia");
                        obj.valor= response_json.getJSONObject("result").getString("valor");
                        obj.duracao = response_json.getJSONObject("result").getString("duracao");
                        obj.destino = response_json.getJSONObject("result").getString("destino");

                        progress.hide();
                        Intent i = new Intent(c,ConfirmarTransladoDetalhesActivity.class);
                        Bundle mBundle = new Bundle();
                        mBundle.putSerializable("obj", obj);
                        i.putExtras(mBundle);
                        startActivity(i);

                    } else {
                        progress.hide();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    progress.hide();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                progress.hide();

                t.printStackTrace();
                System.out.println("Deu erro de requisição");

            }

        });


    }

    @OnClick({R.id.btn_historico_origem})
    public void historico(View view)
    {

        Intent intent = new Intent(this, HistoricoEnderecoActivity.class);
        startActivityForResult(intent,HISTORICO_ORIGEM);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == HISTORICO_ORIGEM) {
            txt_origem.setText(data.getStringExtra("endereco"));
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

//
//    @OnClick({R.id.btn_calcular, R.id.btn_adicionar_trecho})
//    public void btnClick(View v) {
//
//    }



}
