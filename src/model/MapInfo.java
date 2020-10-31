/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author tienanh
 */
public class MapInfo {
	private String name;
	private String imagePreviewPath;
	private String type;
	private String des;

	public String getName() {
		return name;
	}

	public String getImagePreviewPath() {
		return imagePreviewPath;
	}

	public String getType() {
		return type;
	}

	public String getDes() {
		return des;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setImagePreviewPath(String imagePreviewPath) {
		this.imagePreviewPath = imagePreviewPath;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setDes(String des) {
		this.des = des;
	}
	
	public String getClassName (){
		return "map."+this.name;
	}
}
