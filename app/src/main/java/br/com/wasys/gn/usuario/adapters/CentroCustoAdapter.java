package br.com.wasys.gn.usuario.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import br.com.wasys.gn.usuario.R;
import br.com.wasys.gn.usuario.models.CentroCusto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fernandamoncores on 5/5/16.
 */
public class CentroCustoAdapter extends BaseAdapter {

    private Context context;
    public List<CentroCusto> dados = new ArrayList<CentroCusto>();


    public CentroCustoAdapter(Context c)
    {
        this.context = c;
    }

    public CentroCustoAdapter(Context c, List<CentroCusto> dados)
    {
        this.context = c;
        this.dados = dados;
    }

    @Override
    public int getCount() {
        return dados.size();
    }

    @Override
    public CentroCusto getItem(int position) {
        return dados.get(position);
    }

    @Override
    public long getItemId(int position) {
        CentroCusto obj = dados.get(position);
        return Long.parseLong(obj.getId());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        convertView = layoutInflater.inflate(R.layout.item_lista_historico, null);
        CentroCusto obj = dados.get(position);
        TextView txt_opcao = (TextView) convertView.findViewById(R.id.txt_opcao);
        txt_opcao.setText(obj.getNome());
        TextView txt_tipo = (TextView) convertView.findViewById(R.id.txt_tipo);
        txt_tipo.setText("");
        return convertView;
    }
}
