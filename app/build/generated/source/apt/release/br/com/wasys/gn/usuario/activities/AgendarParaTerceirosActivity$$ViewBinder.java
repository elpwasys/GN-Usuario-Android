// Generated code from Butter Knife. Do not modify!
package br.com.wasys.gn.usuario.activities;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class AgendarParaTerceirosActivity$$ViewBinder<T extends br.com.wasys.gn.usuario.activities.AgendarParaTerceirosActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131493000, "field 'spinner_pessoas'");
    target.spinner_pessoas = finder.castView(view, 2131493000, "field 'spinner_pessoas'");
    view = finder.findRequiredView(source, 2131493001, "method 'Agendar'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.Agendar(p0);
        }
      });
    view = finder.findRequiredView(source, 2131492998, "method 'Agendar'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.Agendar(p0);
        }
      });
  }

  @Override public void unbind(T target) {
    target.spinner_pessoas = null;
  }
}
