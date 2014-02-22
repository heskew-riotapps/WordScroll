package com.rozen.wordscroll.hooks;

public class PlayedTile {
	private String letter = "";
	private boolean isDefault;
	private boolean isBonus;
	private int position;
	private int tileId;
	
	public static PlayedTile getDefaultInstance(){
		PlayedTile tile = new PlayedTile();
		tile.setDefault(false);
		tile.setLetter("");
		tile.setPosition(0);
		tile.setTileId(0);
		
		return tile;
	}
	
	
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	public String getLetter() {
		return letter;
	}
	public void setLetter(String letter) {
		this.letter = letter;
	}
	public boolean isDefault() {
		return isDefault;
	}
	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}
	public int getTileId() {
		return tileId;
	}
	public void setTileId(int tileId) {
		this.tileId = tileId;
	}


	public boolean isBonus() {
		return isBonus;
	}


	public void setBonus(boolean isBonus) {
		this.isBonus = isBonus;
	}
	
	

}
