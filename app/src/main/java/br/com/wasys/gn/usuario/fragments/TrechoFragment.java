package br.com.wasys.gn.usuario.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import br.com.wasys.gn.usuario.R;
import br.com.wasys.gn.usuario.models.Endereco;
import br.com.wasys.gn.usuario.models.Trecho;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import br.com.elp.library.utils.DateUtils;
import br.com.elp.library.utils.FieldUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class TrechoFragment extends Fragment {


    public TrechoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trecho, container, false);
    }

    public void popular(List<Trecho> trechos) {
        ViewGroup viewGroup = (ViewGroup) getView();
        viewGroup.removeAllViews();
        if (CollectionUtils.isNotEmpty(trechos)) {
            Date data = null;
            String horario = null;
            List<Item> items = new LinkedList<>();
            if (trechos.size() == 1) {
                Trecho trecho = trechos.get(0);
                data = trecho.data;
                horario = trecho.horario;
                Endereco inicio = trecho.inicio;
                Endereco termino = trecho.termino;
                items.add(new Item(R.drawable.icone_origem_azul, inicio.completo, null));
                items.add(new Item(R.drawable.icone_mapa_origem_azul, termino.completo, trecho.distance.text + " - " + trecho.duration.text));
            }
            else {
                for (int i = 0; i < trechos.size(); i++) {
                    Trecho trecho = trechos.get(i);
                    Endereco inicio = trecho.inicio;
                    if (i == 0) {
                        items.add(new Item(R.drawable.icone_origem_azul, inicio.completo, null));
                    }
                    else {
                        Trecho anterior = trechos.get(i - 1);
                        items.add(new Item(R.drawable.icone_mapa_origem_azul, inicio.completo, anterior.distance.text + " / " + anterior.duration.text));
                        if (i == trechos.size() - 1) {
                            Endereco termino = trecho.termino;
                            items.add(new Item(R.drawable.icone_mapa_origem_azul, termino.completo, trecho.distance.text + " / " + trecho.duration.text));
                        }
                    }
                    if (trecho.data != null && StringUtils.isNotBlank(trecho.horario)) {
                        if (data == null || data.compareTo(trecho.data) > 0) {
                            data = trecho.data;
                            horario = trecho.horario;
                        }
                    }
                }
            }
            if (data != null && StringUtils.isNotBlank(horario)) {
                int icon = R.drawable.icone_calendario_origem_azul;
                String title = DateUtils.format(data, DateUtils.DateType.DATE_BR.getPattern());
                String subtitle = horario;
                items.add(1, new Item(icon, title, subtitle));
            }
            Context context = getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            for (Item item : items) {
                View view = inflater.inflate(R.layout.item_trecho_fragment, null);
                TextView titleTextView = (TextView) view.findViewById(R.id.title_text_view);
                TextView subtitleTextView = (TextView) view.findViewById(R.id.subtitle_text_view);
                titleTextView.setCompoundDrawablesWithIntrinsicBounds(item.icon, 0, 0, 0);
                FieldUtils.setText(titleTextView, item.title);
                FieldUtils.setText(subtitleTextView, item.subtitle);
                viewGroup.addView(view);
            }
        }
    }

    public static class Item {
        public int icon;
        public String title;
        public String subtitle;
        public Item(int icon, String title, String subtitle) {
            this.icon = icon;
            this.title = title;
            this.subtitle = subtitle;
        }
    }
}
