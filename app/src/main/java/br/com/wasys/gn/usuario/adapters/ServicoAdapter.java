package br.com.wasys.gn.usuario.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import br.com.wasys.gn.usuario.R;
import br.com.wasys.gn.usuario.models.Servico;

import java.util.ArrayList;

/**
 * Created by fernandamoncores on 5/10/16.
 */
public class ServicoAdapter extends BaseAdapter {

    private Context context;
    public ArrayList<Servico> dados;

    public ServicoAdapter(Context c)
    {
        this.context = c;
    }
    public ServicoAdapter(Context c, ArrayList<Servico> results)
    {
        this.context = c;
        this.dados = results;
    }

    @Override
    public int getCount() {
        return dados.size();
    }

    @Override
    public Servico getItem(int position) {
        return dados.get(position);
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(dados.get(position).getId());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        convertView = layoutInflater.inflate(R.layout.row, null);
        Servico obj = dados.get(position);

        TextView nome_servico = (TextView) convertView.findViewById(R.id.txt_nome_pessoa);
        nome_servico.setText(obj.getNome());

        return convertView;
    }



}
