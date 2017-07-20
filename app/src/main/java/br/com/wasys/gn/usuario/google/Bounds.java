package br.com.wasys.gn.usuario.google;

import com.google.gson.annotations.SerializedName;

public class Bounds {

	@SerializedName("northeast")
	public LatLng northeast;
	
	@SerializedName("southwest")
	public LatLng southwest;
}
