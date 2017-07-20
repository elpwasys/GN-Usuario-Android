// Generated code from Butter Knife. Do not modify!
package br.com.wasys.gn.usuario.activities;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class AvaliacaoMotoristaActivity$$ViewBinder<T extends br.com.wasys.gn.usuario.activities.AvaliacaoMotoristaActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131493024, "field 'txt_codigo'");
    target.txt_codigo = finder.castView(view, 2131493024, "field 'txt_codigo'");
    view = finder.findRequiredView(source, 2131493027, "field 'txt_partida'");
    target.txt_partida = finder.castView(view, 2131493027, "field 'txt_partida'");
    view = finder.findRequiredView(source, 2131493030, "field 'txt_chegada'");
    target.txt_chegada = finder.castView(view, 2131493030, "field 'txt_chegada'");
    view = finder.findRequiredView(source, 2131493056, "field 'lbl_valor'");
    target.lbl_valor = finder.castView(view, 2131493056, "field 'lbl_valor'");
    view = finder.findRequiredView(source, 2131493041, "field 'lbl_franquia'");
    target.lbl_franquia = finder.castView(view, 2131493041, "field 'lbl_franquia'");
    view = finder.findRequiredView(source, 2131493009, "field 'txt_motorista'");
    target.txt_motorista = finder.castView(view, 2131493009, "field 'txt_motorista'");
    view = finder.findRequiredView(source, 2131493012, "field 'txt_carro'");
    target.txt_carro = finder.castView(view, 2131493012, "field 'txt_carro'");
    view = finder.findRequiredView(source, 2131493015, "field 'txt_tipo_transporte'");
    target.txt_tipo_transporte = finder.castView(view, 2131493015, "field 'txt_tipo_transporte'");
    view = finder.findRequiredView(source, 2131493019, "field 'txt_meia_diaria'");
    target.txt_meia_diaria = finder.castView(view, 2131493019, "field 'txt_meia_diaria'");
    view = finder.findRequiredView(source, 2131493059, "field 'txt_empresa'");
    target.txt_empresa = finder.castView(view, 2131493059, "field 'txt_empresa'");
    view = finder.findRequiredView(source, 2131493061, "field 'txt_simei'");
    target.txt_simei = finder.castView(view, 2131493061, "field 'txt_simei'");
    view = finder.findRequiredView(source, 2131493034, "field 'nota'");
    target.nota = finder.castView(view, 2131493034, "field 'nota'");
    view = finder.findRequiredView(source, 2131493035, "field 'comentarios'");
    target.comentarios = finder.castView(view, 2131493035, "field 'comentarios'");
    view = finder.findRequiredView(source, 2131492991, "field 'txt_data_agendamento'");
    target.txt_data_agendamento = finder.castView(view, 2131492991, "field 'txt_data_agendamento'");
    view = finder.findRequiredView(source, 2131493021, "field 'txt_distancia'");
    target.txt_distancia = finder.castView(view, 2131493021, "field 'txt_distancia'");
    view = finder.findRequiredView(source, 2131493062, "method 'Enviar'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.Enviar(p0);
        }
      });
  }

  @Override public void unbind(T target) {
    target.txt_codigo = null;
    target.txt_partida = null;
    target.txt_chegada = null;
    target.lbl_valor = null;
    target.lbl_franquia = null;
    target.txt_motorista = null;
    target.txt_carro = null;
    target.txt_tipo_transporte = null;
    target.txt_meia_diaria = null;
    target.txt_empresa = null;
    target.txt_simei = null;
    target.nota = null;
    target.comentarios = null;
    target.txt_data_agendamento = null;
    target.txt_distancia = null;
  }
}
