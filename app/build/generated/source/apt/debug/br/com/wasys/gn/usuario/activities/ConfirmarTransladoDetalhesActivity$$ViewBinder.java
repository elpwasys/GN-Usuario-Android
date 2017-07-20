// Generated code from Butter Knife. Do not modify!
package br.com.wasys.gn.usuario.activities;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class ConfirmarTransladoDetalhesActivity$$ViewBinder<T extends br.com.wasys.gn.usuario.activities.ConfirmarTransladoDetalhesActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131493056, "field 'lbl_valor'");
    target.lbl_valor = finder.castView(view, 2131493056, "field 'lbl_valor'");
    view = finder.findRequiredView(source, 2131493070, "field 'txt_destino_bairro_cidade_estado'");
    target.txt_destino_bairro_cidade_estado = finder.castView(view, 2131493070, "field 'txt_destino_bairro_cidade_estado'");
    view = finder.findRequiredView(source, 2131492990, "field 'txt_origem'");
    target.txt_origem = finder.castView(view, 2131492990, "field 'txt_origem'");
    view = finder.findRequiredView(source, 2131492994, "field 'txt_destino'");
    target.txt_destino = finder.castView(view, 2131492994, "field 'txt_destino'");
    view = finder.findRequiredView(source, 2131492991, "field 'txt_data_agendamento'");
    target.txt_data_agendamento = finder.castView(view, 2131492991, "field 'txt_data_agendamento'");
    view = finder.findRequiredView(source, 2131492992, "field 'txt_horario_agendamento'");
    target.txt_horario_agendamento = finder.castView(view, 2131492992, "field 'txt_horario_agendamento'");
    view = finder.findRequiredView(source, 2131493073, "field 'txt_disponivel'");
    target.txt_disponivel = finder.castView(view, 2131493073, "field 'txt_disponivel'");
    view = finder.findRequiredView(source, 2131493075, "field 'txt_tipo_diaria'");
    target.txt_tipo_diaria = finder.castView(view, 2131493075, "field 'txt_tipo_diaria'");
    view = finder.findRequiredView(source, 2131493021, "field 'txt_distancia'");
    target.txt_distancia = finder.castView(view, 2131493021, "field 'txt_distancia'");
    view = finder.findRequiredView(source, 2131493077, "field 'spinner_empresa' and method 'selected'");
    target.spinner_empresa = finder.castView(view, 2131493077, "field 'spinner_empresa'");
    ((android.widget.AdapterView<?>) view).setOnItemSelectedListener(
      new android.widget.AdapterView.OnItemSelectedListener() {
        @Override public void onItemSelected(
          android.widget.AdapterView<?> p0,
          android.view.View p1,
          int p2,
          long p3
        ) {
          target.selected(p0, p1, p2, p3);
        }
        @Override public void onNothingSelected(
          android.widget.AdapterView<?> p0
        ) {
          
        }
      });
    view = finder.findRequiredView(source, 2131493079, "field 'spinner_centro_de_custo'");
    target.spinner_centro_de_custo = finder.castView(view, 2131493079, "field 'spinner_centro_de_custo'");
    view = finder.findRequiredView(source, 2131493080, "method 'btnClick'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.btnClick(p0);
        }
      });
  }

  @Override public void unbind(T target) {
    target.lbl_valor = null;
    target.txt_destino_bairro_cidade_estado = null;
    target.txt_origem = null;
    target.txt_destino = null;
    target.txt_data_agendamento = null;
    target.txt_horario_agendamento = null;
    target.txt_disponivel = null;
    target.txt_tipo_diaria = null;
    target.txt_distancia = null;
    target.spinner_empresa = null;
    target.spinner_centro_de_custo = null;
  }
}
