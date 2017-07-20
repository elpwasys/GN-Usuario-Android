package br.com.wasys.gn.usuario.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import br.com.wasys.gn.usuario.R;
import br.com.wasys.gn.usuario.models.Empresa;

import java.util.ArrayList;

/**
 * Created by fernandamoncores on 5/5/16.
 */
public class EmpresasAdapter extends BaseAdapter {

    private Context context;
    public ArrayList<Empresa> dados = new ArrayList<Empresa>();


    public EmpresasAdapter(Context c, ArrayList<Empresa> dados)
    {
        this.context = c;
        this.dados = dados;
    }

    @Override
    public int getCount() {
        return dados.size();
    }

    @Override
    public Empresa getItem(int position) {
        return dados.get(position);
    }

    @Override
    public long getItemId(int position) {
        Empresa obj = dados.get(position);
        return Long.parseLong(obj.getId());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        System.out.println("Numero de carros:"+dados.size());

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        convertView = layoutInflater.inflate(R.layout.item_lista_historico, null);
        Empresa obj_carro = dados.get(position);
        TextView txt_opcao = (TextView) convertView.findViewById(R.id.txt_opcao);
        txt_opcao.setText(obj_carro.getNome());

        TextView txt_tipo = (TextView) convertView.findViewById(R.id.txt_tipo);
        txt_tipo.setText("");
        return convertView;
    }
}
