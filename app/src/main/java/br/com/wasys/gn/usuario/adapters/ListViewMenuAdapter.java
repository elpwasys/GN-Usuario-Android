package br.com.wasys.gn.usuario.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import br.com.wasys.gn.usuario.R;
import br.com.wasys.gn.usuario.widgets.CustomTextView;

/**
 * Created by fernandamoncores on 3/28/16.
 */
public class ListViewMenuAdapter extends BaseAdapter {

    private Context context;

    public ListViewMenuAdapter(Context c)
    {
        this.context = c;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        convertView = layoutInflater.inflate(R.layout.item_menu, null);
        CustomTextView txt = (CustomTextView) convertView.findViewById(R.id.textView);
        ImageView img = (ImageView) convertView.findViewById(R.id.image);

        switch(position)
        {
            case 0:
                txt.setText("1/2 Diária");
                img.setBackgroundResource(R.drawable.menu_meia_diaria);
                break;
            case 1:
                txt.setText("Diária");
                img.setBackgroundResource(R.drawable.menu_diaria);
                break;
            case 2:
                txt.setText("Pernoite");
                img.setBackgroundResource(R.drawable.menu_pernoite);
                break;
            case 3:
                txt.setText("Traslado");
                img.setBackgroundResource(R.drawable.menu_translado);
                break;
        }
        return convertView;
    }
}
