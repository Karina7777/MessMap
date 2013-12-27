package org.smartblobs.gmaps;

public class Point {
	private double latitude;
	private double longitude;
	public Point(double lat, double lg){
		this.latitude = lat;
		this.longitude = lg;
	}
	
	public double getLatitude(){
		return this.latitude;
	}
	
	public double getLongitude(){
		return this.longitude;
	}
}
