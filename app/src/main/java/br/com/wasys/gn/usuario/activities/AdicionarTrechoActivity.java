package br.com.wasys.gn.usuario.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import br.com.wasys.gn.usuario.R;
import br.com.wasys.gn.usuario.adapters.GooglePlacesAutocompleteAdapter;
import br.com.wasys.gn.usuario.helpers.Helper;
import br.com.wasys.gn.usuario.models.Endereco;
import br.com.wasys.gn.usuario.models.Trecho;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.com.elp.library.utils.DateUtils;
import br.com.elp.library.utils.FieldUtils;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;

public class AdicionarTrechoActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    @Bind(R.id.txt_horario_agendamento)
    EditText txt_horario_agendamento;
    @Bind(R.id.txt_origem)
    AutoCompleteTextView txt_origem;
    @Bind(R.id.txt_data_agendamento)
    EditText txt_data_agendamento;
    @Bind(R.id.txt_destino)
    AutoCompleteTextView txt_destino;
    @Bind(R.id.switch_pernoite)
    SwitchCompat switch_pernoite;
    public static final int HISTORICO_DESTINO = 123;
    public static final int HISTORICO_ORIGEM = 664;
    Context mContext;


    private int mYear, mMonth, mDay, mHour, mMinute;

    public static final String EXTRA_KEY_TRECHOS = AdicionarTrechoActivity.class.getName() + ".trechos";
    public static final String EXTRA_KEY_ENDERECO_INICIO = AdicionarTrechoActivity.class.getName() + ".inicio";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_trecho);
        ButterKnife.bind(this);
        setupActionBar();

        mContext = this;

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            Endereco endereco = (Endereco) extras.getSerializable(EXTRA_KEY_ENDERECO_INICIO);
            if (endereco != null) {
                FieldUtils.setText(txt_origem, endereco.completo);
                txt_origem.setEnabled(false);
            }
        }

        txt_origem.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    hideKeyboard();
                    return true;
                }
                return false;
            }
        });

        txt_destino.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    hideKeyboard();
                    return true;
                }
                return false;
            }
        });

        txt_origem.setOnItemClickListener(this);
        txt_origem.setAdapter(new GooglePlacesAutocompleteAdapter(this, R.layout.autocomplete_list_item));

        txt_destino.setOnItemClickListener(this);
        txt_destino.setAdapter(new GooglePlacesAutocompleteAdapter(this, R.layout.autocomplete_list_item));
    }

    public void hideKeyboard() {
        Helper.hideSoftKeyboard(this);
    }

    public void onItemClick(AdapterView adapterView, View view, int position, long id) {
        String str = (String) adapterView.getItemAtPosition(position);
        hideKeyboard();
        //Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    public void setupActionBar() {
        android.support.v7.app.ActionBar app = getSupportActionBar();
        app.setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        app.setCustomView(R.layout.action_bar);
        app.setDisplayHomeAsUpEnabled(true);
        ((TextView) app.getCustomView().findViewById(R.id.actionbar_title)).setText("Adicionar Trecho");
    }

    @OnClick(R.id.btn_confirmar)
    public void onConfirmarClick() {
        Trecho trecho = create(true);
        if (trecho != null) {
            Context context = getBaseContext();
            Geocoder geocoder = new Geocoder(this);
            try {
                List<Address> addresses;
                // ORIGEM
                addresses = geocoder.getFromLocationName(trecho.inicio.completo, 1);
                if (CollectionUtils.isNotEmpty(addresses)) {
                    Address address = addresses.get(0);
                    trecho.inicio.cidade = address.getLocality();
                    trecho.inicio.latitude = address.getLatitude();
                    trecho.inicio.longitude = address.getLongitude();
                }
                addresses = geocoder.getFromLocationName(trecho.termino.completo, 1);
                if (CollectionUtils.isNotEmpty(addresses)) {
                    Address address = addresses.get(0);
                    trecho.termino.cidade = address.getLocality();
                    trecho.termino.latitude = address.getLatitude();
                    trecho.termino.longitude = address.getLongitude();
                }
                Intent intent = new Intent();
                Bundle extras = new Bundle();
                extras.putSerializable(EXTRA_KEY_TRECHOS, trecho);
                intent.putExtras(extras);
                setResult(RESULT_OK, intent);
                finish();
            } catch (IOException e) {
                Toast.makeText(context, ExceptionUtils.getRootCauseMessage(e), Toast.LENGTH_LONG).show();
            }
        }
    }

    private Trecho create(boolean showErrors) {
        StringBuilder errors = new StringBuilder();
        // Origem
        String origem = FieldUtils.getValue(txt_origem);
        if (StringUtils.isBlank(origem)) {
            errors.append(getString(R.string.msg_campo_obrigatorio, getString(R.string.origem)));
        }
        // Data
        Date data = FieldUtils.getValue(Date.class, txt_data_agendamento);
        if (data == null) {
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
        // Valida e cria o Trecho
        Trecho trecho = null;
        if (StringUtils.isNotBlank(errors)) {
            if (showErrors) {
                Toast.makeText(this, errors, Toast.LENGTH_LONG).show();
            }
        }
        else {
            trecho = new Trecho();
            trecho.data = data;
            trecho.horario = horario;
            trecho.inicio = new Endereco(origem);
            trecho.termino = new Endereco(destino);
            trecho.pernoite = switch_pernoite.isChecked();
        }
        return trecho;
    }

    public void showCalendar() {
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

                        c.set(Calendar.YEAR, year);
                        c.set(Calendar.DATE, dayOfMonth);
                        c.set(Calendar.MONTH, monthOfYear);

                        Date date = c.getTime();

                        txt_data_agendamento.setText(DateUtils.format(date, DateUtils.DateType.DATE_BR.getPattern()));
                        hideKeyboard();

                    }
                }, mYear, mMonth, mDay);
        DatePicker dp = datePickerDialog.getDatePicker();
        dp.setMinDate(c.getTimeInMillis());
        datePickerDialog.show();
    }

    public void showHourPicker() {
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, AlertDialog.THEME_TRADITIONAL,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        String minute_string = null;
                        if (minute < 10) {
                            minute_string = "0" + minute;
                        } else {
                            minute_string = String.valueOf(minute);
                        }
                        txt_horario_agendamento.setText(hourOfDay + ":" + minute_string);
                        hideKeyboard();
                    }
                }, mHour, mMinute, true);
        timePickerDialog.show();
    }

    @OnFocusChange({R.id.txt_data_agendamento, R.id.txt_horario_agendamento})
    public void changeValue(View v, boolean hasFocus) {
        if (hasFocus) {
            switch (v.getId()) {
                case R.id.txt_data_agendamento:
                    showCalendar();
                    break;
                case R.id.txt_horario_agendamento:
                    showHourPicker();
                    break;
            }

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == HISTORICO_DESTINO) {
                System.out.println("Printar o negocio");
                System.out.println(data.getStringExtra("endereco"));
                System.out.println("Destino");
                txt_destino.setText(data.getStringExtra("endereco"));

            } else if (requestCode == HISTORICO_ORIGEM) {
                System.out.println("Printar a origem");
                System.out.println(data.getStringExtra("endereco"));
                txt_origem.setText(data.getStringExtra("endereco"));
                System.out.println("Origem");
            }
        } catch (Exception e) {
            Toast.makeText(mContext, "Nenhum endereço selecionado", Toast.LENGTH_LONG).show();
        }
    }

    @OnClick({R.id.btn_historico_origem, R.id.btn_historico_destino, R.id.txt_data_agendamento, R.id.txt_horario_agendamento})
    public void historico(View view) {

        Intent i = new Intent(this, HistoricoEnderecoActivity.class);
        switch (view.getId()) {

            case R.id.btn_historico_destino:
                startActivityForResult(i, HISTORICO_DESTINO);
                break;
            case R.id.btn_historico_origem:
                startActivityForResult(i, HISTORICO_ORIGEM);
                break;
            case R.id.txt_data_agendamento:
                showCalendar();
                break;
            case R.id.txt_horario_agendamento:
                showHourPicker();
                break;
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
}
