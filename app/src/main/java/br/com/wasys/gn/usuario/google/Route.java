package br.com.wasys.gn.usuario.google;

import com.google.gson.annotations.SerializedName;

public class Route {

	@SerializedName("bounds")
	public Bounds bounds;
	
	@SerializedName("summary")
	public String summary;
	
	@SerializedName("copyrights")
	public String copyrights;
	
	@SerializedName("overview_polyline")
	public Polyline polyline;
	
	@SerializedName("warnings")
	public String[] warnings;
	
	@SerializedName("waypoint_order")
	public int[] waypointOrder;
	
	@SerializedName("legs")
	public Leg[] legs;
}
