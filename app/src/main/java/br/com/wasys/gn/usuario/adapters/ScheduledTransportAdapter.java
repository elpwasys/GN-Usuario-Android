package br.com.wasys.gn.usuario.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import br.com.wasys.gn.usuario.R;
import br.com.wasys.gn.usuario.endpoint.Endpoint;
import br.com.wasys.gn.usuario.endpoint.MotoristaEndpoint;
import br.com.wasys.gn.usuario.models.Motorista;
import br.com.wasys.gn.usuario.services.TransportesAgendadosData;

import java.io.IOException;
import java.util.ArrayList;

import br.com.elp.library.enumerator.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by fernandamoncores on 3/28/16.
 */
public class ScheduledTransportAdapter extends BaseAdapter {

    private Context context;
    public ArrayList<TransportesAgendadosData> results;

    public ScheduledTransportAdapter(Context c)
    {
        this.context = c;
    }
    public ScheduledTransportAdapter(Context c, ArrayList<TransportesAgendadosData> results)
    {
        this.context = c;
        this.results = results;
    }

    @Override
    public int getCount() {
        return results.size();
    }

    @Override
    public TransportesAgendadosData getItem(int position) {
        return results.get(position);
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(results.get(position).getId());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        }
        else {
            LayoutInflater inflater = LayoutInflater.from(context);
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_scheduled_transport_menu, null);
            holder.mapImageView = (ImageView) convertView.findViewById(R.id.imageView);
            holder.photoImageView = (ImageView) convertView.findViewById(R.id.user_photo);
            holder.progressBar = (ProgressBar) convertView.findViewById(R.id.progress_photo);
            holder.carroTextView = (TextView) convertView.findViewById(R.id.txt_carro);
            holder.codigoTextView = (TextView) convertView.findViewById(R.id.id_corrida);
            holder.horarioTextView = (TextView) convertView.findViewById(R.id.txt_horario_agendamento);
            holder.distanciaTextView = (TextView) convertView.findViewById(R.id.txt_distancia);
            holder.tipoCarroTextView = (TextView) convertView.findViewById(R.id.txt_tipo_carro);
            holder.tipoTransporteTextView = (TextView) convertView.findViewById(R.id.txt_tipo_transporte);
            convertView.setTag(holder);
        }
        TransportesAgendadosData obj = results.get(position);
        holder.codigoTextView.setText(obj.getCodigo());
        holder.horarioTextView.setText(obj.getData_verbose());
        holder.carroTextView.setText(obj.getNome_carro());
        holder.tipoTransporteTextView.setText(obj.getTipo());
        holder.distanciaTextView.setText(obj.getDistancia()+"Km");
        // Imagem
        String snapshot = obj.getSnapshot();
        if (!TextUtils.isEmpty(snapshot)) {
            byte[] bytes = Base64.decode(snapshot, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            holder.mapImageView.setImageBitmap(bitmap);
        }
        else {
            holder.mapImageView.setImageResource(R.drawable.ic_map);
        }
        // Imagem
        byte[] foto = obj.getFoto();
        if (foto != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(foto, 0, foto.length);
            holder.photoImageView.setImageBitmap(bitmap);
            holder.progressBar.setVisibility(View.GONE);
        }
        else {
            holder.progressBar.setVisibility(View.VISIBLE);
            holder.photoImageView.setImageResource(R.drawable.userpic);
            download(position);
        }
        return convertView;
    }

    private void download(int position) {
        final TransportesAgendadosData data = results.get(position);
        Motorista motorista = data.getMotorista();
        if (motorista != null) {
            MotoristaEndpoint endpoint = Endpoint.create(MotoristaEndpoint.class, MediaType.IMAGE_JPEG);
            Call<ResponseBody> call = endpoint.downloadFoto(motorista.id);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            byte[] bytes = response.body().bytes();
                            data.setFoto(bytes);
                            notifyDataSetChanged();
                        } catch (IOException e) {
                            //
                        }
                    }
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        }
    }

    static class ViewHolder {
        public ProgressBar progressBar;
        public ImageView mapImageView;
        public ImageView photoImageView;
        public TextView carroTextView;
        public TextView codigoTextView;
        public TextView horarioTextView;
        public TextView distanciaTextView;
        public TextView tipoCarroTextView;
        public TextView tipoTransporteTextView;
    }
}
