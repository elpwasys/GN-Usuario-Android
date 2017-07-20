// Generated code from Butter Knife. Do not modify!
package br.com.wasys.gn.usuario.activities;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class TransladoActivity$$ViewBinder<T extends br.com.wasys.gn.usuario.activities.TransladoActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131493133, "field 'spinner_servicos'");
    target.spinner_servicos = finder.castView(view, 2131493133, "field 'spinner_servicos'");
    view = finder.findRequiredView(source, 2131492991, "field 'txt_data_agendamento' and method 'changeValue'");
    target.txt_data_agendamento = finder.castView(view, 2131492991, "field 'txt_data_agendamento'");
    view.setOnFocusChangeListener(
      new android.view.View.OnFocusChangeListener() {
        @Override public void onFocusChange(
          android.view.View p0,
          boolean p1
        ) {
          target.changeValue(p0, p1);
        }
      });
    view = finder.findRequiredView(source, 2131493129, "field 'checkbox_executivo'");
    target.checkbox_executivo = finder.castView(view, 2131493129, "field 'checkbox_executivo'");
    view = finder.findRequiredView(source, 2131492992, "field 'txt_horario_agendamento' and method 'changeValue'");
    target.txt_horario_agendamento = finder.castView(view, 2131492992, "field 'txt_horario_agendamento'");
    view.setOnFocusChangeListener(
      new android.view.View.OnFocusChangeListener() {
        @Override public void onFocusChange(
          android.view.View p0,
          boolean p1
        ) {
          target.changeValue(p0, p1);
        }
      });
    view = finder.findRequiredView(source, 2131492990, "field 'txt_origem'");
    target.txt_origem = finder.castView(view, 2131492990, "field 'txt_origem'");
    view = finder.findRequiredView(source, 2131493134, "method 'btnClick'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.btnClick(p0);
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
  }

  @Override public void unbind(T target) {
    target.spinner_servicos = null;
    target.txt_data_agendamento = null;
    target.checkbox_executivo = null;
    target.txt_horario_agendamento = null;
    target.txt_origem = null;
  }
}
