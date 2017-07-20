package br.com.wasys.gn.usuario.google;

import com.google.gson.annotations.SerializedName;

public class LatLng {

	@SerializedName("lat")
	public double lat;
	
	@SerializedName("lng")
	public double lng;

	public LatLng() {
		
	}
	
	public LatLng(double lat, double lng) {
		this.lat = lat;
		this.lng = lng;
	}
	
	@Override
	public String toString() {
		return String.format("%.8f,%.8f", lat, lng);
	}
}
