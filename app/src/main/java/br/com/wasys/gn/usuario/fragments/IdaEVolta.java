package br.com.wasys.gn.usuario.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.SwitchCompat;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TimePicker;
import android.widget.Toast;

import br.com.wasys.gn.usuario.R;
import br.com.wasys.gn.usuario.activities.HistoricoEnderecoActivity;
import br.com.wasys.gn.usuario.adapters.GooglePlacesAutocompleteAdapter;
import br.com.wasys.gn.usuario.helpers.Helper;
import br.com.wasys.gn.usuario.services.CalcularIdaEVoltaData;
import br.com.wasys.gn.usuario.services.CalcularIdaEVoltaService;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import br.com.elp.library.utils.FieldUtils;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by fernandamoncores on 3/28/16.
 */
public class IdaEVolta extends Fragment implements AdapterView.OnItemClickListener {

    @Bind(R.id.txt_origem) AutoCompleteTextView txt_origem;
    @Bind(R.id.txt_destino) AutoCompleteTextView txt_destino;
    @Bind(R.id.txt_horario_agendamento) EditText txt_horario_agendamento;
    @Bind(R.id.txt_retorno) AutoCompleteTextView txt_retorno;
    @Bind(R.id.txt_data_agendamento) EditText txt_data_agendamento;
    @Bind(R.id.txt_observacao) EditText txt_observacao;
    @Bind(R.id.switch_user_agreement) SwitchCompat switch_observacao;
    @Bind(R.id.layout_calcular_transporte) LinearLayout layout_calcular_transporte;
    @Bind(R.id.checkbox_medio) RadioButton checkbox_medio;
    @Bind(R.id.checkbox_executivo) RadioButton checkbox_executivo;
    @Bind(R.id.btn_historico_retorno) Button btn_historico_retorno;
    private int mYear, mMonth, mDay, mHour, mMinute;
    SharedPreferences settings;
    public static final String PREFS_NAME = "AOP_PREFS";
    public static final int HISTORICO_DESTINO = 123;
    public static final int HISTORICO_ORIGEM = 664;
    public static final int HISTORICO_RETORNO = 665;
    private Context mContext;
    public int opcao_menu;
    public String colaborador_final_id;
    ProgressDialog progress;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View android = inflater.inflate(R.layout.ida_e_volta, container, false);
        ButterKnife.bind(this, android);

        txt_origem.setOnItemClickListener(this);
        txt_origem.setAdapter(new GooglePlacesAutocompleteAdapter(getActivity(), R.layout.autocomplete_list_item));

        txt_destino.setOnItemClickListener(this);
        txt_destino.setAdapter(new GooglePlacesAutocompleteAdapter(getActivity(), R.layout.autocomplete_list_item));

        txt_retorno.setOnItemClickListener(this);
        txt_retorno.setAdapter(new GooglePlacesAutocompleteAdapter(getActivity(), R.layout.autocomplete_list_item));


        txt_observacao.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    Helper.hideSoftKeyboard(getActivity());
                    return true;
                }
                return false;
            }
        });

        txt_origem.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    Helper.hideSoftKeyboard(getActivity());
                    return true;
                }
                return false;
            }
        });

        txt_retorno.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    Helper.hideSoftKeyboard(getActivity());
                    return true;
                }
                return false;
            }
        });

        return android;
    }



    public void onItemClick(AdapterView adapterView, View view, int position, long id) {
        String str = (String) adapterView.getItemAtPosition(position);
        Helper.hideSoftKeyboard(getActivity());
        Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
    }

    public void showCalendar()
    {
        Helper.hideSoftKeyboard(getActivity());
        txt_data_agendamento.setInputType(InputType.TYPE_NULL);
        final Calendar c = Calendar.getInstance();
        //c.add(Calendar.DATE, 1);
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this.getActivity(),
                 new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        txt_data_agendamento.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        Helper.hideSoftKeyboard(getActivity());
                       // txt_data_agendamento.setText(sd1.format(new Date(year,monthOfYear,dayOfMonth)));

                    }




                }, mYear, mMonth, mDay);
        DatePicker dp = datePickerDialog.getDatePicker();
        dp.setMinDate(c.getTimeInMillis());
        datePickerDialog.show();
    }

    public void showHourPicker()
    {
        Helper.hideSoftKeyboard(getActivity());
        txt_horario_agendamento.setInputType(InputType.TYPE_NULL);
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this.getActivity(), AlertDialog.THEME_TRADITIONAL,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        String minute_string = null;
                        if(minute < 10)
                        {
                            minute_string = "0"+minute;
                        }else
                        {
                            minute_string = String.valueOf(minute);
                        }
                        //String curTime = String.format("%02d:%02d", mHour, mMinute);
                        txt_horario_agendamento.setText(hourOfDay + ":" + minute_string);
                        Helper.hideSoftKeyboard(getActivity());
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

    @OnCheckedChanged(R.id.switch_user_agreement)
    public void valueSwitchChange(CompoundButton buttonView, boolean isChecked)
    {
        if(isChecked)
        {
            btn_historico_retorno.setVisibility(View.VISIBLE);
            txt_retorno.setVisibility(View.VISIBLE);
        }else
        {
            btn_historico_retorno.setVisibility(View.GONE);
            txt_retorno.setVisibility(View.GONE);
            txt_retorno.setText("");
        }
    }

    private boolean isValid(boolean showErrors) {
        StringBuilder errors = new StringBuilder();
        // Origem
        String origem = FieldUtils.getValue(txt_origem);
        if (StringUtils.isBlank(origem)) {
            errors.append(getString(R.string.msg_campo_obrigatorio, getString(R.string.origem)));
        }
        // Data
        String data = FieldUtils.getValue(txt_data_agendamento);
        if (StringUtils.isBlank(data)) {
            if (errors.length() > 0) {
                errors.append("\n");
            }
            errors.append(getString(R.string.msg_campo_obrigatorio, getString(R.string.data)));
        }
        // Horario
        String horario = FieldUtils.getValue(txt_horario_agendamento);
        if (StringUtils.isBlank(horario)) {
            if (errors.length() > 0) {
                errors.append("\n");
            }
            errors.append(getString(R.string.msg_campo_obrigatorio, getString(R.string.horario)));
        }
        // Destino
        String destino = FieldUtils.getValue(txt_destino);
        if (StringUtils.isBlank(destino)) {
            if (errors.length() > 0) {
                errors.append("\n");
            }
            errors.append(getString(R.string.msg_campo_obrigatorio, getString(R.string.destino)));
        }
        if (StringUtils.isBlank(errors)) {
            return true;
        }
        else {
            if (showErrors) {
                Context context = getContext();
                Toast.makeText(context, errors, Toast.LENGTH_LONG).show();
            }
            return false;
        }
    }

    @OnClick(R.id.btn_calcular)
    public void btnCalcular()
    {

        boolean valid = isValid(true);
        if (valid) {
            showProgressDialog();
            Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
            double distancia = 0;
            String cidade_origem = "";
            String cidade_destino = "";
            try {
                List<Address> addresses;

                Address origem = geocoder.getFromLocationName(txt_origem.getText().toString(), 10).get(0);
                addresses = geocoder.getFromLocation(origem.getLatitude(), origem.getLongitude(), 1);
                if (addresses.size() > 0) {
                    cidade_origem = addresses.get(0).getLocality();
                }
                if (StringUtils.isBlank(cidade_origem)) {
                    progress.hide();
                    AlertDialog dialog = new AlertDialog.Builder(getActivity())
                            .setTitle(R.string.endereco)
                            .setMessage(R.string.msg_cidade_origem_indefindida)
                            .create();
                    dialog.show();
                    return;
                }

                Address destino = geocoder.getFromLocationName(txt_destino.getText().toString(), 10).get(0);
                List<Address> addresses1 = geocoder.getFromLocation(destino.getLatitude(), destino.getLongitude(), 1);
                if (addresses.size() > 0)
                {
                    cidade_destino = addresses1.get(0).getLocality();
                }
                if (StringUtils.isBlank(cidade_destino)) {
                    progress.hide();
                    AlertDialog dialog = new AlertDialog.Builder(getActivity())
                            .setTitle(R.string.endereco)
                            .setMessage(R.string.msg_cidade_destino_indefindida)
                            .create();
                    dialog.show();
                    return;
                }

                Location loc1 = new Location("");
                loc1.setLatitude(origem.getLatitude());
                loc1.setLongitude(origem.getLongitude());

                Location loc2 = new Location("");
                loc2.setLatitude(destino.getLatitude());
                loc2.setLongitude(destino.getLongitude());

                distancia = loc1.distanceTo(loc2);





            }catch (Exception e)
            {
                distancia = 10;
                e.printStackTrace();
            }


            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(CalcularIdaEVoltaService.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            CalcularIdaEVoltaService service = retrofit.create(CalcularIdaEVoltaService.class);

            String categoria_carro = "INTERMEDIARIO";
            if(checkbox_executivo.isChecked())
                categoria_carro = "EXECUTIVO";

            boolean pernoite = false;
            String opcao_transporte = "DIARIA";
            switch (opcao_menu)
            {
                case 0:
                    opcao_transporte = "MEIA";
                    break;
                case 1:
                    opcao_transporte = "DIARIA";
                    break;
                case 2:
                    opcao_transporte = "PERNOITE";
                    pernoite = true;
                    break;
                case 3:
                    opcao_transporte = "TRANSLADO";
                    break;
                default:
                    opcao_transporte = "DIARIA";
                    break;
            }

            String distancia_string = distancia + "";

            final String cidadeOrigem = cidade_origem;
            final String cidadeDestino = cidade_destino;

            Call<ResponseBody> response = service.calcular(new CalcularIdaEVoltaData(opcao_transporte,categoria_carro, colaborador_final_id, txt_origem.getText().toString(), txt_destino.getText().toString(), pernoite, cidade_origem,cidade_destino,txt_retorno.getText().toString(),txt_observacao.getText().toString()));

            final String finalOpcao_transporte = opcao_transporte;
            final String finalCategoria_carro = categoria_carro;
            response.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {

                        progress.hide();
                        JSONObject response_json = new JSONObject(response.body().string());
                        System.out.println("Teste:"+response_json.toString());
                        if(response_json.getString("status").equals("SUCCESS"))
                        {

                            String distancia_string = response_json.getJSONObject("result").getString("distancia");

                            String valor = response_json.getJSONObject("result").getString("valor");
                            layout_calcular_transporte.setVisibility(View.GONE);
                            FragmentManager fm = getFragmentManager();
                            if(fm != null)
                            {
                                FragmentTransaction ft = fm.beginTransaction();
                                TransporteCalculado frag = new TransporteCalculado();
                                frag.origem = txt_origem.getText().toString();
                                frag.destino = txt_destino.getText().toString();
                                frag.horario = txt_horario_agendamento.getText().toString();
                                frag.data = txt_data_agendamento.getText().toString();
                                frag.valor = valor;
                                frag.distancia = distancia_string;
                                frag.tipo_diaria = finalOpcao_transporte;
                                frag.categoria_carro = finalCategoria_carro;
                                frag.disponivel = response_json.getJSONObject("result").getString("duracao");
                                frag.duracao = response_json.getJSONObject("result").getString("duracao");
                                frag.observacoes = txt_observacao.getText().toString();
                                frag.retorno = txt_retorno.getText().toString();
                                frag.colaborador_final_id = colaborador_final_id;
                                frag.cidade_origem = cidadeOrigem;
                                frag.cidade_destino = cidadeDestino;

                                ft.replace(R.id.layout_unico, frag);
                                ft.addToBackStack(null);
                                ft.commit();
                            }

                        }else
                        {
                            progress.hide();
                            Toast.makeText(getActivity(),response_json.getJSONArray("messages").getString(0),Toast.LENGTH_LONG).show();
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

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try{
            if(requestCode == HISTORICO_DESTINO)
            {
                txt_destino.setText(data.getStringExtra("endereco"));

            }else if(requestCode == HISTORICO_ORIGEM)
            {
                txt_origem.setText(data.getStringExtra("endereco"));
            }else if(requestCode == HISTORICO_RETORNO)
            {
                txt_retorno.setText(data.getStringExtra("endereco"));
            }
        }catch (Exception e)
        {
//            Toast.makeText(mContext,"Nenhum endereço selecionado",Toast.LENGTH_LONG).show();
        }

    }

    @OnClick({R.id.btn_historico_origem,R.id.btn_historico_destino,R.id.btn_historico_retorno, R.id.txt_horario_agendamento, R.id.txt_data_agendamento})
    public void historico(View view)
    {

        Intent i = new Intent(getActivity(), HistoricoEnderecoActivity.class);
        switch (view.getId())
        {

            case R.id.btn_historico_destino:
                startActivityForResult(i,HISTORICO_DESTINO);
                break;
            case R.id.btn_historico_origem:
                startActivityForResult(i,HISTORICO_ORIGEM);
                break;
            case R.id.btn_historico_retorno:
                startActivityForResult(i,HISTORICO_RETORNO);
                break;
            case R.id.txt_horario_agendamento:
                showHourPicker();
                break;
            case R.id.txt_data_agendamento:
                showCalendar();
                break;
        }

    }

    public void showProgressDialog()
    {
        progress = new ProgressDialog(getActivity());
        progress.setMessage("Processando....");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        //progress.setIndeterminate(true);
        progress.show();
    }






}
