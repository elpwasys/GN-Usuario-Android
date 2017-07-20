package br.com.wasys.gn.usuario.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import br.com.wasys.gn.usuario.R;
import br.com.wasys.gn.usuario.models.Trecho;

import java.util.List;

import br.com.elp.library.adapter.ListAdapter;
import br.com.elp.library.utils.FieldUtils;
import butterknife.ButterKnife;

/**
 * @author pascke
 */
public class TrechosAdapter extends ListAdapter<Trecho> implements View.OnClickListener {

    public TrechosAdapter(Context context, List<Trecho> rows) {
        super(context, rows);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;

        if (view != null) {
            holder = (ViewHolder) view.getTag();
        }
        else {
            view = inflater.inflate(R.layout.trecho, null);
            holder = new ViewHolder();
            holder.numeroTextView = ButterKnife.findById(view, R.id.numtrecho);
            holder.origemTextView = ButterKnife.findById(view, R.id.lbl_origem);
            holder.destinoTextView = ButterKnife.findById(view, R.id.lbl_destino);
            holder.excluirButton = ButterKnife.findById(view, R.id.btn_remove_trecho);
            holder.excluirButton.setOnClickListener(this);
            view.setTag(holder);
        }
        Trecho trecho = rows.get(position);
        holder.position = position;
        FieldUtils.setText(holder.numeroTextView, (position + 1));
        FieldUtils.setText(holder.origemTextView, trecho.inicio.completo);
        FieldUtils.setText(holder.destinoTextView, trecho.termino.completo);
        int visibility = View.INVISIBLE;
        if ((rows.size() == 0) || (position == rows.size() - 1)) {
            visibility = View.VISIBLE;
        }
        holder.excluirButton.setTag(holder);
        holder.excluirButton.setVisibility(visibility);
        return view;
    }

    @Override
    public void onClick(View view) {
        if (view instanceof Button) {
            Button button = (Button) view;
            ViewHolder holder = (ViewHolder) button.getTag();
            rows.remove(holder.position);
            notifyDataSetChanged();
        }
    }

    static class ViewHolder {
        int position;
        Button excluirButton;
        TextView numeroTextView;
        TextView origemTextView;
        TextView destinoTextView;
    }
}
