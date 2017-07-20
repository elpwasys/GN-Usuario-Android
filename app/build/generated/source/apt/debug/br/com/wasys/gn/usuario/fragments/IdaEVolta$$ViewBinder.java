// Generated code from Butter Knife. Do not modify!
package br.com.wasys.gn.usuario.fragments;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class IdaEVolta$$ViewBinder<T extends br.com.wasys.gn.usuario.fragments.IdaEVolta> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131492990, "field 'txt_origem'");
    target.txt_origem = finder.castView(view, 2131492990, "field 'txt_origem'");
    view = finder.findRequiredView(source, 2131492994, "field 'txt_destino'");
    target.txt_destino = finder.castView(view, 2131492994, "field 'txt_destino'");
    view = finder.findRequiredView(source, 2131492992, "field 'txt_horario_agendamento', method 'historico', and method 'changeValue'");
    target.txt_horario_agendamento = finder.castView(view, 2131492992, "field 'txt_horario_agendamento'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.historico(p0);
        }
      });
    view.setOnFocusChangeListener(
      new android.view.View.OnFocusChangeListener() {
        @Override public void onFocusChange(
          android.view.View p0,
          boolean p1
        ) {
          target.changeValue(p0, p1);
        }
      });
    view = finder.findRequiredView(source, 2131493161, "field 'txt_retorno'");
    target.txt_retorno = finder.castView(view, 2131493161, "field 'txt_retorno'");
    view = finder.findRequiredView(source, 2131492991, "field 'txt_data_agendamento', method 'historico', and method 'changeValue'");
    target.txt_data_agendamento = finder.castView(view, 2131492991, "field 'txt_data_agendamento'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.historico(p0);
        }
      });
    view.setOnFocusChangeListener(
      new android.view.View.OnFocusChangeListener() {
        @Override public void onFocusChange(
          android.view.View p0,
          boolean p1
        ) {
          target.changeValue(p0, p1);
        }
      });
    view = finder.findRequiredView(source, 2131493162, "field 'txt_observacao'");
    target.txt_observacao = finder.castView(view, 2131493162, "field 'txt_observacao'");
    view = finder.findRequiredView(source, 2131493159, "field 'switch_observacao' and method 'valueSwitchChange'");
    target.switch_observacao = finder.castView(view, 2131493159, "field 'switch_observacao'");
    ((android.widget.CompoundButton) view).setOnCheckedChangeListener(
      new android.widget.CompoundButton.OnCheckedChangeListener() {
        @Override public void onCheckedChanged(
          android.widget.CompoundButton p0,
          boolean p1
        ) {
          target.valueSwitchChange(p0, p1);
        }
      });
    view = finder.findRequiredView(source, 2131493067, "field 'layout_calcular_transporte'");
    target.layout_calcular_transporte = finder.castView(view, 2131493067, "field 'layout_calcular_transporte'");
    view = finder.findRequiredView(source, 2131493128, "field 'checkbox_medio'");
    target.checkbox_medio = finder.castView(view, 2131493128, "field 'checkbox_medio'");
    view = finder.findRequiredView(source, 2131493129, "field 'checkbox_executivo'");
    target.checkbox_executivo = finder.castView(view, 2131493129, "field 'checkbox_executivo'");
    view = finder.findRequiredView(source, 2131493160, "field 'btn_historico_retorno' and method 'historico'");
    target.btn_historico_retorno = finder.castView(view, 2131493160, "field 'btn_historico_retorno'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.historico(p0);
        }
      });
    view = finder.findRequiredView(source, 2131493134, "method 'btnCalcular'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.btnCalcular();
        }
      });
    view = finder.findRequiredView(source, 2131492989, "method 'historico'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.historico(p0);
        }
      });
    view = finder.findRequiredView(source, 2131492993, "method 'historico'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.historico(p0);
        }
      });
  }

  @Override public void unbind(T target) {
    target.txt_origem = null;
    target.txt_destino = null;
    target.txt_horario_agendamento = null;
    target.txt_retorno = null;
    target.txt_data_agendamento = null;
    target.txt_observacao = null;
    target.switch_observacao = null;
    target.layout_calcular_transporte = null;
    target.checkbox_medio = null;
    target.checkbox_executivo = null;
    target.btn_historico_retorno = null;
  }
}
