package br.com.wasys.gn.usuario.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import br.com.wasys.gn.usuario.Permission;
import br.com.wasys.gn.usuario.R;
import br.com.wasys.gn.usuario.endpoint.Endpoint;
import br.com.wasys.gn.usuario.endpoint.RotaEndpoint;
import br.com.wasys.gn.usuario.endpoint.SolicitacaoEndpoint;
import br.com.wasys.gn.usuario.fragments.MapFragment;
import br.com.wasys.gn.usuario.fragments.TrechoFragment;
import br.com.wasys.gn.usuario.google.DirectionResult;
import br.com.wasys.gn.usuario.google.Leg;
import br.com.wasys.gn.usuario.google.Route;
import br.com.wasys.gn.usuario.models.Rota;
import br.com.wasys.gn.usuario.result.RotaResult;
import br.com.wasys.gn.usuario.models.Solicitacao;
import br.com.wasys.gn.usuario.models.Trecho;
import br.com.elp.library.utils.BitmapUtils;
import com.google.android.gms.maps.GoogleMap;

import java.io.IOException;

import br.com.elp.library.activity.AppActivity;
import br.com.elp.library.http.Callback;
import br.com.elp.library.http.Error;
import br.com.elp.library.http.Result;
import br.com.elp.library.utils.FieldUtils;
import br.com.elp.library.utils.NumberUtils;
import retrofit2.Call;

public class SolicitacaoActivity extends AppActivity implements MapFragment.MapCallback {

    private Rota mRota;
    private Solicitacao mSolicitacao;

    private View mMainView;
    private Button mOkButton;
    private TextView mKmTextView;
    private TextView mTempoTextView;
    private TextView mValorTextView;
    private MapFragment mMapFragment;

    private TrechoFragment mTrechoFragment;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;

    private static final int REQUEST_PHONE_STATE = 1;
    private static final String TAG = SolicitacaoActivity.class.getSimpleName();
    public static final String EXTRA_KEY_SOLICITACAO = AdicionarTrechoActivity.class.getName() + ".solicitacao";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitacao);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mMainView = findViewById(R.id.main_view);
        mOkButton = (Button) findViewById(R.id.ok_button);
        mKmTextView = (TextView) findViewById(R.id.km_text_view);
        mTempoTextView = (TextView) findViewById(R.id.tempo_text_view);
        mValorTextView = (TextView) findViewById(R.id.valor_text_view);
        mMapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mTrechoFragment = (TrechoFragment) getSupportFragmentManager().findFragmentById(R.id.trecho_fragment);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);

        mMapFragment.setMapCallback(this);
        mCollapsingToolbarLayout.setExpandedTitleColor(ContextCompat.getColor(this, android.R.color.transparent));

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mSolicitacao = (Solicitacao) extras.getSerializable(EXTRA_KEY_SOLICITACAO);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mSolicitacao != null) {
            mMapFragment.sincronizar(mSolicitacao.trechos);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PHONE_STATE) {
            boolean granted = grantedResults(grantResults);
            if (granted) {
                calcular();
            }
        }
        else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onDirectionLoaded(DirectionResult directionResult) {
        if (DirectionResult.Status.OK.equals(directionResult.status)) {
            Route route = directionResult.routes[0];
            for (int i = 0; i < route.legs.length; i++) {
                Leg leg = route.legs[i];
                Trecho trecho = mSolicitacao.trechos.get(i);
                trecho.distance = leg.distance;
                trecho.duration = leg.duration;
            }
            calcular();
            mTrechoFragment.popular(mSolicitacao.trechos);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void calcular() {
        if (!checkedSelfPermission(Permission.PHONE)) {
            ActivityCompat.requestPermissions(this, Permission.PHONE, REQUEST_PHONE_STATE);
            return;
        }
        RotaEndpoint endpoint = Endpoint.create(RotaEndpoint.class);
        Call<RotaResult> call = endpoint.calcular(mSolicitacao);
        call.enqueue(new Callback<RotaResult>() {
            @Override
            public void onSuccess(RotaResult result) {
                mRota = result.rota;
                popular();
            }
            @Override
            public void onError(Error error) {
                Context context = getBaseContext();
                Toast.makeText(context, error.getFormattedErrorMessage(), Toast.LENGTH_LONG).show();
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }

    private void popular() {
        if (mRota != null) {
            int horas = (int) (mRota.tempo / 3600);
            int minutos = ((int) (mRota.tempo / 60)) % 60;
            double distancia = mRota.km / 1000d;
            FieldUtils.setText(mKmTextView, getString(R.string.distancia_km, distancia));
            FieldUtils.setText(mValorTextView, mRota.valor);
            FieldUtils.setText(mTempoTextView, String.format("%d:%02d", horas, minutos));
            mOkButton.setEnabled(true);
            mMainView.setVisibility(View.VISIBLE);
        }
    }

    public void onClickSolicitar(View view) {

        Context context = getBaseContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View dialogView = inflater.inflate(R.layout.dialog_multiplos, null, false);
        TextView textView = (TextView) dialogView.findViewById(R.id.txt_mensagem);

        final Dialog dialog = new Dialog(this);
        dialog.setTitle("???");
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        String confirmacao = context.getString(R.string.msg_confirmacao_solicitacao_transporte, NumberUtils.format(mRota.valor));
        FieldUtils.setText(textView, confirmacao);

        dialog.setContentView(dialogView);
        dialog.show();

        Button okButton = (Button) dialog.findViewById(R.id.btn_ok);
        final Button cancelButton = (Button) dialog.findViewById(R.id.btn_cancel);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                solicitar();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void solicitar() {
        if (mSolicitacao != null) {
            final Context context = getBaseContext();
            mMapFragment.snapshot(new GoogleMap.SnapshotReadyCallback() {
                @Override
                public void onSnapshotReady(Bitmap bitmap) {
                    // Obtem a imagem do mapa
                    try {
                        mSolicitacao.snapshot = BitmapUtils.toBase64(bitmap);
                        SolicitacaoEndpoint endpoint = Endpoint.create(SolicitacaoEndpoint.class);
                        Call<Result> call = endpoint.salvar(mSolicitacao);
                        showProgress();
                        call.enqueue(new Callback<Result>() {
                            @Override
                            public void onSuccess(Result result) {
                                SolicitacaoActivity.this.hideProgress();
                                String message = getString(R.string.msg_sucesso_solicitacao_transporte);
                                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                                setResult(RESULT_OK);
                                SolicitacaoActivity.this.finish();
                            }
                            @Override
                            public void onError(Error error) {
                                SolicitacaoActivity.this.hideProgress();
                                Toast.makeText(context, error.getFormattedErrorMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    } catch (IOException e) {
                        String text = "Erro ao gerar a imagem do mapa!\nTente novamente.";
                        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
                        Log.e(TAG, text, e);
                    }
                }
            });
        }
    }
}