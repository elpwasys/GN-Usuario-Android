package br.com.wasys.gn.usuario.google;

import com.google.gson.annotations.SerializedName;

public class Leg {

	@SerializedName("distance")
	public Distance distance;
	
	@SerializedName("duration")
	public Duration duration;
	
	@SerializedName("end_address")
	public String endAddress;
	
	@SerializedName("end_location")
	public LatLng endLocation;
	
	@SerializedName("start_address")
	public String startAddress;
	
	@SerializedName("start_location")
	public LatLng startLocation;
	
	@SerializedName("steps")
	public Step[] steps;
}
