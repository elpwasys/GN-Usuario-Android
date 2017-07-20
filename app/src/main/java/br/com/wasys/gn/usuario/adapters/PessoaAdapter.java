package br.com.wasys.gn.usuario.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import br.com.wasys.gn.usuario.R;
import br.com.wasys.gn.usuario.models.Pessoa;

import java.util.ArrayList;

/**
 * Created by fernandamoncores on 5/5/16.
 */
public class PessoaAdapter extends BaseAdapter {

    private Context context;
    public ArrayList<Pessoa> dados;

    public PessoaAdapter(Context c)
    {
        this.context = c;
    }
    public PessoaAdapter(Context c, ArrayList<Pessoa> results)
    {
        this.context = c;
        this.dados = results;
    }

    @Override
    public int getCount() {
        return dados.size();
    }

    @Override
    public Pessoa getItem(int position) {
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
        Pessoa obj = dados.get(position);

        TextView nome_pessoa = (TextView) convertView.findViewById(R.id.txt_nome_pessoa);
        nome_pessoa.setText(obj.getNome());

        return convertView;
    }


}
