// Generated code from Butter Knife. Do not modify!
package br.com.wasys.gn.usuario.activities;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class LoginActivity$$ViewBinder<T extends br.com.wasys.gn.usuario.activities.LoginActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131493099, "field 'btn_activity_login_esqueci_minha_senha' and method 'onClick'");
    target.btn_activity_login_esqueci_minha_senha = finder.castView(view, 2131493099, "field 'btn_activity_login_esqueci_minha_senha'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131493096, "field 'login'");
    target.login = finder.castView(view, 2131493096, "field 'login'");
    view = finder.findRequiredView(source, 2131493097, "field 'senha'");
    target.senha = finder.castView(view, 2131493097, "field 'senha'");
    view = finder.findRequiredView(source, 2131493088, "field 'message'");
    target.message = finder.castView(view, 2131493088, "field 'message'");
    view = finder.findRequiredView(source, 2131493098, "method 'onClick'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
  }

  @Override public void unbind(T target) {
    target.btn_activity_login_esqueci_minha_senha = null;
    target.login = null;
    target.senha = null;
    target.message = null;
  }
}
