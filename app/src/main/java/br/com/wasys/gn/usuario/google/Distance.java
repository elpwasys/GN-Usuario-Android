package br.com.wasys.gn.usuario.google;

import com.google.gson.annotations.SerializedName;

public class Distance {

	@SerializedName("value")
	public long value;
	
	@SerializedName("text")
	public String text;
}
