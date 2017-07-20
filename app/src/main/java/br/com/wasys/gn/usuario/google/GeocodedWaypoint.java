package br.com.wasys.gn.usuario.google;

import com.google.gson.annotations.SerializedName;

public class GeocodedWaypoint {

	@SerializedName("geocoder_status")
	public Status status;
	
	@SerializedName("place_id")
	public String placeId;
	
	@SerializedName("partial_match")
	public boolean partialMatch;
	
	@SerializedName("types")
	public String[] types;
	
	public enum Status {
		OK,
		ZERO_RESULTS
	}
}
