package com.findmybaby.www.dto;

public class LocationDTO extends TotalDTO{
	private String l_id;
	private String l_latitude;
	private String l_longitude;
	private String l_heading;
	
	public String getL_id() {
		return l_id;
	}
	public void setL_id(String l_id) {
		this.l_id = l_id;
	}
	public String getL_latitude() {
		return l_latitude;
	}
	public void setL_latitude(String l_latitude) {
		this.l_latitude = l_latitude;
	}
	public String getL_longitude() {
		return l_longitude;
	}
	public void setL_longitude(String l_longitude) {
		this.l_longitude = l_longitude;
	}
	public String getL_heading() {
		return l_heading;
	}
	public void setL_heading(String l_heading) {
		this.l_heading = l_heading;
	}
	
}
