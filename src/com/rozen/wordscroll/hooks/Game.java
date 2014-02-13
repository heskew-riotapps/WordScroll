package com.rozen.wordscroll.hooks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.google.gson.annotations.SerializedName;
import com.riotapps.wordbase.hooks.PlayedWord;

public class Game {
	private static final String TAG = Game.class.getSimpleName();
	public Game(){}
	
	@SerializedName("id")
	private String id = "";
	
	@SerializedName("played_words")
	private List<PlayedWord> playedWords = new ArrayList<PlayedWord>();
 
	@SerializedName("played_letters")
	private List<String> playedTiles = new ArrayList<String>();
	
	@SerializedName("r1_tiles")
	private List<Tile> row1Tiles = new ArrayList<Tile>();

	@SerializedName("r2_tiles")
	private List<Tile> row2Tiles = new ArrayList<Tile>();
	
	@SerializedName("r_v")
	private String randomVowel;

	@SerializedName("r_c")
	private List<String> randomConsonants;
	
	@SerializedName("hop")
	private List<String> hopper;
	
	@SerializedName("cr_d")
	private Date createDate = new Date(0);  
 
	
	@SerializedName("co_d")
	private Date completionDate = new Date(0); 

	@SerializedName("st")
	private int status = 0;   
	
	@SerializedName("t")
	private int turn = 0;  	


	public String getRandomVowel() {
		return randomVowel;
	}

	public void setRandomVowel(String randomVowel) {
		this.randomVowel = randomVowel;
	}

	public List<String> getRandomConsonants() {
		return randomConsonants;
	}

	public void setRandomConsonants(List<String> randomConsonants) {
		this.randomConsonants = randomConsonants;
	}
	
	

	public List<Tile> getRow1Tiles() {
		return row1Tiles;
	}

	public void setRow1Tiles(List<Tile> row1Tiles) {
		this.row1Tiles = row1Tiles;
	}

	public List<Tile> getRow2Tiles() {
		return row2Tiles;
	}

	public void setRow2Tiles(List<Tile> row2Tiles) {
		this.row2Tiles = row2Tiles;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<PlayedWord> getPlayedWords() {
		return playedWords;
	}
	

	public void setPlayedWords(List<PlayedWord> playedWords) {
		this.playedWords = playedWords;
	}


	
	public List<String> getHopper() {
		return hopper;
	}

	public void setHopper(List<String> hopper) {
		this.hopper = hopper;
	}

	public void shuffleHopper(){
		Collections.shuffle(this.hopper);
	}
	
	
	
 
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getCompletionDate() {
		return completionDate;
	}

	public void setCompletionDate(Date completionDate) {
		this.completionDate = completionDate;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
 
 
	
	public boolean isCompleted(){
		return this.status == 3 || this.getStatus() == 4;
	}
	
	public boolean isActive(){
		return this.status == 1;
	}
	

}
