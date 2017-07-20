package br.com.wasys.gn.usuario.google;

import com.google.gson.annotations.SerializedName;

public class DirectionResult {

	@SerializedName("status")
	public Status status;
	
	@SerializedName("error_message")
	public String errorMessage;
	
	@SerializedName("routes")
	public Route[] routes;
	
	
	@SerializedName("geocoded_waypoints")
	public GeocodedWaypoint[] geocodedWaypoints;
	
	public enum Status {
		OK,
		NOT_FOUND,
		ZERO_RESULTS,
		MAX_WAYPOINTS_EXCEEDED,
		INVALID_REQUEST,
		OVER_QUERY_LIMIT,
		REQUEST_DENIED,
		UNKNOWN_ERROR;
	}
}
