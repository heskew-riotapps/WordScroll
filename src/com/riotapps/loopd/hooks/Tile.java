package com.riotapps.loopd.hooks;

import com.riotapps.wordbase.ui.Coordinate;
import com.riotapps.wordbase.ui.Location;
  
public class Tile {
	private int row;
	private String letter;
	private boolean isPlayed;
	private boolean isBonus;
	private int id;
	/*
	private Location location;
	
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	*/
	
	private Coordinate location;

	public Coordinate getLocation() {
		return location;
	}
	public void setLocation(Coordinate location) {
		this.location = location;
	}
	
	public int getRow() {
		return row;
	}
	public void setRow(int row) {
		this.row = row;
	}
	public String getLetter() {
		return letter;
	}
	public void setLetter(String letter) {
		this.letter = letter;
	}
	public boolean isPlayed() {
		return isPlayed;
	}
	public void setPlayed(boolean isPlayed) {
		this.isPlayed = isPlayed;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public boolean isBonus() {
		return isBonus;
	}
	public void setBonus(boolean isBonus) {
		this.isBonus = isBonus;
	}
	

}
