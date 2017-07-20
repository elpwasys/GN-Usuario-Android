package br.com.wasys.gn.usuario.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import br.com.wasys.gn.usuario.R;
import br.com.wasys.gn.usuario.activities.AdicionarTrechoActivity;
import br.com.wasys.gn.usuario.activities.MainActivity;
import br.com.wasys.gn.usuario.activities.SolicitacaoActivity;
import br.com.wasys.gn.usuario.adapters.TrechosAdapter;
import br.com.wasys.gn.usuario.models.Colaborador;
import br.com.wasys.gn.usuario.models.Solicitacao;
import br.com.wasys.gn.usuario.models.Trecho;

import org.apache.commons.collections4.CollectionUtils;

import java.util.LinkedList;
import java.util.List;

import br.com.elp.library.utils.TypeUtils;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by fernandamoncores on 3/28/16.
 */
public class Multiplos extends Fragment {

    View dialogView;
    ListView mListView;
    Context c;
    TrechosAdapter mAdapter;
    public String colaborador_final_id;
    @Bind(R.id.checkbox_executivo) RadioButton mCheckboxExecutivo;
    ProgressDialog progress;

    private List<Trecho> mTrechos;

    private static final int REQUEST_CODE_TRECHO = 1;
    private static final int REQUEST_CODE_SOLICITACAO = 2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.c = this.getContext();
        dialogView = inflater.inflate(R.layout.dialog_multiplos, container, false);
        View android = inflater.inflate(R.layout.multiplos, container, false);
        ButterKnife.bind(this, android);

        mTrechos = new LinkedList<>();
        mAdapter = new TrechosAdapter(getContext(), mTrechos);
        mListView = (ListView) android.findViewById(R.id.list_trechos);
        mListView.setAdapter(mAdapter);

        return android;
    }

    public void showProgressDialog()
    {
        progress = new ProgressDialog(getActivity());
        progress.setMessage("Processando....");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        //progress.setIndeterminate(true);
        progress.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_TRECHO: {
                    Bundle extras = intent.getExtras();
                    Trecho trecho = (Trecho) extras.getSerializable(AdicionarTrechoActivity.EXTRA_KEY_TRECHOS);
                    mTrechos.add(trecho);
                    mAdapter.notifyDataSetChanged();
                    break;
                }
                case REQUEST_CODE_SOLICITACAO: {
                    returnToMain();
                    break;
                }
            }
        }
    }

    private void returnToMain() {
        FragmentActivity activity = getActivity();
        Intent intent = new Intent(activity, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
    }

    @OnClick({R.id.btn_calcular, R.id.btn_adicionar_trecho})
    public void btnClick(View view) {
        FragmentActivity activity = getActivity();
        switch (view.getId()) {
            case R.id.btn_adicionar_trecho: {
                Intent intent = new Intent(activity, AdicionarTrechoActivity.class);
                if (CollectionUtils.isNotEmpty(mTrechos)) {
                    Trecho trecho = mTrechos.get(mTrechos.size() - 1);
                    Bundle extras = new Bundle();
                    extras.putSerializable(AdicionarTrechoActivity.EXTRA_KEY_ENDERECO_INICIO, trecho.termino);
                    intent.putExtras(extras);
                }
                startActivityForResult(intent, REQUEST_CODE_TRECHO);
                break;
            }
            case R.id.btn_calcular: {
                if (CollectionUtils.isEmpty(mTrechos)) {
                    String message = getString(R.string.msg_campo_obrigatorio, getString(R.string.trecho));
                    Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                }
                else {
                    int last = mTrechos.size() - 1;
                    Trecho trecho = mTrechos.get(last);
                    if (trecho.pernoite) {
                        String message = getString(R.string.msg_ultimo_trecho_nao_pernoite);
                        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Solicitacao solicitacao = new Solicitacao();
                        solicitacao.trechos = mTrechos;
                        solicitacao.tipo = Solicitacao.Tipo.PERNOITE;
                        solicitacao.modo = Solicitacao.Modo.MULTIPLOS;
                        solicitacao.categoria = Solicitacao.Categoria.INTERMEDIARIO;
                        if (mCheckboxExecutivo.isChecked()) {
                            solicitacao.categoria = Solicitacao.Categoria.EXECUTIVO;
                        }
                        solicitacao.colaborador = new Colaborador(TypeUtils.parse(Long.class, colaborador_final_id));
                        Bundle extras = new Bundle();
                        extras.putSerializable(SolicitacaoActivity.EXTRA_KEY_SOLICITACAO, solicitacao);
                        Intent intent = new Intent(activity, SolicitacaoActivity.class);
                        intent.putExtras(extras);
                        startActivityForResult(intent, REQUEST_CODE_SOLICITACAO);
                    }
                }
                break;
            }
        }
    }
}
