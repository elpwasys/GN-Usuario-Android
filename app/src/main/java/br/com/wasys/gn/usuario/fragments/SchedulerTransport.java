package br.com.wasys.gn.usuario.fragments;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import br.com.wasys.gn.usuario.R;
import br.com.wasys.gn.usuario.activities.AgendarParaTerceirosActivity;
import br.com.wasys.gn.usuario.activities.EscolherTransporteActivity;
import br.com.wasys.gn.usuario.activities.TransladoActivity;
import br.com.wasys.gn.usuario.adapters.ListViewMenuAdapter;
import br.com.wasys.gn.usuario.helpers.Helper;

/**
 * Created by fernandamoncores on 3/28/16.
 */
public class SchedulerTransport extends Fragment implements AdapterView.OnItemClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View android = inflater.inflate(R.layout.fragment_scheduler_transport, container, false);
        ListView list = (ListView) android.findViewById(R.id.list_view_menu);
        list.setAdapter(new ListViewMenuAdapter(getContext()));
        list.setOnItemClickListener(this);

        return android;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i;
        if(Boolean.parseBoolean(Helper.current_user(getActivity()).getPermiteAgendarTerceiros()))
        {
            i = new Intent(getContext(), AgendarParaTerceirosActivity.class);
        }
        else
        {
            if(position < 3)
                i = new Intent(getContext(), EscolherTransporteActivity.class);
            else
                i = new Intent(getContext(), TransladoActivity.class);
        }

        Bundle b = new Bundle();
        b.putInt("opcao",position);
        i.putExtras(b);
        startActivity(i);
    }
}
