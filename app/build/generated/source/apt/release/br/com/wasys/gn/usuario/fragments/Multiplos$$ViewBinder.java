// Generated code from Butter Knife. Do not modify!
package br.com.wasys.gn.usuario.fragments;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class Multiplos$$ViewBinder<T extends br.com.wasys.gn.usuario.fragments.Multiplos> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131493129, "field 'mCheckboxExecutivo'");
    target.mCheckboxExecutivo = finder.castView(view, 2131493129, "field 'mCheckboxExecutivo'");
    view = finder.findRequiredView(source, 2131493134, "method 'btnClick'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.btnClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131493179, "method 'btnClick'");
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
    target.mCheckboxExecutivo = null;
  }
}
