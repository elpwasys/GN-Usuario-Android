// Generated code from Butter Knife. Do not modify!
package br.com.wasys.gn.usuario.activities;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class AdicionarTrechoActivity$$ViewBinder<T extends br.com.wasys.gn.usuario.activities.AdicionarTrechoActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
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
    view = finder.findRequiredView(source, 2131492990, "field 'txt_origem'");
    target.txt_origem = finder.castView(view, 2131492990, "field 'txt_origem'");
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
    view = finder.findRequiredView(source, 2131492994, "field 'txt_destino'");
    target.txt_destino = finder.castView(view, 2131492994, "field 'txt_destino'");
    view = finder.findRequiredView(source, 2131492995, "field 'switch_pernoite'");
    target.switch_pernoite = finder.castView(view, 2131492995, "field 'switch_pernoite'");
    view = finder.findRequiredView(source, 2131492996, "method 'onConfirmarClick'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onConfirmarClick();
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
    target.txt_horario_agendamento = null;
    target.txt_origem = null;
    target.txt_data_agendamento = null;
    target.txt_destino = null;
    target.switch_pernoite = null;
  }
}
