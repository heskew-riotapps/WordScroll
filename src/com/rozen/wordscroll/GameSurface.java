package com.rozen.wordscroll;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
 
import android.widget.PopupMenu;
import android.widget.TextView;

import com.riotapps.wordbase.R;
import com.rozen.wordscroll.hooks.Fragment;
import com.rozen.wordscroll.hooks.FragmentService;
import com.rozen.wordscroll.hooks.GameService;
import com.rozen.wordscroll.hooks.PlayedTile;
import com.rozen.wordscroll.hooks.Tile;
import com.riotapps.wordbase.hooks.Player;
import com.riotapps.wordbase.interfaces.ICloseDialog;
import com.rozen.wordscroll.hooks.Game;
import com.rozen.wordscroll.ui.GameSurfaceView;
import com.riotapps.wordbase.services.WordLoaderService;
import com.riotapps.wordbase.ui.MenuUtils;
import com.riotapps.wordbase.utils.ApplicationContext;
import com.riotapps.wordbase.utils.Constants;
import com.riotapps.wordbase.utils.DesignByContractException;
import com.riotapps.wordbase.utils.ImageHelper;
import com.riotapps.wordbase.utils.Logger;
 
 

public class GameSurface  extends FragmentActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener, ICloseDialog{
	private static final String TAG = GameSurface.class.getSimpleName();
	private GameSurfaceView gameSurfaceView;
	
	private Game game;
	private Player player;
	private List<Fragment> fragments = new ArrayList<Fragment>();
	private LoadFragmentsTask loadFragmentsTask;
	private TextView ivRow1PlayedLetter1;
	private TextView ivRow1PlayedLetter2;
	private TextView ivRow1PlayedLetter3;
	private TextView ivRow1PlayedLetter4;
	private TextView ivRow1PlayedLetter5;
	private TextView ivRow1PlayedLetter6;
	private TextView ivRow1PlayedLetter7;
	private TextView ivRow1PlayedLetter8;
	private TextView ivRow1PlayedLetter9;
	private TextView ivRow1PlayedLetter10;
	private TextView ivRow2PlayedLetter1;
	private TextView ivRow2PlayedLetter2;
	private TextView ivRow2PlayedLetter3;
	private TextView ivRow2PlayedLetter4;
	private TextView ivRow2PlayedLetter5;
	private TextView ivRow2PlayedLetter6;
	private TextView ivRow2PlayedLetter7;
	private TextView ivRow2PlayedLetter8;
	private TextView ivRow2PlayedLetter9;
	private TextView ivRow2PlayedLetter10;
	private TextView ivRow3PlayedLetter1;
	private TextView ivRow3PlayedLetter2;
	private TextView ivRow3PlayedLetter3;
	private TextView ivRow3PlayedLetter4;
	private TextView ivRow3PlayedLetter5;
	private TextView ivRow3PlayedLetter6;
	private TextView ivRow3PlayedLetter7;
	private TextView ivRow3PlayedLetter8;
	private TextView ivRow3PlayedLetter9;
	private TextView ivRow3PlayedLetter10;
	
	private List<PlayedTile> row1Tiles = new ArrayList<PlayedTile>();
	private List<PlayedTile> row2Tiles = new ArrayList<PlayedTile>();
	private List<PlayedTile> row3Tiles = new ArrayList<PlayedTile>();
	
	private TextView tvCountdown;
	private TextView tvScore;
	private TextView tvTopScore;
	
	private int playedLetterTileSize;
	private float letterTileSize;
	
	private static Bitmap bgTray = null;
	
	public Player getPlayer() {
		//return player;
		if (this.player == null){
			this.player = ((ApplicationContext)this.getApplicationContext()).getPlayer(); //PlayerService.getPlayerFromLocal();
		}
		return this.player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}
	
	public Game getGame(){
		return this.game;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gamesurface);
		
		Logger.d(TAG, "onCreate called");
		
	//	ImageView ivLetter = (ImageView)this.findViewById(R.id.ivLetter);
//		ivLetter.setOnClickListener(this);
		
		 
	 //	this.setupPreloadTask();
		
		this.kickoff();
	}
	@Override
	public void dialogClose(int resultCode) {
		// TODO Auto-generated method stub
		
	}

	private void kickoff(){

		this.setGame(); 
		
		//temp
		String s = "";
		//for(int i = 0; i < 10; i ++){
		//	this.game.getRow1Tiles().remove(0);
		//}
	 	for (Tile t : this.game.getRow1Tiles()){ 
	 		 s += t.getLetter() + " ";// + " id=" + t.getId());
	 	}
	 	 Logger.d("TAG", "hopper=" +  s);// + " id=" + t.getId());
	 	 
	 	for (Tile t : this.game.getRow1Tiles()){
	 		Logger.d("TAG", "hopper id=" + t.getId() + " letter=" + t.getLetter() + " played=" + t.isPlayed());
	 	}
	    this.gameSurfaceView = (GameSurfaceView)findViewById(R.id.gameSurface);
	    this.gameSurfaceView.construct(this);
	    
	    
		this.loadViews();
		this.setupFonts();
		this.setViewLayouts();
		this.preloadPlayedTiles();		
		
		this.loadFragmentsTask = new LoadFragmentsTask();
		this.loadFragmentsTask.execute(); 
	}
	
 
	private class LoadFragmentsTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... arg0) {
			// wait until the word loader service is finished because the first time 
			//through the database will not have been copied over yet
			//x is safety valve
			
			//this method will just whistle a tune until the database is copied over
			//it should happen very fast
			int x = 0;
			
			while( x < 5000 ){
				
				if ( !WordLoaderService.isLoading){
					Logger.d(TAG, "LoadFragmentsTask WordLoaderService is FINISHED loading");
					break;
				}
				else{
					Logger.d(TAG, "LoadFragmentsTask WordLoaderService is loading still");
				}
				
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			 
			
			return null;
		}
		
	      @Override
	      protected void onPostExecute(Void arg0) {  
	    	  GameSurface.this.loadFragmentsAndView();
	    	 
	      }
	 }
	
	private class fragmentLetter{
		private String letter;
		private int position;
		
		public fragmentLetter(String letter, int position){
			this.letter = letter;
			this.position = position;
		}
		
		public String getLetter() {
			return letter;
		}
		public void setLetter(String letter) {
			this.letter = letter;
		}
		public int getPosition() {
			return position;
		}
		public void setPosition(int position) {
			this.position = position;
		}
		
	}
	
	private void initializePlayedLists(){
 
		String[] fragment1_arr = this.fragments.get(0).getLetters().split("");
	 	List<fragmentLetter> fragmentLetters_1 =  new ArrayList<fragmentLetter>();
		
	 	//subtracting 1 from 1 because java adds an extra item when splitting, very annoying
		for (int i = 1; i < fragment1_arr.length; i++){
			fragmentLetters_1.add(new fragmentLetter(fragment1_arr[i], this.fragments.get(0).getStartingPosition() + i - 1));
		}
		
		String[] fragment2_arr = this.fragments.get(1).getLetters().split("");
	 	List<fragmentLetter> fragmentLetters_2 =  new ArrayList<fragmentLetter>();
		
		for (int i = 1; i < fragment2_arr.length; i++){
			fragmentLetters_2.add(new fragmentLetter(fragment2_arr[i], this.fragments.get(1).getStartingPosition() + i - 1));
		}
		
		String[] fragment3_arr = this.fragments.get(2).getLetters().split("");
	 	List<fragmentLetter> fragmentLetters_3 =  new ArrayList<fragmentLetter>();
		
		for (int i = 1; i < fragment3_arr.length; i++){
			fragmentLetters_3.add(new fragmentLetter(fragment3_arr[i], this.fragments.get(2).getStartingPosition() + i - 1));
		}
		
		for (int i = 1; i <= 10; i++){
			PlayedTile playedTile = PlayedTile.getDefaultInstance();
			
			if (fragments.get(0).getStartingPosition() <= i){
			
			}
			
			
			this.row1Tiles.add(playedTile);
		}
		
		String fragment2 = this.fragments.get(1).getLetters();
		
		String fragment3 = this.fragments.get(2).getLetters();
	}
	
	private void loadFragmentsAndView(){
		this.loadFragments(30);
		this.initializePlayedLists();
		this.loadPlayedTileViews();
	}
	
	private void loadPlayedTileViews(){
	
		String fragment1 = this.fragments.get(0).getLetters();
		String[] fragment1_arr = fragment1.split("");
		List<String> fragment1List = new LinkedList<String>(Arrays.asList(fragment1_arr));// Array;//s.asList(fragment1_arr);
		
		//because java adds empty string as the first element ( its a bit annoying )
		//so lets get rid of it
		fragment1List.remove(0); 
		
		Logger.d(TAG, "loadFragmentsAndView letters=" + this.fragments.get(0).getLetters() );
		
		//use 1 to get the first  element in this case 
		for (int i = 0; i < fragment1List.size(); i++) {
			if (i + this.fragments.get(0).getStartingPosition() == 1) {
				this.ivRow1PlayedLetter1.setText(fragment1List.get(i));
			}
			else if (i + this.fragments.get(0).getStartingPosition() == 2) {
				this.ivRow1PlayedLetter2.setText(fragment1List.get(i));
			}
			else if (i + this.fragments.get(0).getStartingPosition() == 3) {
				this.ivRow1PlayedLetter3.setText(fragment1List.get(i));
			}
			else if (i + this.fragments.get(0).getStartingPosition() == 4) {
				this.ivRow1PlayedLetter4.setText(fragment1List.get(i));
			}
			else if (i + this.fragments.get(0).getStartingPosition() == 5) {
				this.ivRow1PlayedLetter5.setText(fragment1List.get(i));
			}
			else if (i + this.fragments.get(0).getStartingPosition() == 6) {
				this.ivRow1PlayedLetter6.setText(fragment1List.get(i));
			}
			else if (i + this.fragments.get(0).getStartingPosition() == 7) {
				this.ivRow1PlayedLetter7.setText(fragment1List.get(i));
			}
			else if (i + this.fragments.get(0).getStartingPosition() == 8) {
				this.ivRow1PlayedLetter8.setText(fragment1List.get(i));
			}
			else if (i + this.fragments.get(0).getStartingPosition() == 9) {
				this.ivRow1PlayedLetter9.setText(fragment1List.get(i));
			}
			else if (i + this.fragments.get(0).getStartingPosition() == 10) {
				this.ivRow1PlayedLetter10.setText(fragment1List.get(i));
			} 
		}
		
		String fragment2 = this.fragments.get(1).getLetters();
		String[] fragment2_arr = fragment2.split("");
		List<String> fragment2List = new LinkedList<String>(Arrays.asList(fragment2_arr));// Array;//s.asList(fragment1_arr);
		
		//because java adds empty string as the first element ( its a bit annoying )
		//so lets get rid of it
		fragment2List.remove(0); 
		
		Logger.d(TAG, "loadFragmentsAndView letters=" + this.fragments.get(1).getLetters() );
		
		//use 1 to get the first  element in this case 
		for (int i = 0; i < fragment2List.size(); i++) {
			if (i + this.fragments.get(1).getStartingPosition() == 1) {
				this.ivRow2PlayedLetter1.setText(fragment2List.get(i));
			}
			else if (i + this.fragments.get(1).getStartingPosition() == 2) {
				this.ivRow2PlayedLetter2.setText(fragment2List.get(i));
			}
			else if (i + this.fragments.get(1).getStartingPosition() == 3) {
				this.ivRow2PlayedLetter3.setText(fragment2List.get(i));
			}
			else if (i + this.fragments.get(1).getStartingPosition() == 4) {
				this.ivRow2PlayedLetter4.setText(fragment2List.get(i));
			}
			else if (i + this.fragments.get(1).getStartingPosition() == 5) {
				this.ivRow2PlayedLetter5.setText(fragment2List.get(i));
			}
			else if (i + this.fragments.get(1).getStartingPosition() == 6) {
				this.ivRow2PlayedLetter6.setText(fragment2List.get(i));
			}
			else if (i + this.fragments.get(1).getStartingPosition() == 7) {
				this.ivRow2PlayedLetter7.setText(fragment2List.get(i));
			}
			else if (i + this.fragments.get(1).getStartingPosition() == 8) {
				this.ivRow2PlayedLetter8.setText(fragment2List.get(i));
			}
			else if (i + this.fragments.get(1).getStartingPosition() == 9) {
				this.ivRow2PlayedLetter9.setText(fragment2List.get(i));
			}
			else if (i + this.fragments.get(1).getStartingPosition() == 10) {
				this.ivRow2PlayedLetter10.setText(fragment2List.get(i));
			} 
		}
		
		
		String fragment3 = this.fragments.get(2).getLetters();
		String[] fragment3_arr = fragment3.split("");
		List<String> fragment3List = new LinkedList<String>(Arrays.asList(fragment3_arr));// Array;//s.asList(fragment1_arr);
		
		//because java adds empty string as the first element ( its a bit annoying )
		//so lets get rid of it
		fragment3List.remove(0); 
		
		Logger.d(TAG, "loadFragmentsAndView letters=" + this.fragments.get(2).getLetters() );
		
		//use 1 to get the first  element in this case 
		for (int i = 0; i < fragment3List.size(); i++) {
			if (i + this.fragments.get(2).getStartingPosition() == 1) {
				this.ivRow3PlayedLetter1.setText(fragment3List.get(i));
			}
			else if (i + this.fragments.get(2).getStartingPosition() == 2) {
				this.ivRow3PlayedLetter2.setText(fragment3List.get(i));
			}
			else if (i + this.fragments.get(2).getStartingPosition() == 3) {
				this.ivRow3PlayedLetter3.setText(fragment3List.get(i));
			}
			else if (i + this.fragments.get(2).getStartingPosition() == 4) {
				this.ivRow3PlayedLetter4.setText(fragment3List.get(i));
			}
			else if (i + this.fragments.get(2).getStartingPosition() == 5) {
				this.ivRow3PlayedLetter5.setText(fragment3List.get(i));
			}
			else if (i + this.fragments.get(2).getStartingPosition() == 6) {
				this.ivRow3PlayedLetter6.setText(fragment3List.get(i));
			}
			else if (i + this.fragments.get(2).getStartingPosition() == 7) {
				this.ivRow3PlayedLetter7.setText(fragment3List.get(i));
			}
			else if (i + this.fragments.get(2).getStartingPosition() == 8) {
				this.ivRow3PlayedLetter8.setText(fragment3List.get(i));
			}
			else if (i + this.fragments.get(2).getStartingPosition() == 9) {
				this.ivRow3PlayedLetter9.setText(fragment3List.get(i));
			}
			else if (i + this.fragments.get(2).getStartingPosition() == 10) {
				this.ivRow3PlayedLetter10.setText(fragment3List.get(i));
			} 
		}
		
	}
	
	private void loadFragments(int num){
		Logger.d(TAG, "loadFragments WordLoaderService.isLoading=" + WordLoaderService.isLoading);
		
		
		FragmentService fragmentService = new FragmentService(this);
		this.fragments = fragmentService.getRandomFragments(30);
		fragmentService.finish();
		fragmentService = null;
		
		Logger.d(TAG, "loadFragments num fragments=" + this.fragments.size());
	}
	
	
	@Override
	public void dialogClose(int resultCode, String returnValue) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onMenuItemClick(MenuItem arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onClick(View v) {
		Logger.d(TAG, "onClick called");
		
		// TODO Auto-generated method stub
	//	ImageView ivLetter = (ImageView)this.findViewById(R.id.ivLetter);
		
	
	}
	
	public void callback(){
		
	}
	
	private void loadViews(){
 
		this.ivRow1PlayedLetter1 = (TextView) findViewById(com.rozen.wordscroll.R.id.ivRow1PlayedLetter1);
		this.ivRow1PlayedLetter2 = (TextView) findViewById(com.rozen.wordscroll.R.id.ivRow1PlayedLetter2);
		this.ivRow1PlayedLetter3 = (TextView) findViewById(com.rozen.wordscroll.R.id.ivRow1PlayedLetter3);
		this.ivRow1PlayedLetter4 = (TextView) findViewById(com.rozen.wordscroll.R.id.ivRow1PlayedLetter4);
		this.ivRow1PlayedLetter5 = (TextView) findViewById(com.rozen.wordscroll.R.id.ivRow1PlayedLetter5);
		this.ivRow1PlayedLetter6 = (TextView) findViewById(com.rozen.wordscroll.R.id.ivRow1PlayedLetter6);
		this.ivRow1PlayedLetter7 = (TextView) findViewById(com.rozen.wordscroll.R.id.ivRow1PlayedLetter7);
		this.ivRow1PlayedLetter8 = (TextView) findViewById(com.rozen.wordscroll.R.id.ivRow1PlayedLetter8);
		this.ivRow1PlayedLetter9 = (TextView) findViewById(com.rozen.wordscroll.R.id.ivRow1PlayedLetter9);
		this.ivRow1PlayedLetter10 = (TextView) findViewById(com.rozen.wordscroll.R.id.ivRow1PlayedLetter10);
		 
		this.ivRow2PlayedLetter1 = (TextView) findViewById(com.rozen.wordscroll.R.id.ivRow2PlayedLetter1);
		this.ivRow2PlayedLetter2 = (TextView) findViewById(com.rozen.wordscroll.R.id.ivRow2PlayedLetter2);
		this.ivRow2PlayedLetter3 = (TextView) findViewById(com.rozen.wordscroll.R.id.ivRow2PlayedLetter3);
		this.ivRow2PlayedLetter4 = (TextView) findViewById(com.rozen.wordscroll.R.id.ivRow2PlayedLetter4);
		this.ivRow2PlayedLetter5 = (TextView) findViewById(com.rozen.wordscroll.R.id.ivRow2PlayedLetter5);
		this.ivRow2PlayedLetter6 = (TextView) findViewById(com.rozen.wordscroll.R.id.ivRow2PlayedLetter6);
		this.ivRow2PlayedLetter7 = (TextView) findViewById(com.rozen.wordscroll.R.id.ivRow2PlayedLetter7);
		this.ivRow2PlayedLetter8 = (TextView) findViewById(com.rozen.wordscroll.R.id.ivRow2PlayedLetter8);
		this.ivRow2PlayedLetter9 = (TextView) findViewById(com.rozen.wordscroll.R.id.ivRow2PlayedLetter9);
		this.ivRow2PlayedLetter10 = (TextView) findViewById(com.rozen.wordscroll.R.id.ivRow2PlayedLetter10);
		
		this.ivRow3PlayedLetter1 = (TextView) findViewById(com.rozen.wordscroll.R.id.ivRow3PlayedLetter1);
		this.ivRow3PlayedLetter2 = (TextView) findViewById(com.rozen.wordscroll.R.id.ivRow3PlayedLetter2);
		this.ivRow3PlayedLetter3 = (TextView) findViewById(com.rozen.wordscroll.R.id.ivRow3PlayedLetter3);
		this.ivRow3PlayedLetter4 = (TextView) findViewById(com.rozen.wordscroll.R.id.ivRow3PlayedLetter4);
		this.ivRow3PlayedLetter5 = (TextView) findViewById(com.rozen.wordscroll.R.id.ivRow3PlayedLetter5);
		this.ivRow3PlayedLetter6 = (TextView) findViewById(com.rozen.wordscroll.R.id.ivRow3PlayedLetter6);
		this.ivRow3PlayedLetter7 = (TextView) findViewById(com.rozen.wordscroll.R.id.ivRow3PlayedLetter7);
		this.ivRow3PlayedLetter8 = (TextView) findViewById(com.rozen.wordscroll.R.id.ivRow3PlayedLetter8);
		this.ivRow3PlayedLetter9 = (TextView) findViewById(com.rozen.wordscroll.R.id.ivRow3PlayedLetter9);
		this.ivRow3PlayedLetter10 = (TextView) findViewById(com.rozen.wordscroll.R.id.ivRow3PlayedLetter10);

		this.tvCountdown = (TextView) findViewById(com.rozen.wordscroll.R.id.tvCountdown);
		this.tvScore = (TextView) findViewById(com.rozen.wordscroll.R.id.tvScore); 
		this.tvTopScore = (TextView) findViewById(com.rozen.wordscroll.R.id.tvTopScore); 
		
 
		this.tvScore.setText("0");
		this.tvTopScore.setText("0");
		
	}

	private void setViewLayouts(){
		
		Display display = getWindowManager().getDefaultDisplay();
	    Point size = new Point();
	 	display.getSize(size);
	 	display.getSize(size);
		int fullWidth = size.x;
		
		this.playedLetterTileSize = Math.round(fullWidth / 10.50f);
		this.letterTileSize =  this.playedLetterTileSize * .28f ;	
		
		Logger.d(TAG, "setViewLayouts letterTileSize=" + this.letterTileSize + " playedLetterTileSize=" + this.playedLetterTileSize);
	 	//int maxTrayTileSize = this.getResources().getInteger(R.integer.maxTrayTileSize);
		//if (this.letterTileSize > maxTrayTileSize){letterTileSize = maxTrayTileSize;} 
		
		ViewGroup.LayoutParams params_r1_p1 = ivRow1PlayedLetter1.getLayoutParams();
		params_r1_p1.width = this.playedLetterTileSize;
		params_r1_p1.height = this.playedLetterTileSize;
		ivRow1PlayedLetter1.setLayoutParams(params_r1_p1);
		ivRow1PlayedLetter1.setTextSize(this.letterTileSize);
		
		ViewGroup.LayoutParams params_r1_p2 = ivRow1PlayedLetter2.getLayoutParams();
		params_r1_p2.width = this.playedLetterTileSize;
		params_r1_p2.height = this.playedLetterTileSize;
		ivRow1PlayedLetter2.setLayoutParams(params_r1_p2);
		ivRow1PlayedLetter2.setTextSize(this.letterTileSize);
		
		ViewGroup.LayoutParams params_r1_p3 = ivRow1PlayedLetter3.getLayoutParams();
		params_r1_p3.width = this.playedLetterTileSize;
		params_r1_p3.height = this.playedLetterTileSize;
		ivRow1PlayedLetter3.setLayoutParams(params_r1_p3);
		ivRow1PlayedLetter3.setTextSize(this.letterTileSize);

		ViewGroup.LayoutParams params_r1_p4 = ivRow1PlayedLetter4.getLayoutParams();
		params_r1_p4.width = this.playedLetterTileSize;
		params_r1_p4.height = this.playedLetterTileSize;
		ivRow1PlayedLetter4.setLayoutParams(params_r1_p4);
		ivRow1PlayedLetter4.setTextSize(this.letterTileSize);
		
		ViewGroup.LayoutParams params_r1_p5 = ivRow1PlayedLetter5.getLayoutParams();
		params_r1_p5.width = this.playedLetterTileSize;
		params_r1_p5.height = this.playedLetterTileSize;
		ivRow1PlayedLetter5.setLayoutParams(params_r1_p5);
		ivRow1PlayedLetter5.setTextSize(this.letterTileSize);

		ViewGroup.LayoutParams params_r1_p6 = ivRow1PlayedLetter6.getLayoutParams();
		params_r1_p6.width = this.playedLetterTileSize;
		params_r1_p6.height = this.playedLetterTileSize;
		ivRow1PlayedLetter6.setLayoutParams(params_r1_p6);
		ivRow1PlayedLetter6.setTextSize(this.letterTileSize);

		ViewGroup.LayoutParams params_r1_p7 = ivRow1PlayedLetter7.getLayoutParams();
		params_r1_p7.width = this.playedLetterTileSize;
		params_r1_p7.height = this.playedLetterTileSize;
		ivRow1PlayedLetter7.setLayoutParams(params_r1_p7);
		ivRow1PlayedLetter7.setTextSize(this.letterTileSize);

		ViewGroup.LayoutParams params_r1_p8 = ivRow1PlayedLetter8.getLayoutParams();
		params_r1_p8.width = this.playedLetterTileSize;
		params_r1_p8.height = this.playedLetterTileSize;
		ivRow1PlayedLetter8.setLayoutParams(params_r1_p8);
		ivRow1PlayedLetter8.setTextSize(this.letterTileSize);

		ViewGroup.LayoutParams params_r1_p9 = ivRow1PlayedLetter9.getLayoutParams();
		params_r1_p9.width = this.playedLetterTileSize;
		params_r1_p9.height = this.playedLetterTileSize;
		ivRow1PlayedLetter9.setLayoutParams(params_r1_p9);
		ivRow1PlayedLetter9.setTextSize(this.letterTileSize);

		ViewGroup.LayoutParams params_r1_p10 = ivRow1PlayedLetter10.getLayoutParams();
		params_r1_p10.width = this.playedLetterTileSize;
		params_r1_p10.height = this.playedLetterTileSize;
		ivRow1PlayedLetter10.setLayoutParams(params_r1_p10);
		ivRow1PlayedLetter10.setTextSize(this.letterTileSize);
	
		//row 2
		ViewGroup.LayoutParams params_r2_p1 = ivRow2PlayedLetter1.getLayoutParams();
		params_r2_p1.width = this.playedLetterTileSize;
		params_r2_p1.height = this.playedLetterTileSize;
		ivRow2PlayedLetter1.setLayoutParams(params_r2_p1);
		ivRow2PlayedLetter1.setTextSize(this.letterTileSize);
		
		ViewGroup.LayoutParams params_r2_p2 = ivRow2PlayedLetter2.getLayoutParams();
		params_r2_p2.width = this.playedLetterTileSize;
		params_r2_p2.height = this.playedLetterTileSize;
		ivRow2PlayedLetter2.setLayoutParams(params_r2_p2);
		ivRow2PlayedLetter2.setTextSize(this.letterTileSize);
		
		ViewGroup.LayoutParams params_r2_p3 = ivRow2PlayedLetter3.getLayoutParams();
		params_r2_p3.width = this.playedLetterTileSize;
		params_r2_p3.height = this.playedLetterTileSize;
		ivRow2PlayedLetter3.setLayoutParams(params_r2_p3);
		ivRow2PlayedLetter3.setTextSize(this.letterTileSize);

		ViewGroup.LayoutParams params_r2_p4 = ivRow2PlayedLetter4.getLayoutParams();
		params_r2_p4.width = this.playedLetterTileSize;
		params_r2_p4.height = this.playedLetterTileSize;
		ivRow2PlayedLetter4.setLayoutParams(params_r2_p4);
		ivRow2PlayedLetter4.setTextSize(this.letterTileSize);
		
		ViewGroup.LayoutParams params_r2_p5 = ivRow2PlayedLetter5.getLayoutParams();
		params_r2_p5.width = this.playedLetterTileSize;
		params_r2_p5.height = this.playedLetterTileSize;
		ivRow2PlayedLetter5.setLayoutParams(params_r2_p5);
		ivRow2PlayedLetter5.setTextSize(this.letterTileSize);

		ViewGroup.LayoutParams params_r2_p6 = ivRow2PlayedLetter6.getLayoutParams();
		params_r2_p6.width = this.playedLetterTileSize;
		params_r2_p6.height = this.playedLetterTileSize;
		ivRow2PlayedLetter6.setLayoutParams(params_r2_p6);
		ivRow2PlayedLetter6.setTextSize(this.letterTileSize);

		ViewGroup.LayoutParams params_r2_p7 = ivRow2PlayedLetter7.getLayoutParams();
		params_r2_p7.width = this.playedLetterTileSize;
		params_r2_p7.height = this.playedLetterTileSize;
		ivRow2PlayedLetter7.setLayoutParams(params_r2_p7);
		ivRow2PlayedLetter7.setTextSize(this.letterTileSize);

		ViewGroup.LayoutParams params_r2_p8 = ivRow2PlayedLetter8.getLayoutParams();
		params_r2_p8.width = this.playedLetterTileSize;
		params_r2_p8.height = this.playedLetterTileSize;
		ivRow2PlayedLetter8.setLayoutParams(params_r2_p8);
		ivRow2PlayedLetter8.setTextSize(this.letterTileSize);

		ViewGroup.LayoutParams params_r2_p9 = ivRow2PlayedLetter9.getLayoutParams();
		params_r2_p9.width = this.playedLetterTileSize;
		params_r2_p9.height = this.playedLetterTileSize;
		ivRow2PlayedLetter9.setLayoutParams(params_r2_p9);
		ivRow2PlayedLetter9.setTextSize(this.letterTileSize);

		ViewGroup.LayoutParams params_r2_p10 = ivRow2PlayedLetter10.getLayoutParams();
		params_r2_p10.width = this.playedLetterTileSize;
		params_r2_p10.height = this.playedLetterTileSize;
		ivRow2PlayedLetter10.setLayoutParams(params_r2_p10);
		ivRow2PlayedLetter10.setTextSize(this.letterTileSize);
		
		
		ViewGroup.LayoutParams params_r3_p1 = ivRow3PlayedLetter1.getLayoutParams();
		params_r3_p1.width = this.playedLetterTileSize;
		params_r3_p1.height = this.playedLetterTileSize;
		ivRow3PlayedLetter1.setLayoutParams(params_r3_p1);
		ivRow3PlayedLetter1.setTextSize(this.letterTileSize);
		
		ViewGroup.LayoutParams params_r3_p2 = ivRow3PlayedLetter2.getLayoutParams();
		params_r3_p2.width = this.playedLetterTileSize;
		params_r3_p2.height = this.playedLetterTileSize;
		ivRow3PlayedLetter2.setLayoutParams(params_r3_p2);
		ivRow3PlayedLetter2.setTextSize(this.letterTileSize);
		
		ViewGroup.LayoutParams params_r3_p3 = ivRow3PlayedLetter3.getLayoutParams();
		params_r3_p3.width = this.playedLetterTileSize;
		params_r3_p3.height = this.playedLetterTileSize;
		ivRow3PlayedLetter3.setLayoutParams(params_r3_p3);
		ivRow3PlayedLetter3.setTextSize(this.letterTileSize);

		ViewGroup.LayoutParams params_r3_p4 = ivRow3PlayedLetter4.getLayoutParams();
		params_r3_p4.width = this.playedLetterTileSize;
		params_r3_p4.height = this.playedLetterTileSize;
		ivRow3PlayedLetter4.setLayoutParams(params_r3_p4);
		ivRow3PlayedLetter4.setTextSize(this.letterTileSize);
		
		ViewGroup.LayoutParams params_r3_p5 = ivRow3PlayedLetter5.getLayoutParams();
		params_r3_p5.width = this.playedLetterTileSize;
		params_r3_p5.height = this.playedLetterTileSize;
		ivRow3PlayedLetter5.setLayoutParams(params_r3_p5);
		ivRow3PlayedLetter5.setTextSize(this.letterTileSize);

		ViewGroup.LayoutParams params_r3_p6 = ivRow3PlayedLetter6.getLayoutParams();
		params_r3_p6.width = this.playedLetterTileSize;
		params_r3_p6.height = this.playedLetterTileSize;
		ivRow3PlayedLetter6.setLayoutParams(params_r3_p6);
		ivRow3PlayedLetter6.setTextSize(this.letterTileSize);

		ViewGroup.LayoutParams params_r3_p7 = ivRow3PlayedLetter7.getLayoutParams();
		params_r3_p7.width = this.playedLetterTileSize;
		params_r3_p7.height = this.playedLetterTileSize;
		ivRow3PlayedLetter7.setLayoutParams(params_r3_p7);
		ivRow3PlayedLetter7.setTextSize(this.letterTileSize);

		ViewGroup.LayoutParams params_r3_p8 = ivRow3PlayedLetter8.getLayoutParams();
		params_r3_p8.width = this.playedLetterTileSize;
		params_r3_p8.height = this.playedLetterTileSize;
		ivRow3PlayedLetter8.setLayoutParams(params_r3_p8);
		ivRow3PlayedLetter8.setTextSize(this.letterTileSize);

		ViewGroup.LayoutParams params_r3_p9 = ivRow3PlayedLetter9.getLayoutParams();
		params_r3_p9.width = this.playedLetterTileSize;
		params_r3_p9.height = this.playedLetterTileSize;
		ivRow3PlayedLetter9.setLayoutParams(params_r3_p9);
		ivRow3PlayedLetter9.setTextSize(this.letterTileSize);

		ViewGroup.LayoutParams params_r3_p10 = ivRow3PlayedLetter10.getLayoutParams();
		params_r3_p10.width = this.playedLetterTileSize;
		params_r3_p10.height = this.playedLetterTileSize;
		ivRow3PlayedLetter10.setLayoutParams(params_r3_p10);
		ivRow3PlayedLetter10.setTextSize(this.letterTileSize);
	}
	
	private void preloadPlayedTiles(){
		 if (GameSurface.bgTray == null){
			 GameSurface.bgTray = ImageHelper.decodeSampledBitmapFromResource(this.getResources(), com.rozen.wordscroll.R.drawable.tray_letter_bg, this.playedLetterTileSize, this.playedLetterTileSize);
		 }
 
			this.ivRow1PlayedLetter1.setBackground(new BitmapDrawable(getResources(), GameSurface.bgTray));
			this.ivRow1PlayedLetter2.setBackground(new BitmapDrawable(getResources(), GameSurface.bgTray));
			this.ivRow1PlayedLetter3.setBackground(new BitmapDrawable(getResources(), GameSurface.bgTray));
			this.ivRow1PlayedLetter4.setBackground(new BitmapDrawable(getResources(), GameSurface.bgTray));
			this.ivRow1PlayedLetter5.setBackground(new BitmapDrawable(getResources(), GameSurface.bgTray));
			this.ivRow1PlayedLetter6.setBackground(new BitmapDrawable(getResources(), GameSurface.bgTray));
			this.ivRow1PlayedLetter7.setBackground(new BitmapDrawable(getResources(), GameSurface.bgTray));
			this.ivRow1PlayedLetter8.setBackground(new BitmapDrawable(getResources(), GameSurface.bgTray));
			this.ivRow1PlayedLetter9.setBackground(new BitmapDrawable(getResources(), GameSurface.bgTray));
			this.ivRow1PlayedLetter10.setBackground(new BitmapDrawable(getResources(), GameSurface.bgTray));
			
			this.ivRow2PlayedLetter1.setBackground(new BitmapDrawable(getResources(), GameSurface.bgTray));
			this.ivRow2PlayedLetter2.setBackground(new BitmapDrawable(getResources(), GameSurface.bgTray));
			this.ivRow2PlayedLetter3.setBackground(new BitmapDrawable(getResources(), GameSurface.bgTray));
			this.ivRow2PlayedLetter4.setBackground(new BitmapDrawable(getResources(), GameSurface.bgTray));
			this.ivRow2PlayedLetter5.setBackground(new BitmapDrawable(getResources(), GameSurface.bgTray));
			this.ivRow2PlayedLetter6.setBackground(new BitmapDrawable(getResources(), GameSurface.bgTray));
			this.ivRow2PlayedLetter7.setBackground(new BitmapDrawable(getResources(), GameSurface.bgTray));
			this.ivRow2PlayedLetter8.setBackground(new BitmapDrawable(getResources(), GameSurface.bgTray));
			this.ivRow2PlayedLetter9.setBackground(new BitmapDrawable(getResources(), GameSurface.bgTray));
			this.ivRow2PlayedLetter10.setBackground(new BitmapDrawable(getResources(), GameSurface.bgTray));
			
			this.ivRow3PlayedLetter1.setBackground(new BitmapDrawable(getResources(), GameSurface.bgTray));
			this.ivRow3PlayedLetter2.setBackground(new BitmapDrawable(getResources(), GameSurface.bgTray));
			this.ivRow3PlayedLetter3.setBackground(new BitmapDrawable(getResources(), GameSurface.bgTray));
			this.ivRow3PlayedLetter4.setBackground(new BitmapDrawable(getResources(), GameSurface.bgTray));
			this.ivRow3PlayedLetter5.setBackground(new BitmapDrawable(getResources(), GameSurface.bgTray));
			this.ivRow3PlayedLetter6.setBackground(new BitmapDrawable(getResources(), GameSurface.bgTray));
			this.ivRow3PlayedLetter7.setBackground(new BitmapDrawable(getResources(), GameSurface.bgTray));
			this.ivRow3PlayedLetter8.setBackground(new BitmapDrawable(getResources(), GameSurface.bgTray));
			this.ivRow3PlayedLetter9.setBackground(new BitmapDrawable(getResources(), GameSurface.bgTray));
			this.ivRow3PlayedLetter10.setBackground(new BitmapDrawable(getResources(), GameSurface.bgTray));
 
	}
	
	
	public void captureTime(String text){
		ApplicationContext.captureTime(TAG, text);
 
	}
	
	public boolean onTileClick(Tile tile){
		if (tile.getRow() == 1 && this.row1Tiles.size() >= 10){ //10 is the max size
			return false;
		}
		else if (tile.getRow() == 2 && this.row2Tiles.size() >= 10){ //10 is the max size
			return false;
		}
		else if (tile.getRow() == 3 && this.row3Tiles.size() >= 10){ //10 is the max size
			return false;
		}
		
		if (tile.getRow() == 1){  
			for (PlayedTile t : this.row1Tiles){
				if (t.getLetter().equals("")){
					t.setLetter(tile.getLetter());
					t.setTileId(tile.getId());
					break;
				}
			}				
		}
		else if (tile.getRow() == 2){ 
			for (PlayedTile t : this.row2Tiles){
				if (t.getLetter().equals("")){
					t.setLetter(tile.getLetter());
					t.setTileId(tile.getId());
					break;
				}
			} 
		}
		else if (tile.getRow() == 3){  
			for (PlayedTile t : this.row2Tiles){
				if (t.getLetter().equals("")){
					t.setLetter(tile.getLetter());
					t.setTileId(tile.getId());
					break;
				}
			}
		}
		
		return true;
	}
	
	private void setGame(){
		Intent i = getIntent();
		
		String gameId = "";
	 	//String gameId = i.getStringExtra(Constants.EXTRA_GAME_ID);
	 	boolean fromCompletedGameList = i.getBooleanExtra(Constants.EXTRA_FROM_COMPLETED_GAME_LIST, false);
	 	
	 	//do this so that back button does not get crazy if one navigates to game from completed game list continuously
	 	if (fromCompletedGameList){
	 		MenuUtils.hideMenu(this);
	 	}
	 	//this.game = (Game) i.getParcelableExtra(Constants.EXTRA_GAME);
	 	
	 	this.captureTime("get game from local starting");
	 	
	 	//check to see if we are coming back from a child/downstream activity after game is over
	 //	if (this.game != null && this.game.isCompleted() && !this.gameId.equals("")){
	 //		//just keep gameId that is in context already
	 //	}
	 //	else{
	 //		this.gameId = "";
	 //	}
	 	
	 	if (this.game == null || gameId.equals("")){
		 	gameId = this.getPlayer().getActiveGameId();
		 	
		 	if (fromCompletedGameList){
		 		gameId = i.getStringExtra(Constants.EXTRA_GAME_ID);
		 	} 
	 	}
	 	
	 	if (gameId == null || gameId.equals("")){
	 		Logger.d(TAG, "setGame starting new game");
	 		//start new game
	 		try {
				this.game = GameService.createGame(this, this.getPlayer());
				Logger.d(TAG, "setGame created=" + gameId);
			} catch (DesignByContractException e) {
				// TODO Auto-generated catch block
				Logger.d(TAG, "setGame error:" + e.getMessage() );
			}
	 	}
	 	else{
	 		Logger.d(TAG, "setGame gameId=" + gameId);
	 		this.game = GameService.getGame(gameId);
	 	}
  }
	
	protected void setupFonts(){

		this.ivRow1PlayedLetter1.setTypeface(ApplicationContext.getLetterTypeface());
		this.ivRow1PlayedLetter2.setTypeface(ApplicationContext.getLetterTypeface());
		this.ivRow1PlayedLetter3.setTypeface(ApplicationContext.getLetterTypeface());
		this.ivRow1PlayedLetter4.setTypeface(ApplicationContext.getLetterTypeface());
		this.ivRow1PlayedLetter5.setTypeface(ApplicationContext.getLetterTypeface());
		this.ivRow1PlayedLetter6.setTypeface(ApplicationContext.getLetterTypeface());
		this.ivRow1PlayedLetter7.setTypeface(ApplicationContext.getLetterTypeface());
		this.ivRow1PlayedLetter8.setTypeface(ApplicationContext.getLetterTypeface());
		this.ivRow1PlayedLetter9.setTypeface(ApplicationContext.getLetterTypeface());
		this.ivRow1PlayedLetter10.setTypeface(ApplicationContext.getLetterTypeface());
		
		this.ivRow2PlayedLetter1.setTypeface(ApplicationContext.getLetterTypeface());
		this.ivRow2PlayedLetter2.setTypeface(ApplicationContext.getLetterTypeface());
		this.ivRow2PlayedLetter3.setTypeface(ApplicationContext.getLetterTypeface());
		this.ivRow2PlayedLetter4.setTypeface(ApplicationContext.getLetterTypeface());
		this.ivRow2PlayedLetter5.setTypeface(ApplicationContext.getLetterTypeface());
		this.ivRow2PlayedLetter6.setTypeface(ApplicationContext.getLetterTypeface());
		this.ivRow2PlayedLetter7.setTypeface(ApplicationContext.getLetterTypeface());
		this.ivRow2PlayedLetter8.setTypeface(ApplicationContext.getLetterTypeface());
		this.ivRow2PlayedLetter9.setTypeface(ApplicationContext.getLetterTypeface());
		this.ivRow2PlayedLetter10.setTypeface(ApplicationContext.getLetterTypeface());
		
		this.ivRow3PlayedLetter1.setTypeface(ApplicationContext.getLetterTypeface());
		this.ivRow3PlayedLetter2.setTypeface(ApplicationContext.getLetterTypeface());
		this.ivRow3PlayedLetter3.setTypeface(ApplicationContext.getLetterTypeface());
		this.ivRow3PlayedLetter4.setTypeface(ApplicationContext.getLetterTypeface());
		this.ivRow3PlayedLetter5.setTypeface(ApplicationContext.getLetterTypeface());
		this.ivRow3PlayedLetter6.setTypeface(ApplicationContext.getLetterTypeface());
		this.ivRow3PlayedLetter7.setTypeface(ApplicationContext.getLetterTypeface());
		this.ivRow3PlayedLetter8.setTypeface(ApplicationContext.getLetterTypeface());
		this.ivRow3PlayedLetter9.setTypeface(ApplicationContext.getLetterTypeface());
		this.ivRow3PlayedLetter10.setTypeface(ApplicationContext.getLetterTypeface());
		
	}
}
