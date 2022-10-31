package com.findmybaby.www.dto;

public class FamilyDTO extends TotalDTO{
	private String f_parent;
	private String f_child;
	private int f_confirm;
	public String getF_parent() {
		return f_parent;
	}
	public void setF_parent(String f_parent) {
		this.f_parent = f_parent;
	}
	public String getF_child() {
		return f_child;
	}
	public void setF_child(String f_child) {
		this.f_child = f_child;
	}
	public int getF_confirm() {
		return f_confirm;
	}
	public void setF_confirm(int f_confirm) {
		this.f_confirm = f_confirm;
	}
	
}
