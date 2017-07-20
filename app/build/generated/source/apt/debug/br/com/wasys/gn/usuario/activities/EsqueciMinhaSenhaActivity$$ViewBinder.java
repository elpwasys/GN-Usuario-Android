// Generated code from Butter Knife. Do not modify!
package br.com.wasys.gn.usuario.activities;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class EsqueciMinhaSenhaActivity$$ViewBinder<T extends br.com.wasys.gn.usuario.activities.EsqueciMinhaSenhaActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131493089, "field 'cpf'");
    target.cpf = finder.castView(view, 2131493089, "field 'cpf'");
    view = finder.findRequiredView(source, 2131493090, "field 'data_de_nascimento'");
    target.data_de_nascimento = finder.castView(view, 2131493090, "field 'data_de_nascimento'");
    view = finder.findRequiredView(source, 2131493091, "field 'senha'");
    target.senha = finder.castView(view, 2131493091, "field 'senha'");
    view = finder.findRequiredView(source, 2131493092, "field 'confirmacao_senha'");
    target.confirmacao_senha = finder.castView(view, 2131493092, "field 'confirmacao_senha'");
    view = finder.findRequiredView(source, 2131493088, "field 'txt_invalid_message'");
    target.txt_invalid_message = finder.castView(view, 2131493088, "field 'txt_invalid_message'");
    view = finder.findRequiredView(source, 2131493093, "method 'btnCadastrar'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.btnCadastrar();
        }
      });
  }

  @Override public void unbind(T target) {
    target.cpf = null;
    target.data_de_nascimento = null;
    target.senha = null;
    target.confirmacao_senha = null;
    target.txt_invalid_message = null;
  }
}
