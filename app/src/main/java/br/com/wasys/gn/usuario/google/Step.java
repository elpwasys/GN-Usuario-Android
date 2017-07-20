package br.com.wasys.gn.usuario.google;

import com.google.gson.annotations.SerializedName;

public class Step {
	
	@SerializedName("distance")
	public Distance distance;
	
	@SerializedName("duration")
	public Duration duration;
	
	@SerializedName("end_location")
	public LatLng endLocation;
	
	@SerializedName("start_location")
	public LatLng startLocation;
	
	@SerializedName("polyline")
	public Polyline polyline;
	
	@SerializedName("html_instructions")
	public String htmlInstructions;
	
	@SerializedName("travel_mode")
	public TravelMode travelMode;

	public enum TravelMode {
		DRIVING,
		WALKING,
		BICYCLING,
		TRANSIT;
	}
}
