package org.smartblobs.gmaps;

public class Mark {
	private String title;
	private Point point;
	public Mark(Point p){
		this.setPoint(p);
	}
	public Point getPoint() {
		return point;
	}
	public void setPoint(Point point) {
		this.point = point;
	}
	
}
