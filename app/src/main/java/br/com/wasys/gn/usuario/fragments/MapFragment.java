package br.com.wasys.gn.usuario.fragments;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import br.com.wasys.gn.usuario.Permission;
import br.com.wasys.gn.usuario.R;
import br.com.wasys.gn.usuario.endpoint.GoogleMapsApiEndpoint;
import br.com.wasys.gn.usuario.google.Bounds;
import br.com.wasys.gn.usuario.google.DirectionResult;
import br.com.wasys.gn.usuario.google.Leg;
import br.com.wasys.gn.usuario.google.PolylineEncoding;
import br.com.wasys.gn.usuario.google.Route;
import br.com.wasys.gn.usuario.models.Endereco;
import br.com.wasys.gn.usuario.models.Trecho;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.elp.library.fragment.AppFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends AppFragment implements
        GoogleMap.OnMapLoadedCallback, ActivityCompat.OnRequestPermissionsResultCallback {

    private MapView mMapView;
    private GoogleMap mGoogleMap;
    private MapCallback mMapCallback;
    private ProgressBar mProgressBar;
    private LatLngBounds mLatLngBounds;
    private DirectionResult mDirectionResult;

    private static final int REQUEST_ACCESS_LOCATION = 100;

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        MapsInitializer.initialize(getActivity());
        mMapView = (MapView) view.findViewById(R.id.map_view);
        mMapView.onCreate(savedInstanceState);
        mGoogleMap = mMapView.getMap();
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mGoogleMap.setOnMapLoadedCallback(this);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
        setMyLocation();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onMapLoaded() {
        if (mLatLngBounds != null) {
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(mLatLngBounds, 75));
        }
    }

    public void snapshot(GoogleMap.SnapshotReadyCallback callback) {
        mGoogleMap.snapshot(callback);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_ACCESS_LOCATION) {
            boolean granted = grantedResults(grantResults);
            if (granted) {
                setMyLocation();
            }
        }
        else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void setMyLocation() {
        FragmentActivity activity = getActivity();
        if (!checkedSelfPermission(Permission.LOCATION)) {
            ActivityCompat.requestPermissions(activity, Permission.LOCATION, REQUEST_ACCESS_LOCATION);
            return;
        }
        mGoogleMap.setMyLocationEnabled(true);
    }

    public void sincronizar(List<Trecho> trechos) {
        if (CollectionUtils.isNotEmpty(trechos)) {
            /*
             * Cria os parametros para chamar a Direction API
             */
            String origin = null;
            String destination = null;
            StringBuilder waypoints = new StringBuilder();
            if (trechos.size() == 1) {
                Trecho trecho = trechos.get(0);
                Endereco inicio = trecho.inicio;
                Endereco termino = trecho.termino;
                origin = inicio.completo;
                destination = termino.completo;
            }
            else {
                for (int i = 0; i < trechos.size(); i++) {
                    Trecho trecho = trechos.get(i);
                    Endereco inicio = trecho.inicio;
                    if (i == 0) {
                        origin = inicio.completo;
                    }
                    else {
                        if (waypoints.length() > 0) {
                            waypoints.append("|");
                        }
                        waypoints.append(inicio.completo);
                        if (i == trechos.size() - 1) {
                            Endereco termino = trecho.termino;
                            destination = termino.completo;
                        }
                    }
                }
            }
            /*
             * Configura os parametros para chamar a Direction API
             */
            Map<String, String> parameters = new HashMap<>();
            parameters.put("origin", origin);
            parameters.put("destination", destination);
            parameters.put("mode", "driving");
            parameters.put("language", "pt-BR");
            if (StringUtils.isNotBlank(waypoints)) {
                parameters.put("waypoints", String.valueOf(waypoints));
            }
            /*
             * Chama a Direction API
             */
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(GoogleMapsApiEndpoint.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            GoogleMapsApiEndpoint endpoint = retrofit.create(GoogleMapsApiEndpoint.class);
            Call<DirectionResult> directions = endpoint.directions(parameters);
            /*
             * Escuta a chamda da Direction API
             */
            final Context context = getContext();
            directions.enqueue(new Callback<DirectionResult>() {
                @Override
                public void onResponse(Call<DirectionResult> call, Response<DirectionResult> response) {
                    String message = null;
                    if (response.isSuccessful()) {
                        DirectionResult directionResult = response.body();
                        loadDirectionResult(directionResult);
                    }
                    else {
                        message = getString(R.string.msg_falha_http_request_status, response.code());
                    }
                    if (StringUtils.isNotBlank(message)) {
                        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                    }
                    mMapView.setVisibility(View.VISIBLE);
                    mProgressBar.setVisibility(View.GONE);
                }
                @Override
                public void onFailure(Call<DirectionResult> call, Throwable t) {
                    mMapView.setVisibility(View.VISIBLE);
                    mProgressBar.setVisibility(View.GONE);
                    Toast.makeText(context, ExceptionUtils.getRootCauseMessage(t), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void loadDirectionResult(DirectionResult directionResult) {
        Context context = getContext();
        if (DirectionResult.Status.OK.equals(directionResult.status)) {
            mDirectionResult = directionResult;
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            Route route = mDirectionResult.routes[0];
            PolylineOptions polylineOptions = new PolylineOptions()
                    .width(5)
                    .color(Color.GRAY)
                    .geodesic(true);
            polylineOptions.addAll(PolylineEncoding.decode(route.polyline.points));
            mGoogleMap.addPolyline(polylineOptions);
            for (int i = 0; i < route.legs.length; i++) {
                Leg leg = route.legs[i];
                MarkerOptions startMarkerOptions = new MarkerOptions()
                        .title(leg.startAddress)
                        .position(new LatLng(leg.startLocation.lat, leg.startLocation.lng));
                int startDrawableRes = R.drawable.marker_gold_final;
                if (i == 0) {
                    startDrawableRes = R.drawable.marker_dark_blue;
                }
                startMarkerOptions.icon(BitmapDescriptorFactory.fromResource(startDrawableRes));
                mGoogleMap.addMarker(startMarkerOptions);
                if (i == (route.legs.length - 1)) {
                    MarkerOptions endMarkerOptions = new MarkerOptions()
                            .title(leg.endAddress)
                            .position(new LatLng(leg.endLocation.lat, leg.endLocation.lng))
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_gold_final));
                    mGoogleMap.addMarker(endMarkerOptions);
                }
            }
            Bounds bounds = route.bounds;
            builder.include(new LatLng(bounds.northeast.lat, bounds.northeast.lng));
            builder.include(new LatLng(bounds.southwest.lat, bounds.southwest.lng));
            mLatLngBounds = builder.build();
        }
        else {
            String message;
            if (StringUtils.isNotBlank(directionResult.errorMessage)) {
                message = directionResult.errorMessage;
            }
            else {
                message = getString(R.string.msg_falha_directions_api, directionResult.status.name());
            }
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }
        if (mMapCallback != null) {
            mMapCallback.onDirectionLoaded(directionResult);
        }
    }

    public void setMapCallback(MapCallback mapCallback) {
        mMapCallback = mapCallback;
    }

    public interface MapCallback {
        void onDirectionLoaded(DirectionResult directionResult);
    }
}
