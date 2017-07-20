package br.com.wasys.gn.usuario.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import br.com.wasys.gn.usuario.R;
import br.com.wasys.gn.usuario.models.HistoricoEndereco;

import java.util.ArrayList;

/**
 * Created by fernandamoncores on 5/5/16.
 */
public class HistoricoAdapter extends BaseAdapter {

    private Context context;
    public ArrayList<HistoricoEndereco> dados = new ArrayList<HistoricoEndereco>();


    public HistoricoAdapter(Context c)
    {
        this.context = c;
    }

    @Override
    public int getCount() {
        return dados.size();
    }

    @Override
    public HistoricoEndereco getItem(int position) {
        return dados.get(position);
    }

    @Override
    public long getItemId(int position) {
        HistoricoEndereco obj_endereco = dados.get(position);
        return 1l;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        System.out.println("Numero de carros:"+dados.size());

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        convertView = layoutInflater.inflate(R.layout.item_lista_historico, null);
        HistoricoEndereco historico = dados.get(position);

//        System.out.println("Nome do Carro:"+obj_carro.getNome());
//        System.out.println("Placa do Carro:" + obj_carro.getPlaca());
//
//        LinearLayout l = (LinearLayout) convertView.findViewById(R.id.layout_item);
//
//        String carro_default_id = Helper.get_current_carro_id(this.context);
//
//        if(carro_default_id != null && !carro_default_id.isEmpty() && obj_carro.getId().equals(carro_default_id))
//        {
//            l.setBackgroundColor(Color.parseColor("#aeb2b7"));
//        }
//
        TextView txt_opcao = (TextView) convertView.findViewById(R.id.txt_opcao);
        txt_opcao.setText(historico.getEndereco());

        TextView txt_tipo = (TextView) convertView.findViewById(R.id.txt_tipo);
        txt_tipo.setText(historico.getTipo());


//
//        TextView txt_placa = (TextView) convertView.findViewById(R.id.txt_placa_do_carro);
//        txt_placa.setText(obj_carro.getPlaca());

        return convertView;
    }
}
