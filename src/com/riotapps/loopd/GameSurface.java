package com.riotapps.loopd;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentActivity;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;

 
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chartboost.sdk.Chartboost;
import com.chartboost.sdk.ChartboostDelegate;
import com.google.ads.AdRequest;
import com.google.ads.AdView;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Tracker;
import com.riotapps.loopd.R;
import com.riotapps.loopd.hooks.Fragment;
import com.riotapps.loopd.hooks.FragmentService;
import com.riotapps.loopd.hooks.Game;
import com.riotapps.loopd.hooks.GameService;
import com.riotapps.loopd.hooks.PlayedTile;
import com.riotapps.loopd.hooks.Tile;
import com.riotapps.loopd.ui.GameSurfaceView;
import com.riotapps.wordbase.hooks.PlayedWord;
import com.riotapps.wordbase.hooks.Player;
import com.riotapps.wordbase.hooks.PlayerService;
import com.riotapps.wordbase.hooks.StoreService;
import com.riotapps.wordbase.hooks.WordService;
import com.riotapps.wordbase.interfaces.ICloseDialog;
import com.riotapps.wordbase.services.WordLoaderService;
import com.riotapps.wordbase.ui.CustomButtonDialog;
import com.riotapps.wordbase.ui.CustomProgressDialog;
import com.riotapps.wordbase.ui.CustomToast;
import com.riotapps.wordbase.ui.MenuUtils;
import com.riotapps.wordbase.utils.ApplicationContext;
import com.riotapps.wordbase.utils.Constants;
import com.riotapps.wordbase.utils.DesignByContractException;
import com.riotapps.wordbase.utils.ImageHelper;
import com.riotapps.wordbase.utils.Logger;
import com.riotapps.wordbase.utils.Utils;
 
 

public class GameSurface  extends FragmentActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener, ICloseDialog{
	private static final String TAG = GameSurface.class.getSimpleName();
	private GameSurfaceView gameSurfaceView;
	
	private Game game;
	private Player player;
	private PopupMenu popupMenu;
	private List<Fragment> fragments = new ArrayList<Fragment>();
	private LoadFragmentsTask loadFragmentsTask;
	private OnPauseTask onPauseTask;
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
	
	private TextView tvPlayedWord1Title;
	private TextView tvPlayedWord2Title;
	private TextView tvPlayedWord3Title;
	
	private Button bPlay1;
	private Button bPlay2;
	private Button bPlay3;
	
	private int defaultPlayedTileTextColor;
	private int playedTileTextColor;
	private boolean surfaceViewReadyToPause = false;
	private boolean isPaused = false;
	
	private List<PlayedTile> row1Tiles = new ArrayList<PlayedTile>();
	private List<PlayedTile> row2Tiles = new ArrayList<PlayedTile>();
	private List<PlayedTile> row3Tiles = new ArrayList<PlayedTile>();
	
	private TextView tvCountdown;
	private TextView tvScore;
	private TextView tvTopScore;
	
	private int playedLetterTileSize;
	private float letterTileSize;
	private boolean freezeAction = false;
	private ListView lvPlayedWords;
	
//	private ImageView ivStart;
	private LinearLayout llPlayedWords;
	private LinearLayout llAdWrapper;
	//private LinearLayout llStart;
	
	private playedWordAdapter playedWordAdapter;
	
	private List<PlayedWord> playedWords =  new ArrayList<PlayedWord>();
	
	private CountDownTimer countdown = null;
	protected boolean isCountdownRunning;
	private boolean isChartBoostActive;
	private CustomButtonDialog customDialog;
	private CustomProgressDialog spinner;
	private boolean hasPostAdRun;
	private boolean hideInterstitialAd;
	private Chartboost cb;
	private Tracker tracker;
	private LinearLayout llButtons;
	private int bottomHeight;
	private int valueOfBlankTilesBeforeFirstDefault = 2;
	
	private static Bitmap bgTray = null;
	
	
	
	public int getBottomHeight() {
		return bottomHeight;
	}

	public void setBottomHeight(int bottomHeight) {
		this.bottomHeight = bottomHeight;
	}

	public boolean isSurfaceViewReadyToPause() {
		return surfaceViewReadyToPause;
	}

	public void setSurfaceViewReadyToPause(boolean surfaceViewReadyToPause) {
		this.surfaceViewReadyToPause = surfaceViewReadyToPause;
	}
	 public Tracker getTracker() {
			if (this.tracker == null){
				this.tracker = EasyTracker.getTracker();
			}
			return tracker;
		 }


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
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		Logger.d(TAG, "onCreate called");
		
	//	ImageView ivLetter = (ImageView)this.findViewById(R.id.ivLetter);
//		ivLetter.setOnClickListener(this);
		
		 
	 //	this.setupPreloadTask();
		
		this.kickoff();
	}
	@Override
	public void dialogClose(int resultCode) {
		 switch(resultCode) { 
		   case Constants.RETURN_CODE_CUSTOM_TOAST_READY_FINISHED:
			   this.onStartSet();
			   break;
		   case Constants.RETURN_CODE_CUSTOM_TOAST_SET_FINISHED:
			   this.onStartGo();
			   break;
		   case Constants.RETURN_CODE_CUSTOM_TOAST_GO_FINISHED:
			   this.onStartGoFinished();
			   break;
		 }
		
	}
	
	@Override
	protected void onPause() {
		Logger.d(TAG, "onPause");
		super.onPause();
		
		this.isPaused = true;

		this.gameSurfaceView.onPause();
		
		this.onPauseTask = new OnPauseTask();
		this.onPauseTask.execute();
		
	}
	
	private void finishOnPause(){
		Logger.d(TAG, "finishOnPause called");
		this.onPauseTask = null;
		//wait until surfaceview is ready to pause
	    if (this.game.isActive()){
			Logger.d(TAG, "saveGame about to be called");
	    	GameService.saveGame(this.game);
	    }
	}
	

	@Override
	protected void onDestroy() {
		Logger.d(TAG, "onDestroy");
		super.onDestroy();
		
		this.gameSurfaceView.onDestroy();
	}

	@Override
	protected void onStart() {
		Logger.d(TAG, "onStart");
		super.onStart();
	}
	
	@Override
	protected void onRestart() {
		Logger.d(TAG, "onRestart");
		super.onRestart();
		this.gameSurfaceView.onRestart(); 
	}
	
	@Override
	protected void onStop() {
		Logger.d(TAG, "onStop");
		super.onStop();
		
		this.gameSurfaceView.onStop();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		Logger.d(TAG, "onResume");
		super.onResume();
		this.isPaused = false;
		this.surfaceViewReadyToPause = false;
		if (this.game.isActive()){
			this.gameSurfaceView.onResume();
		}
	}

	@Override
	public void onBackPressed() {
		Logger.d(TAG, "onBackPressed");
		// TODO Auto-generated method stub
		//super.onBackPressed();
		//override back button in case user just started game. this will make sure they don;t back through 
		//all of the pick opponent activities
 
		//if (this.isChartBoostActive && this.getCB().onBackPressed())
			// If a Chartboost view exists, close it and return
		//	return;
		//else {
			//if game is completed, just go back to whatever activity is in the stack
			//this.gameSurfaceView.onStop();
			//if (this.game.isActive()){
			//	GameService.saveGame(this.game);
			//}
			
		//	this.game = null;
		//	this.player = null;
			
			super.onBackPressed();
		//}
		 
	}
	
	  private void setupMenu(){
	      	
	  	  this.popupMenu = new PopupMenu(this, findViewById(R.id.options));

	  	  MenuUtils.fillMenuNoGames(this, this.popupMenu);
	      this.popupMenu.setOnMenuItemClickListener(this);
	      findViewById(R.id.options).setOnClickListener(this);
	  }
	    @Override
	    public boolean onMenuItemClick(MenuItem item) {
	    	Logger.d(TAG, "onMenuItemClick");
	    	//probably need to stop thread here
	    	return MenuUtils.handleMenuClick(this, item.getItemId());
	    }
	    
	private void kickoff(){
		this.isPaused = false;
		this.setGame(); 
		this.defaultPlayedTileTextColor = this.getResources().getColor(R.color.default_played_tile_letter);
		this.playedTileTextColor = this.getResources().getColor(R.color.played_tile_letter);
		
	 
		
		//temp
//		String s = "";
		//for(int i = 0; i < 10; i ++){
		//	this.game.getRow1Tiles().remove(0);
		//}
//	 	for (Tile t : this.game.getRow1Tiles()){ 
//	 		 s += t.getLetter() + " ";// + " id=" + t.getId());
//	 	}
//	 	 Logger.d("TAG", "hopper=" +  s);// + " id=" + t.getId());
//	 	 
//	 	for (Tile t : this.game.getRow1Tiles()){
//	 		Logger.d("TAG", "hopper id=" + t.getId() + " letter=" + t.getLetter() + " played=" + t.isPlayed());
//	 	}
	    
		this.loadViews();
		this.setBottom();

	    this.gameSurfaceView = (GameSurfaceView)findViewById(R.id.gameSurface);
	    this.gameSurfaceView.construct(this);

	    
		this.setupFonts();
		this.setViewLayouts();
 
		this.initializeGameOnBoard();		
		
	}
	
 
	
	private void initializeGameOnBoard(){
		this.preloadPlayedTiles();		
		this.initializeWordList();
	
		this.loadFragmentsTask = new LoadFragmentsTask();
		this.loadFragmentsTask.execute(); 
		
		this.resetPoints();
		this.setupMenu();
		
	}
	
	
	private class OnPauseTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... arg0) {
			// wait until the word loader service is finished because the first time 
			//through the database will not have been copied over yet
			//x is safety valve
			
			//this method will just whistle a tune until the database is copied over
			//it should happen very fast
			int x = 0;
			
			while( x < 5000 ){
				
				if ( GameSurface.this.surfaceViewReadyToPause){
					Logger.d(TAG, "surface view finished its loop");
					break;
				}
				else{
					Logger.d(TAG, "surface view NOT finished its loop");
				}
				
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			 
			
			return null;
		}
		
	      @Override
	      protected void onPostExecute(Void arg0) {  
	    	  GameSurface.this.finishOnPause();
	    	 
	      }
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
	

	private void setupAdServer(){
		Logger.d(TAG, "setupAdServer called");
		
	 	boolean isAdMob = Constants.INTERSTITIAL_ADMOB;
	 	boolean isChartBoost = Constants.INTERSTITIAL_CHARTBOOST;
		boolean isRevMob = false; //Constants.INTERSTITIAL_REVMOB;
		final int useRevMob = 0;
		
	 	if (!StoreService.isHideInterstitialAdPurchased(this))
	 	{
	 	
	 		if (isChartBoost){
	 			Logger.d(TAG, "setupAdServer isCharBoost=true");
	 			
		 		this.isChartBoostActive = true;
		 		this.setupChartBoost();
	 		}
		 	}
	 	else{
	 		this.hideInterstitialAd = true;
	 	}
	 		 		
	 	
	 	Logger.d(TAG,  " chartBoost=" + this.isChartBoostActive);
	}

	private void handleInterstitialAd(){
    	this.hasPostAdRun = false; 
    	if (this.hideInterstitialAd){
    		this.handlePostAdServer();   		            	 					
 			}
 		else{
 			long hoursSinceLastReminder = PlayerService.getHoursSinceLastInterstitialPurchaseReminder();
 			
 			Logger.d(TAG, "handleInterstitialAd hoursSinceLastReminder=" + hoursSinceLastReminder);
 			
 			if (hoursSinceLastReminder == Constants.DEFAULT_HIDE_AD_PURCHASE_REMINDER){
 				//save it for the first time
 				PlayerService.setLastInterstitialPurchaseReminderTime();
 			}
 			
 			if (hoursSinceLastReminder >= 24  ){
 				this.customDialog = new CustomButtonDialog(this, 
 		    			this.getString(R.string.game_surface_interstital_purchase_title), 
 		    			this.getString(R.string.game_surface_interstital_purchase_text),
 		    			this.getString(R.string.go_to_store),
 		    			this.getString(R.string.no_thanks),
 		    			Constants.RETURN_CODE_CUSTOM_DIALOG_INTERSTITIAL_REMINDER_OK_CLICKED,
 		    			Constants.RETURN_CODE_CUSTOM_DIALOG_INTERSTITIAL_REMINDER_CANCEL_CLICKED,
 		    			Constants.RETURN_CODE_CUSTOM_DIALOG_INTERSTITIAL_REMINDER_CLOSE_CLICKED);
 				
 				//public static final int RETURN_CODE_CUSTOM_DIALOG_INTERSTITIAL_REMINDER_OK_CLICKED = 133;	
 				//public static final int RETURN_CODE_CUSTOM_DIALOG_INTERSTITIAL_REMINDER_CLOSE_CLICKED = 134;
 				//public static final int RETURN_CODE_CUSTOM_DIALOG_INTERSTITIAL_REMINDER_CANCEL_CLICKED = 135;	
 		    
 				PlayerService.setLastInterstitialPurchaseReminderTime();
 		    	this.customDialog.show();
 			}
 			else{
	 			if (this.isChartBoostActive) {
	 				if (this.spinner == null){
	 					this.spinner = new CustomProgressDialog(this);
	 					this.spinner.setMessage(this.getString(R.string.progress_almost_ready));
	 					this.spinner.show();				
	 				}
	 				
		 			this.cb.setTimeout((int)Constants.GAME_SURFACE_INTERSTITIAL_AD_CHECK_IN_MILLISECONDS);
		 			this.cb.showInterstitial();
			    	Logger.d(TAG, "showInterstitial from Chartboost");
 	 			}
 			}
			}
    }

	
	public void handlePostAdServer(){
	    //	if (!this.hasPostAdRun &&  this.postTurnMessage.length() > 0){  //chartboost dismiss and close both call this, lets make sure its not run twice
    		 	
	    		//make sure the pre-primed hintlist is cleared
	    	//	this.placedResultsForWordHints.clear();
			//	this.hints.clear();

	    	this.hasPostAdRun = true;
    		// 	this.unfreezeButtons();
    		// 	Logger.d(TAG, "unfreeze handlePostAdServer");
    			if (spinner != null) {
    		 		spinner.dismiss();
    		 		spinner = null;
    		 	}

	    	//}
	    }
	private void initializePlayedLists(){
 
		this.initializePlayedList_1();
		this.initializePlayedList_2();
		this.initializePlayedList_3();

	}
	
	private void initializePlayedList_1(){
		this.row1Tiles.clear();
		
		if (this.fragments.size() == 0){
			this.loadFragments(40);
		}
		
		String[] fragment1_arr = this.fragments.get(0).getLetters().split("");
	 	List<fragmentLetter> fragmentLetters_1 =  new ArrayList<fragmentLetter>();
		
	 	//load letters into temp lists for processing into PlayedTiles
	 	//subtracting 1 from 1 because java adds an extra item when splitting, very annoying
		for (int i = 1; i < fragment1_arr.length; i++){
			fragmentLetters_1.add(new fragmentLetter(fragment1_arr[i], this.fragments.get(0).getStartingPosition() + i - 1));
		}
		
		//load all 10 playedTiles
		for (int i = 1; i <= 10; i++){
			PlayedTile playedTile = PlayedTile.getDefaultInstance();
			
			playedTile.setLetter("");
			playedTile.setDefault(false);
			playedTile.setPosition(i);
			playedTile.setTileId(-1);
			
			//determine if letter should be preloaded from fragment
			for (fragmentLetter fragmentLetter : fragmentLetters_1){
				if (fragmentLetter.getPosition() == i){
					playedTile.setLetter(fragmentLetter.getLetter());
					playedTile.setDefault(true);
				}
			}

			this.row1Tiles.add(playedTile);
		}
		
		//remove fragment so that it does not get reused this game
		this.fragments.remove(0);
	}
	
	private void initializePlayedList_2(){
		this.row2Tiles.clear();
		
		if (this.fragments.size() == 0){
			this.loadFragments(40);
		}
		
		String[] fragment2_arr = this.fragments.get(0).getLetters().split("");
	 	List<fragmentLetter> fragmentLetters_2 =  new ArrayList<fragmentLetter>();
		
		for (int i = 1; i < fragment2_arr.length; i++){
			fragmentLetters_2.add(new fragmentLetter(fragment2_arr[i], this.fragments.get(0).getStartingPosition() + i - 1));
		}
		
		//load all 10 playedTiles
		for (int i = 1; i <= 10; i++){
			PlayedTile playedTile = PlayedTile.getDefaultInstance();
			
			playedTile.setLetter("");
			playedTile.setDefault(false);
			playedTile.setPosition(i);
			playedTile.setTileId(-1);
			
			//determine if letter should be preloaded from fragment
			for (fragmentLetter fragmentLetter : fragmentLetters_2){
				if (fragmentLetter.getPosition() == i){
					playedTile.setLetter(fragmentLetter.getLetter());
					playedTile.setDefault(true);
				}
			}

			this.row2Tiles.add(playedTile);
		}
		
		//remove fragment so that it does not get reused this game
		this.fragments.remove(0);
	}
	
	private void initializePlayedList_3(){
		this.row3Tiles.clear();
		
		if (this.fragments.size() == 0){
			this.loadFragments(40);
		}
		
		String[] fragment3_arr = this.fragments.get(0).getLetters().split("");
	 	List<fragmentLetter> fragmentLetters_3 =  new ArrayList<fragmentLetter>();
		
		for (int i = 1; i < fragment3_arr.length; i++){
			fragmentLetters_3.add(new fragmentLetter(fragment3_arr[i], this.fragments.get(0).getStartingPosition() + i - 1));
		}
		
		//load all 10 playedTiles
		for (int i = 1; i <= 10; i++){
			PlayedTile playedTile = PlayedTile.getDefaultInstance();
			
			playedTile.setLetter("");
			playedTile.setDefault(false);
			playedTile.setPosition(i);
			playedTile.setTileId(-1);
			
			//determine if letter should be preloaded from fragment
			for (fragmentLetter fragmentLetter : fragmentLetters_3){
				if (fragmentLetter.getPosition() == i){
					playedTile.setLetter(fragmentLetter.getLetter());
					playedTile.setDefault(true);
				}
			}

			this.row3Tiles.add(playedTile);
		}
		
		//remove fragment so that it does not get reused this game
		this.fragments.remove(0);
	}
	
	private void loadFragmentsAndView(){
		this.loadFragmentsTask = null;
		
		this.loadFragments(40);
		this.initializePlayedLists();  ///this will have to change if game is already started
		this.loadPlayedTileViews();
	}
	
	private int getPlayedTileTextColor(boolean isDefault){
		return isDefault ? this.defaultPlayedTileTextColor : this.playedTileTextColor;
	}
	
	private void loadPlayedWord_1(){
		
		this.ivRow1PlayedLetter1.setText(this.row1Tiles.get(0).getLetter());
		this.ivRow1PlayedLetter1.setTextColor(getPlayedTileTextColor(this.row1Tiles.get(0).isDefault()));
		this.ivRow1PlayedLetter2.setText(this.row1Tiles.get(1).getLetter());
		this.ivRow1PlayedLetter2.setTextColor(getPlayedTileTextColor(this.row1Tiles.get(1).isDefault()));
		this.ivRow1PlayedLetter3.setText(this.row1Tiles.get(2).getLetter());
		this.ivRow1PlayedLetter3.setTextColor(getPlayedTileTextColor(this.row1Tiles.get(2).isDefault()));
		this.ivRow1PlayedLetter4.setText(this.row1Tiles.get(3).getLetter());
		this.ivRow1PlayedLetter4.setTextColor(getPlayedTileTextColor(this.row1Tiles.get(3).isDefault()));
		this.ivRow1PlayedLetter5.setText(this.row1Tiles.get(4).getLetter());
		this.ivRow1PlayedLetter5.setTextColor(getPlayedTileTextColor(this.row1Tiles.get(4).isDefault()));
		this.ivRow1PlayedLetter6.setText(this.row1Tiles.get(5).getLetter());
		this.ivRow1PlayedLetter6.setTextColor(getPlayedTileTextColor(this.row1Tiles.get(5).isDefault()));
		this.ivRow1PlayedLetter7.setText(this.row1Tiles.get(6).getLetter());
		this.ivRow1PlayedLetter7.setTextColor(getPlayedTileTextColor(this.row1Tiles.get(6).isDefault()));
		this.ivRow1PlayedLetter8.setText(this.row1Tiles.get(7).getLetter());
		this.ivRow1PlayedLetter8.setTextColor(getPlayedTileTextColor(this.row1Tiles.get(7).isDefault()));
		this.ivRow1PlayedLetter9.setText(this.row1Tiles.get(8).getLetter());
		this.ivRow1PlayedLetter9.setTextColor(getPlayedTileTextColor(this.row1Tiles.get(8).isDefault()));
		this.ivRow1PlayedLetter10.setText(this.row1Tiles.get(9).getLetter());
		this.ivRow1PlayedLetter10.setTextColor(getPlayedTileTextColor(this.row1Tiles.get(9).isDefault()));
	}
	
	private void loadPlayedWord_2(){
		
		this.ivRow2PlayedLetter1.setText(this.row2Tiles.get(0).getLetter());
		this.ivRow2PlayedLetter1.setTextColor(getPlayedTileTextColor(this.row2Tiles.get(0).isDefault()));
		this.ivRow2PlayedLetter2.setText(this.row2Tiles.get(1).getLetter());
		this.ivRow2PlayedLetter2.setTextColor(getPlayedTileTextColor(this.row2Tiles.get(1).isDefault()));
		this.ivRow2PlayedLetter3.setText(this.row2Tiles.get(2).getLetter());
		this.ivRow2PlayedLetter3.setTextColor(getPlayedTileTextColor(this.row2Tiles.get(2).isDefault()));
		this.ivRow2PlayedLetter4.setText(this.row2Tiles.get(3).getLetter());
		this.ivRow2PlayedLetter4.setTextColor(getPlayedTileTextColor(this.row2Tiles.get(3).isDefault()));
		this.ivRow2PlayedLetter5.setText(this.row2Tiles.get(4).getLetter());
		this.ivRow2PlayedLetter5.setTextColor(getPlayedTileTextColor(this.row2Tiles.get(4).isDefault()));
		this.ivRow2PlayedLetter6.setText(this.row2Tiles.get(5).getLetter());
		this.ivRow2PlayedLetter6.setTextColor(getPlayedTileTextColor(this.row2Tiles.get(5).isDefault()));
		this.ivRow2PlayedLetter7.setText(this.row2Tiles.get(6).getLetter());
		this.ivRow2PlayedLetter7.setTextColor(getPlayedTileTextColor(this.row2Tiles.get(6).isDefault()));
		this.ivRow2PlayedLetter8.setText(this.row2Tiles.get(7).getLetter());
		this.ivRow2PlayedLetter8.setTextColor(getPlayedTileTextColor(this.row2Tiles.get(7).isDefault()));
		this.ivRow2PlayedLetter9.setText(this.row2Tiles.get(8).getLetter());
		this.ivRow2PlayedLetter9.setTextColor(getPlayedTileTextColor(this.row2Tiles.get(8).isDefault()));
		this.ivRow2PlayedLetter10.setText(this.row2Tiles.get(9).getLetter());
		this.ivRow2PlayedLetter10.setTextColor(getPlayedTileTextColor(this.row2Tiles.get(9).isDefault()));
	}
	
	private void loadPlayedWord_3(){
		
		this.ivRow3PlayedLetter1.setText(this.row3Tiles.get(0).getLetter());
		this.ivRow3PlayedLetter1.setTextColor(getPlayedTileTextColor(this.row3Tiles.get(0).isDefault()));
		this.ivRow3PlayedLetter2.setText(this.row3Tiles.get(1).getLetter());
		this.ivRow3PlayedLetter2.setTextColor(getPlayedTileTextColor(this.row3Tiles.get(1).isDefault()));
		this.ivRow3PlayedLetter3.setText(this.row3Tiles.get(2).getLetter());
		this.ivRow3PlayedLetter3.setTextColor(getPlayedTileTextColor(this.row3Tiles.get(2).isDefault()));
		this.ivRow3PlayedLetter4.setText(this.row3Tiles.get(3).getLetter());
		this.ivRow3PlayedLetter4.setTextColor(getPlayedTileTextColor(this.row3Tiles.get(3).isDefault()));
		this.ivRow3PlayedLetter5.setText(this.row3Tiles.get(4).getLetter());
		this.ivRow3PlayedLetter5.setTextColor(getPlayedTileTextColor(this.row3Tiles.get(4).isDefault()));
		this.ivRow3PlayedLetter6.setText(this.row3Tiles.get(5).getLetter());
		this.ivRow3PlayedLetter6.setTextColor(getPlayedTileTextColor(this.row3Tiles.get(5).isDefault()));
		this.ivRow3PlayedLetter7.setText(this.row3Tiles.get(6).getLetter());
		this.ivRow3PlayedLetter7.setTextColor(getPlayedTileTextColor(this.row3Tiles.get(6).isDefault()));
		this.ivRow3PlayedLetter8.setText(this.row3Tiles.get(7).getLetter());
		this.ivRow3PlayedLetter8.setTextColor(getPlayedTileTextColor(this.row3Tiles.get(7).isDefault()));
		this.ivRow3PlayedLetter9.setText(this.row3Tiles.get(8).getLetter());
		this.ivRow3PlayedLetter9.setTextColor(getPlayedTileTextColor(this.row3Tiles.get(8).isDefault()));
		this.ivRow3PlayedLetter10.setText(this.row3Tiles.get(9).getLetter());
		this.ivRow3PlayedLetter10.setTextColor(getPlayedTileTextColor(this.row3Tiles.get(9).isDefault()));
	}
	
	private void loadPlayedTileViews(){
	
		this.loadPlayedWord_1();
		this.loadPlayedWord_2();
		this.loadPlayedWord_3();
	}
	
	private void loadFragments(int num){
		Logger.d(TAG, "loadFragments WordLoaderService.isLoading=" + WordLoaderService.isLoading);
		this.fragments.clear();
		
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

//	@Override
//	public boolean onMenuItemClick(MenuItem arg0) {
//		// TODO Auto-generated method stub
//		return false;
//	}

	@Override
	public void onClick(View v) {
		Logger.d(TAG, "onClick called this.isPaused=" + this.isPaused); 
		if (this.isPaused){ return; }
		
		switch (v.getId()){
			case R.id.options:
			//	if (!this.game.isActive()){
					popupMenu.show();
			//	}
				break;
		//	case R.id.ivStart:
		//		this.handleStartOnClick();
		//		break;
			case R.id.ivRow1PlayedLetter1:
			case R.id.ivRow1PlayedLetter2:
			case R.id.ivRow1PlayedLetter3:
			case R.id.ivRow1PlayedLetter4:
			case R.id.ivRow1PlayedLetter5:
			case R.id.ivRow1PlayedLetter6:
			case R.id.ivRow1PlayedLetter7:
			case R.id.ivRow1PlayedLetter8:
			case R.id.ivRow1PlayedLetter9:
			case R.id.ivRow1PlayedLetter10:
			case R.id.ivRow2PlayedLetter1:
			case R.id.ivRow2PlayedLetter2:
			case R.id.ivRow2PlayedLetter3:
			case R.id.ivRow2PlayedLetter4:
			case R.id.ivRow2PlayedLetter5:
			case R.id.ivRow2PlayedLetter6:
			case R.id.ivRow2PlayedLetter7:
			case R.id.ivRow2PlayedLetter8:
			case R.id.ivRow2PlayedLetter9:
			case R.id.ivRow2PlayedLetter10:
			case R.id.ivRow3PlayedLetter1:
			case R.id.ivRow3PlayedLetter2:
			case R.id.ivRow3PlayedLetter3:
			case R.id.ivRow3PlayedLetter4:
			case R.id.ivRow3PlayedLetter5:
			case R.id.ivRow3PlayedLetter6:
			case R.id.ivRow3PlayedLetter7:
			case R.id.ivRow3PlayedLetter8:
			case R.id.ivRow3PlayedLetter9:
			case R.id.ivRow3PlayedLetter10:
				this.handlePlayedTileClick(v.getId());
				break;
			case R.id.bPlay1:
			case R.id.bPlay2:
			case R.id.bPlay3:
				this.handleButtonClick(v.getId());
				break;
		}		
	}

	public void handleStartOnClick(){
		 //make countdown timer a variable so that it can be killed in stop/pause
		   //change game status to started (5)
		   //ApplicationContext.captureTime(TAG, "handleStartOnClick called");
    		
	   //		ApplicationContext.captureTime(TAG, "startGame about to be called");
		   
		   
		//   ApplicationContext.captureTime(TAG, "startGame completed");

		    
		    
		  // ApplicationContext.captureTime(TAG, "loadbuttons completed");
			
		   //might need a start delay here at some point
	/*	   int timerLength = this.getResources().getInteger(R.integer.gameTypeDashCountdownStart); //2 minutes
		   if (this.game.isDoubleTimeType()){
			   timerLength = this.getResources().getInteger(R.integer.gameTypeDoubleTimeCountdownStart);
		   }
		   else if(this.game.isSpeedRoundType()){
			   timerLength = this.getResources().getInteger(R.integer.gameTypeSpeedRoundsCountdownStart);
		   }
		*/   
		   //ready set go!!
		   
		    this.onStartReady();
		   
		   
		 //  this.setCountdown(this.getTimerStart());
		   
		   ApplicationContext.captureTime(TAG, "handleStartOnClick completed");
				
	   }
	   private void onStartReady(){
		   new CustomToast(this, this.getString(R.string.game_surface_start_game_ready), 800, Constants.RETURN_CODE_CUSTOM_TOAST_READY_FINISHED).show();
	   }
	   private void onStartSet(){
		   new CustomToast(this, this.getString(R.string.game_surface_start_game_set), 800, Constants.RETURN_CODE_CUSTOM_TOAST_SET_FINISHED).show();
	   }
	   private void onStartGo(){
		   new CustomToast(this, this.getString(R.string.game_surface_start_game_go), 800, Constants.RETURN_CODE_CUSTOM_TOAST_GO_FINISHED).show();
	   }
	   private void onStartGoFinished(){
		   GameService.startGame(this.game);
		   this.setBottom();
		   this.setCountdown(this.getTimerStart());
	   }
 
	private void handleButtonClick(int id){

		switch (id){
			case R.id.bPlay1:
				this.handlePlay(1, this.row1Tiles);
				break;
			case R.id.bPlay2:
				this.handlePlay(2, this.row2Tiles);
				break;
			case R.id.bPlay3:
				this.handlePlay(3, this.row3Tiles);
				break;
		}
	}
	
	private void handlePlay(int index, List<PlayedTile> playedTiles){
		//<string name="game_surface_race_word_too_short">words must be at least 3 letters long</string>
		// <string name="game_surface_race_word_opponent_played">%1$s has already played %2$s</string>	
		// <string name="game_surface_race_word_already_played">you have already played %2$s</string> 
		// <string name="game_surface_race_word_invalid">%1$s has already played %2$s</string>
		if (this.freezeAction){ return; }
		
		String word = "";
 
		for (PlayedTile tile : playedTiles){
			word += tile.getLetter().equals("") ? " " : tile.getLetter();
		}
		word = com.riotapps.wordbase.utils.Utils.trimEnd(word);  
		
		if( word.indexOf(" ") >= 0){
			//no spaces allowed
			new CustomToast(this, this.getString(R.string.game_surface_played_word_contains_spaces)).show();
			return;
		}
		
		if (word.length() < this.getResources().getInteger(R.integer.minWordLength)){
			new CustomToast(this, this.getString(R.string.game_surface_played_word_too_short)).show();
			return;
	 	}
		
		for (PlayedWord playedWord : this.game.getPlayedWords()){
			if (word.equals(playedWord.getWord())){
				new CustomToast(this, String.format(this.getString(R.string.game_surface_played_word_already_played), word)).show();
				return;
		 	}
		}
		 
		//validate word with word service
        WordService wordService = new WordService(this);
    	boolean doesWordExist = wordService.doesWordExist(word);
        wordService.finish();
        wordService = null;
            
		if (!doesWordExist){
			new CustomToast(this, String.format(this.getString(R.string.game_surface_played_word_invalid), word)).show(); 
			return;
	 	}
 		int points = this.calculatePoints(playedTiles);
		
//		this.handleRecall();
//		this.wordsPlayedByPlayer.add(word);
		//new CustomToast(this, word).show();
		
		PlayedWord playedWord = new PlayedWord();
		playedWord.setPlayedDate(new Date());
		playedWord.setOpponentPlay(false);
 		playedWord.setPointsScored(points);
		playedWord.setWord(word);
		
		//scroll.fullScroll(View.FOCUS_DOWN) also should work.
		this.game.getPlayedWords().add(playedWord);
		this.game.setScore(this.game.getScore() + points);
		this.playedWords.add(playedWord);
		//this.llPlayerWords.addView(this.getPlayedWordView(playedWord));
 		this.playedWordAdapter.notifyDataSetChanged();
 		this.lvPlayedWords.setSelection(this.playedWordAdapter.getCount() - 1);
 
//		this.game.getPlayedWords().add(playedWord);
//		this.game.setPlayerScore(this.game.getPlayerScore() + points);
		
//		this.tvPlayerScore.setText(String.valueOf(this.game.getPlayerScore()));
//		this.game.incrementNumWordsPlayedThisRound();
	 	
//		this.tvWordsLeft.setText(String.format(this.getString(com.riotapps.wordrace.R.string.scoreboard_words_left), game.getNumPossibleWords() - game.getNumWordsPlayedThisRound()));
 
		
		switch (index){
			case 1:
				this.initializePlayedList_1();
				this.loadPlayedWord_1();
				break;
			case 2:
				this.initializePlayedList_2();
				this.loadPlayedWord_2();
				break;
			case 3:
				this.initializePlayedList_3();
				this.loadPlayedWord_3();
				break;
		}
		
		this.resetPoints();
		//save game upon completion, not here
	}
	
	private void resetPoints(){
		this.tvScore.setText(String.valueOf(this.game.getScore()));
	}
	
	
	private int calculatePoints(List<PlayedTile> playedTiles){
		int points = 0;
		/*for (PlayedTile tile : playedTiles){
			if (!tile.getLetter().equals("")){
				points += 1;
				if (tile.isBonus()){
					points += 10;
				}
			}
		}*/
		
		int blankTiles = 0;
		boolean stopCountingBlanks = false;
				
		
		//tiles 1,2,3 worth 1 point each
		//tiles 4,5,6 worth 3 point each
		//tiles 7,8,9 worth 5 points each
		//tile 10 worth 10 points
		//bonus worth 10 points
		for (int i = 0; i < playedTiles.size(); i++){
			PlayedTile tile = playedTiles.get(i);
			if (tile.isDefault()){
				stopCountingBlanks = true;
			}
			else {
				if (!stopCountingBlanks){
					blankTiles += valueOfBlankTilesBeforeFirstDefault;
				}
			}
				
			if (!tile.getLetter().equals("")){
				if (i < 3){
					points += 1;
				}
				else if (i > 2 && i < 6){
					points += 3;
				}
				else if (i > 5 && i < 9){
					points += 5;
				}
				else {
					points += 10;
				}
				if (tile.isBonus()){
					points += 10;
				}
			}
		}
		//add 2 points for each blank tile before the starter tile		
		points += blankTiles;
		return points;
	}
	
	private void handlePlayedTileClick(int id){
	
		switch (id){
			case R.id.ivRow1PlayedLetter1:
				this.returnPlayedLetterToHopper(this.ivRow1PlayedLetter1, this.row1Tiles.get(0));
				break;
			case R.id.ivRow1PlayedLetter2:
				this.returnPlayedLetterToHopper(this.ivRow1PlayedLetter2, this.row1Tiles.get(1));
				break;
			case R.id.ivRow1PlayedLetter3:
				this.returnPlayedLetterToHopper(this.ivRow1PlayedLetter3, this.row1Tiles.get(2));
				break;
			case R.id.ivRow1PlayedLetter4:
				this.returnPlayedLetterToHopper(this.ivRow1PlayedLetter4, this.row1Tiles.get(3));
				break;
			case R.id.ivRow1PlayedLetter5:
				this.returnPlayedLetterToHopper(this.ivRow1PlayedLetter5, this.row1Tiles.get(4));
				break;
			case R.id.ivRow1PlayedLetter6:
				this.returnPlayedLetterToHopper(this.ivRow1PlayedLetter6, this.row1Tiles.get(5));
				break;
			case R.id.ivRow1PlayedLetter7:
				this.returnPlayedLetterToHopper(this.ivRow1PlayedLetter7, this.row1Tiles.get(6));
				break;
			case R.id.ivRow1PlayedLetter8:
				this.returnPlayedLetterToHopper(this.ivRow1PlayedLetter8, this.row1Tiles.get(7));
				break;
			case R.id.ivRow1PlayedLetter9:
				this.returnPlayedLetterToHopper(this.ivRow1PlayedLetter9, this.row1Tiles.get(8));
				break;
			case R.id.ivRow1PlayedLetter10:
				this.returnPlayedLetterToHopper(this.ivRow1PlayedLetter10, this.row1Tiles.get(9));
				break;
			case R.id.ivRow2PlayedLetter1:
				this.returnPlayedLetterToHopper(this.ivRow2PlayedLetter1, this.row2Tiles.get(0));
				break;
			case R.id.ivRow2PlayedLetter2:
				this.returnPlayedLetterToHopper(this.ivRow2PlayedLetter2, this.row2Tiles.get(1));
				break;
			case R.id.ivRow2PlayedLetter3:
				this.returnPlayedLetterToHopper(this.ivRow2PlayedLetter3, this.row2Tiles.get(2));
				break;
			case R.id.ivRow2PlayedLetter4:
				this.returnPlayedLetterToHopper(this.ivRow2PlayedLetter4, this.row2Tiles.get(3));
				break;
			case R.id.ivRow2PlayedLetter5:
				this.returnPlayedLetterToHopper(this.ivRow2PlayedLetter5, this.row2Tiles.get(4));
				break;
			case R.id.ivRow2PlayedLetter6:
				this.returnPlayedLetterToHopper(this.ivRow2PlayedLetter6, this.row2Tiles.get(5));
				break;
			case R.id.ivRow2PlayedLetter7:
				this.returnPlayedLetterToHopper(this.ivRow2PlayedLetter7, this.row2Tiles.get(6));
				break;
			case R.id.ivRow2PlayedLetter8:
				this.returnPlayedLetterToHopper(this.ivRow2PlayedLetter8, this.row2Tiles.get(7));
				break;
			case R.id.ivRow2PlayedLetter9:
				this.returnPlayedLetterToHopper(this.ivRow2PlayedLetter9, this.row2Tiles.get(8));
				break;
			case R.id.ivRow2PlayedLetter10:
				this.returnPlayedLetterToHopper(this.ivRow2PlayedLetter10, this.row2Tiles.get(9));
				break;
			case R.id.ivRow3PlayedLetter1:
				this.returnPlayedLetterToHopper(this.ivRow3PlayedLetter1, this.row3Tiles.get(0));
				break;
			case R.id.ivRow3PlayedLetter2:
				this.returnPlayedLetterToHopper(this.ivRow3PlayedLetter2, this.row3Tiles.get(1));
				break;
			case R.id.ivRow3PlayedLetter3:
				this.returnPlayedLetterToHopper(this.ivRow3PlayedLetter3, this.row3Tiles.get(2));
				break;
			case R.id.ivRow3PlayedLetter4:
				this.returnPlayedLetterToHopper(this.ivRow3PlayedLetter4, this.row3Tiles.get(3));
				break;
			case R.id.ivRow3PlayedLetter5:
				this.returnPlayedLetterToHopper(this.ivRow3PlayedLetter5, this.row3Tiles.get(4));
				break;
			case R.id.ivRow3PlayedLetter6:
				this.returnPlayedLetterToHopper(this.ivRow3PlayedLetter6, this.row3Tiles.get(5));
				break;
			case R.id.ivRow3PlayedLetter7:
				this.returnPlayedLetterToHopper(this.ivRow3PlayedLetter7, this.row3Tiles.get(6));
				break;
			case R.id.ivRow3PlayedLetter8:
				this.returnPlayedLetterToHopper(this.ivRow3PlayedLetter8, this.row3Tiles.get(7));
				break;
			case R.id.ivRow3PlayedLetter9:
				this.returnPlayedLetterToHopper(this.ivRow3PlayedLetter9, this.row3Tiles.get(8));
				break;
			case R.id.ivRow3PlayedLetter10:
				this.returnPlayedLetterToHopper(this.ivRow3PlayedLetter10, this.row3Tiles.get(9));
				break;
		}
	}
	
	private void initializeWordList(){
		this.playedWords.clear();
	 	this.playedWordAdapter = new playedWordAdapter(this, this.playedWords);
		this.lvPlayedWords.setAdapter(this.playedWordAdapter); 
		
 
		if (this.game.getPlayedWords().size() > 0 && (this.playedWords.size() == 0) ){
			//game is completed or underway, load up lists
			for (PlayedWord word : this.game.getPlayedWords()){
				this.playedWords.add(word); 
			}
			
			this.playedWordAdapter.notifyDataSetChanged();
		
		}
	}
	
	private void returnPlayedLetterToHopper(TextView tv, PlayedTile playedTile){
		if (!playedTile.isDefault() && !playedTile.getLetter().equals("")){
			tv.setText("");
			playedTile.setLetter("");
			playedTile.setBonus(false);
			
			//find hopperTile by id
			for (Tile tile : this.game.getRow1Tiles()){
				if (tile.getId() == playedTile.getTileId()){
					tile.setPlayed(false);
					
					return;
				}
			}
			for (Tile tile : this.game.getRow2Tiles()){
				if (tile.getId() == playedTile.getTileId()){
					tile.setPlayed(false);
					 
					return;
				}
			}
			for (Tile tile : this.game.getRow3Tiles()){
				if (tile.getId() == playedTile.getTileId()){
					tile.setPlayed(false);
				 
					return;
				}
			}
		}
	}
	
	private void setBottom(){
		if (!this.game.isStarted()){
			this.llAdWrapper.setVisibility(View.VISIBLE);
			this.llButtons.setVisibility(View.GONE);
			AdView adView = (AdView)this.findViewById(R.id.adView);
	    	if (StoreService.isHideBannerAdsPurchased(this)){	
				adView.setVisibility(View.GONE);
			}
	    	else {
	    		adView.loadAd(new AdRequest());
	    	}
	    	this.setBottomHeight(this.llAdWrapper);
		}
		else {
			this.llAdWrapper.setVisibility(View.GONE);
			this.llButtons.setVisibility(View.VISIBLE);
	    	this.setBottomHeight(this.llButtons);
		}
	}
	
	private void setBottomHeight(LinearLayout layout){
		if (this.bottomHeight == 0) {
    		LayoutParams params = layout.getLayoutParams();
    	
    		this.bottomHeight = params.height;  //Utils.convertPixelsToDensityPixels(this, params.height);
    		Logger.d(TAG, "llAdWrapper.getHeight()=" + Utils.convertPixelsToDensityPixels(this, params.height));
    	}
	}
	
	
	private void loadViews(){
 
		this.lvPlayedWords = (ListView) findViewById(R.id.lvPlayedWords);
		this.llAdWrapper = (LinearLayout)this.findViewById(R.id.llAdWrapper);
		this.llButtons = (LinearLayout)this.findViewById(R.id.llButtons);
		//this.llStart = (LinearLayout)this.findViewById(R.id.llStart);
		
		//this.ivStart = (ImageView) findViewById(R.id.ivStart);
		this.llPlayedWords = (LinearLayout) findViewById(R.id.llPlayedWords);
		
		this.ivRow1PlayedLetter1 = (TextView) findViewById(R.id.ivRow1PlayedLetter1);
		this.ivRow1PlayedLetter2 = (TextView) findViewById(R.id.ivRow1PlayedLetter2);
		this.ivRow1PlayedLetter3 = (TextView) findViewById(R.id.ivRow1PlayedLetter3);
		this.ivRow1PlayedLetter4 = (TextView) findViewById(R.id.ivRow1PlayedLetter4);
		this.ivRow1PlayedLetter5 = (TextView) findViewById(R.id.ivRow1PlayedLetter5);
		this.ivRow1PlayedLetter6 = (TextView) findViewById(R.id.ivRow1PlayedLetter6);
		this.ivRow1PlayedLetter7 = (TextView) findViewById(R.id.ivRow1PlayedLetter7);
		this.ivRow1PlayedLetter8 = (TextView) findViewById(R.id.ivRow1PlayedLetter8);
		this.ivRow1PlayedLetter9 = (TextView) findViewById(R.id.ivRow1PlayedLetter9);
		this.ivRow1PlayedLetter10 = (TextView) findViewById(R.id.ivRow1PlayedLetter10);
		 
		this.ivRow2PlayedLetter1 = (TextView) findViewById(R.id.ivRow2PlayedLetter1);
		this.ivRow2PlayedLetter2 = (TextView) findViewById(R.id.ivRow2PlayedLetter2);
		this.ivRow2PlayedLetter3 = (TextView) findViewById(R.id.ivRow2PlayedLetter3);
		this.ivRow2PlayedLetter4 = (TextView) findViewById(R.id.ivRow2PlayedLetter4);
		this.ivRow2PlayedLetter5 = (TextView) findViewById(R.id.ivRow2PlayedLetter5);
		this.ivRow2PlayedLetter6 = (TextView) findViewById(R.id.ivRow2PlayedLetter6);
		this.ivRow2PlayedLetter7 = (TextView) findViewById(R.id.ivRow2PlayedLetter7);
		this.ivRow2PlayedLetter8 = (TextView) findViewById(R.id.ivRow2PlayedLetter8);
		this.ivRow2PlayedLetter9 = (TextView) findViewById(R.id.ivRow2PlayedLetter9);
		this.ivRow2PlayedLetter10 = (TextView) findViewById(R.id.ivRow2PlayedLetter10);
		
		this.ivRow3PlayedLetter1 = (TextView) findViewById(R.id.ivRow3PlayedLetter1);
		this.ivRow3PlayedLetter2 = (TextView) findViewById(R.id.ivRow3PlayedLetter2);
		this.ivRow3PlayedLetter3 = (TextView) findViewById(R.id.ivRow3PlayedLetter3);
		this.ivRow3PlayedLetter4 = (TextView) findViewById(R.id.ivRow3PlayedLetter4);
		this.ivRow3PlayedLetter5 = (TextView) findViewById(R.id.ivRow3PlayedLetter5);
		this.ivRow3PlayedLetter6 = (TextView) findViewById(R.id.ivRow3PlayedLetter6);
		this.ivRow3PlayedLetter7 = (TextView) findViewById(R.id.ivRow3PlayedLetter7);
		this.ivRow3PlayedLetter8 = (TextView) findViewById(R.id.ivRow3PlayedLetter8);
		this.ivRow3PlayedLetter9 = (TextView) findViewById(R.id.ivRow3PlayedLetter9);
		this.ivRow3PlayedLetter10 = (TextView) findViewById(R.id.ivRow3PlayedLetter10);

		this.tvPlayedWord1Title = (TextView) findViewById(R.id.tvPlayedWord1Title); 
		this.tvPlayedWord2Title = (TextView) findViewById(R.id.tvPlayedWord2Title); 
		this.tvPlayedWord3Title = (TextView) findViewById(R.id.tvPlayedWord3Title); 
		
		this.bPlay1 = (Button) findViewById(R.id.bPlay1);
		this.bPlay2 = (Button) findViewById(R.id.bPlay2);
		this.bPlay3 = (Button) findViewById(R.id.bPlay3);
		
	//	this.ivStart.setOnClickListener(this);
		
		this.ivRow1PlayedLetter1.setOnClickListener(this);
		this.ivRow1PlayedLetter2.setOnClickListener(this);
		this.ivRow1PlayedLetter3.setOnClickListener(this);
		this.ivRow1PlayedLetter4.setOnClickListener(this);
		this.ivRow1PlayedLetter5.setOnClickListener(this);
		this.ivRow1PlayedLetter6.setOnClickListener(this);
		this.ivRow1PlayedLetter7.setOnClickListener(this);
		this.ivRow1PlayedLetter8.setOnClickListener(this);
		this.ivRow1PlayedLetter9.setOnClickListener(this);
		this.ivRow1PlayedLetter10.setOnClickListener(this);
		 
		this.ivRow2PlayedLetter1.setOnClickListener(this);
		this.ivRow2PlayedLetter2.setOnClickListener(this);
		this.ivRow2PlayedLetter3.setOnClickListener(this);
		this.ivRow2PlayedLetter4.setOnClickListener(this);
		this.ivRow2PlayedLetter5.setOnClickListener(this);
		this.ivRow2PlayedLetter6.setOnClickListener(this);
		this.ivRow2PlayedLetter7.setOnClickListener(this);
		this.ivRow2PlayedLetter8.setOnClickListener(this);
		this.ivRow2PlayedLetter9.setOnClickListener(this);
		this.ivRow2PlayedLetter10.setOnClickListener(this);
		
		this.ivRow3PlayedLetter1.setOnClickListener(this);
		this.ivRow3PlayedLetter2.setOnClickListener(this);
		this.ivRow3PlayedLetter3.setOnClickListener(this);
		this.ivRow3PlayedLetter4.setOnClickListener(this);
		this.ivRow3PlayedLetter5.setOnClickListener(this);
		this.ivRow3PlayedLetter6.setOnClickListener(this);
		this.ivRow3PlayedLetter7.setOnClickListener(this);
		this.ivRow3PlayedLetter8.setOnClickListener(this);
		this.ivRow3PlayedLetter9.setOnClickListener(this);
		this.ivRow3PlayedLetter10.setOnClickListener(this);
		
		this.bPlay1.setOnClickListener(this);
		this.bPlay2.setOnClickListener(this);		
		this.bPlay3.setOnClickListener(this);
		
		this.tvCountdown = (TextView) findViewById(R.id.tvCountdown);
		this.tvScore = (TextView) findViewById(R.id.tvScore); 
		this.tvTopScore = (TextView) findViewById(R.id.tvTopScore); 
		
 
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
			 GameSurface.bgTray = ImageHelper.decodeSampledBitmapFromResource(this.getResources(), R.drawable.tray_letter_bg2, this.playedLetterTileSize, this.playedLetterTileSize);
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
		if (tile.isPlayed()) { return false; }
		if (tile.getRow() == 1){ 
			boolean isFull = true;
			for (PlayedTile t : this.row1Tiles){
				if (t.getLetter().equals("")){
					isFull = false;
				}
			}				
			//10 is the max size
			if (isFull) {return false;}
		}
		else if (tile.getRow() == 2){ 
			boolean isFull = true;
			for (PlayedTile t : this.row2Tiles){
				if (t.getLetter().equals("")){
					isFull = false;
				}
			}				
			//10 is the max size
			if (isFull) {return false;}
		}
		else if (tile.getRow() == 3){ 
			boolean isFull = true;
			for (PlayedTile t : this.row3Tiles){
				if (t.getLetter().equals("")){
					isFull = false;
				}
			}				
			//10 is the max size
			if (isFull) {return false;}
		}
		
		if (tile.getRow() == 1){  
			for (PlayedTile t : this.row1Tiles){
				if (t.getLetter().equals("") && !t.isDefault()){
					t.setLetter(tile.getLetter());
					t.setTileId(tile.getId());
					t.setBonus(tile.isBonus());
					this.loadPlayedWord_1();
					
					break;
				}
			}				
		}
		else if (tile.getRow() == 2){ 
			for (PlayedTile t : this.row2Tiles){
				if (t.getLetter().equals("") && !t.isDefault()){
					t.setLetter(tile.getLetter());
					t.setTileId(tile.getId());
					t.setBonus(tile.isBonus());
					this.loadPlayedWord_2();
					break;
				}
			} 
		}
		else if (tile.getRow() == 3){  
			for (PlayedTile t : this.row3Tiles){
				if (t.getLetter().equals("") && !t.isDefault()){
					t.setLetter(tile.getLetter());
					t.setTileId(tile.getId());
					t.setBonus(tile.isBonus());
					this.loadPlayedWord_3();
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
		
		this.tvPlayedWord1Title.setTypeface(ApplicationContext.getLetterTypeface());
		this.tvPlayedWord2Title.setTypeface(ApplicationContext.getLetterTypeface());
		this.tvPlayedWord3Title.setTypeface(ApplicationContext.getLetterTypeface());
		
		this.bPlay1.setTypeface(ApplicationContext.getScoreboardButtonFontTypeface());
		this.bPlay2.setTypeface(ApplicationContext.getScoreboardButtonFontTypeface());
		this.bPlay3.setTypeface(ApplicationContext.getScoreboardButtonFontTypeface());
		 
	}
	
	
	   private int getTimerStart(){
		   int timerLength = this.getResources().getInteger(R.integer.defaultCountdownStart); //5 minutes
		/*   if (this.game.isDoubleTimeType()){
			   timerLength = this.getResources().getInteger(R.integer.gameTypeDoubleTimeCountdownStart);
		   }
		   else if(this.game.isSpeedRoundType()){
			   timerLength = this.getResources().getInteger(R.integer.gameTypeSpeedRoundsCountdownStart);
		   }
		   */
		   return timerLength;
	   }
	   
	   private void setCountdown(long startingPointInMilliseconds){
		   Logger.d(TAG, "setCountdown startingPointInMilliseconds=" + startingPointInMilliseconds);
		   
		   this.countdown = new CountDownTimer(startingPointInMilliseconds, 10) {

			   
		     public void onTick(long millisUntilFinished) {
		    	 if (millisUntilFinished%10 == 0) {
			    	 int secondsUntilFinished = (int) millisUntilFinished / 1000;
			    	 int minutesLeft = (int) secondsUntilFinished / 60;
			    	 int secondsLeft = secondsUntilFinished - (minutesLeft * 60);
			    
			    	 GameSurface.this.game.setCountdown(millisUntilFinished);
			    	 
			    	 if (minutesLeft == 0 && secondsLeft < 11){
			    		 GameSurface.this.tvCountdown.setTextColor(Color.parseColor(GameSurface.this.getString(R.color.countdown_finish_text_color)));
			    	 }
			    	
			    	 GameSurface.this.tvCountdown.setText(String.format(GameSurface.this.getString(R.string.scoreboard_countdown), minutesLeft, String.format("%02d", secondsLeft)));
			    	 
		    	 }
		    	 else if(millisUntilFinished == 10){
		    		 GameSurface.this.tvCountdown.setText(GameSurface.this.getString(R.string.scoreboard_countdown_completed));
		    		 GameSurface.this.tvCountdown.setTextColor(Color.parseColor(GameSurface.this.getString(R.color.countdown_text_color)));
		    	 }
		    	 
		     }

		     
		     public void onFinish() {
		    	  Logger.d(TAG, "setCountdown onFinish");
		    	
				   
		        // mTextField.setText("done!");
		    	 //GameSurface.this.restartCountdownView();
		    	 //GameSurface.this.tvCountdown.setText(GameSurface.this.getString(R.string.scoreboard_countdown_completed));
	    		 //GameSurface.this.tvCountdown.setTextColor(Color.parseColor(GameSurface.this.getString(R.color.countdown_text_color)));
		    	 
		    //	 if (GameSurface.this.game.isSpeedRoundType()){
		    //		GameSurface.this.handleRound(GameSurface.this.game.getRound() + 1); 
		    //	 }
		    //	 else{
		    		 GameSurface.this.handleCompletion();
		    //	 }
		     }
		  };
		  
		  
		  this.countdown.start();
		  this.freezeAction = false;
		  this.isCountdownRunning = true;
	   }
	
	   private void handleCompletion(){
		   this.isCountdownRunning = false;
	    	 this.freezeAction = true;
	    	  
	    	  this.game.setCountdown(0);
		   this.gameSurfaceView.setReadyToDraw(false);
		   //this.restartCountdownView();
		    Logger.d(TAG, "handleCompletion this.tvCountdown=" + this.tvCountdown.getText());

		    //this.isCompletedThisSession = true;
		    GameService.completeGame(this, this.player, this.game);
		   this.setCompletedState();
		   
		   this.row1Tiles.clear();
		   this.row2Tiles.clear();
		   this.row3Tiles.clear();
		   this.playedWords.clear();
		   
		   try {
			this.game = GameService.createGame(this, player);
		} catch (DesignByContractException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 
			this.initializeGameOnBoard();
		   
		   //handle Ad or purchase reminder
		// come back to this   this.handleInterstitialAd();
		    
		   
	   }
	   
	   private void setCompletedState(){
			if (this.game.isCompleted()){
				/*
				this.tvShortInfo.setVisibility(View.VISIBLE);
				 this.tvShortInfo.setText(this.getString(R.string.game_surface_game_over));
				 this.tvNumPoints.setVisibility(View.INVISIBLE);
	 		 
				 Button bRematch = (Button) this.findViewById(R.id.bRematch);
				 bRematch.setVisibility(View.VISIBLE);
				 bRematch.setText(String.format(this.getString(R.string.game_surface_rematch), this.game.getOpponent().getName()));
				 bRematch.setClickable(true);
				 bRematch.setOnClickListener(this);
				 this.tvCountdown.setVisibility(View.GONE);
		 	 
				 
				// GameSurface.this.runOnUiThread(new handleCountdownViewState(0));
				 LinearLayout llPlayedWord = (LinearLayout) this.findViewById(R.id.llPlayedWord);
				 RelativeLayout llGameComplete = (RelativeLayout) this.findViewById(R.id.llGameComplete);
				 llPlayedWord.setVisibility(View.GONE);
				 llGameComplete.setVisibility(View.VISIBLE);

				this.llButtons.setVisibility(View.GONE);
				this.llAdWrapper.setVisibility(View.VISIBLE);
				
				AdView adView = (AdView)this.findViewById(R.id.adView);
				View vBottomFill = (View)this.findViewById(R.id.vBottomFill);
		 
		    	if (StoreService.isHideBannerAdsPurchased(this)){	
					adView.setVisibility(View.GONE);
					vBottomFill.setVisibility(View.VISIBLE);
				}
		    	else {
		    		NetworkConnectivity connection = new NetworkConnectivity(this);
		    		boolean isConnected = connection.checkNetworkConnectivity();
		    		
		    		if (isConnected){
		    			vBottomFill.setVisibility(View.GONE);
		    			adView.loadAd(new AdRequest());
		    			
		    		}
		    		else{
		    			adView.setVisibility(View.GONE);
		    			vBottomFill.setVisibility(View.VISIBLE);
		    		}
		    	}
				
		    	this.initializeHopper();
				this.initializeTray();
				this.setListenerOnLists();
				*/
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
	
	
	
	private class playedWordAdapter extends ArrayAdapter<PlayedWord> {
	   	  private final GameSurface context;
	   	  private List<PlayedWord>  values;
	   	  private  int wordCount;
	   	  LayoutInflater inflater;
	
	   	  public playedWordAdapter(GameSurface context, List<PlayedWord> values) {
		  	super(context, R.layout.wordlistitem, values); 
		    this.context = context;
		    this.values = values;
		    this.wordCount = values.size();
		    
		    this.inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		  }

   	 
	  @Override
	  public View getView(int position, View rowView, ViewGroup parent) {
		 
		  if ( rowView == null ) {
			  rowView = inflater.inflate(R.layout.wordlistitem, parent, false);
		  }
		  
    	   PlayedWord word = values.get(position);
    	 
    	   TextView tvWord = (TextView) rowView.findViewById(R.id.tvWord);
    	   TextView tvPoints = (TextView) rowView.findViewById(R.id.tvPoints);

    	   
    	   tvWord.setTypeface(ApplicationContext.getScoreboardFontTypeface());
    	   tvPoints.setTypeface(ApplicationContext.getScoreboardFontTypeface());

    	   tvWord.setText(word.getWord());
    	   tvPoints.setText(String.valueOf(word.getPointsScored()));
	     	   
    	   rowView.setTag(word.getWord());
    	   return rowView;
	  }
	
	} 	
	private void setupChartBoost(){
		this.cb = Chartboost.sharedChartboost();
		this.cb.onCreate(this, this.getString(R.string.chartboost_app_id), this.getString(R.string.chartboost_app_signature), this.chartBoostDelegate);
		this.cb.setImpressionsUseActivities(true);
		this.cb.startSession();
		this.cb.cacheInterstitial();

	}
	private ChartboostDelegate chartBoostDelegate = new ChartboostDelegate() { 

		/*
		 * Chartboost delegate methods
		 * 
		 * Implement the delegate methods below to finely control Chartboost's behavior in your app
		 * 
		 * Minimum recommended: shouldDisplayInterstitial()
		 */


		/* 
		 * shouldDisplayInterstitial(String location)
		 *
		 * This is used to control when an interstitial should or should not be displayed
		 * If you should not display an interstitial, return false
		 *
		 * For example: during gameplay, return false.
		 *
		 * Is fired on:
		 * - showInterstitial()
		 * - Interstitial is loaded & ready to display
		 */
		@Override
		public boolean shouldDisplayInterstitial(String location) {
			Logger.d(TAG, "SHOULD DISPLAY INTERSTITIAL '"+location+ "'?");
			return true;
		}

		/*
		 * shouldRequestInterstitial(String location)
		 * 
		 * This is used to control when an interstitial should or should not be requested
		 * If you should not request an interstitial from the server, return false
		 *
		 * For example: user should not see interstitials for some reason, return false.
		 *
		 * Is fired on:
		 * - cacheInterstitial()
		 * - showInterstitial() if no interstitial is cached
		 * 
		 * Notes: 
		 * - We do not recommend excluding purchasers with this delegate method
		 * - Instead, use an exclusion list on your campaign so you can control it on the fly
		 */
		@Override
		public boolean shouldRequestInterstitial(String location) {
			Logger.d(TAG, "SHOULD REQUEST INSTERSTITIAL '"+location+ "'?");
			return true;
		}

		/*
		 * didCacheInterstitial(String location)
		 * 
		 * Passes in the location name that has successfully been cached
		 * 
		 * Is fired on:
		 * - cacheInterstitial() success
		 * - All assets are loaded
		 * 
		 * Notes:
		 * - Similar to this is: cb.hasCachedInterstitial(String location) 
		 * Which will return true if a cached interstitial exists for that location
		 */
		@Override
		public void didCacheInterstitial(String location) {
			Logger.d(TAG, "INTERSTITIAL '"+location+"' CACHED");
		}

		/*
		 * didFailToLoadInterstitial(String location)
		 * 
		 * This is called when an interstitial has failed to load for any reason
		 * 
		 * Is fired on:
		 * - cacheInterstitial() failure
		 * - showInterstitial() failure if no interstitial was cached
		 * 
		 * Possible reasons:
		 * - No network connection
		 * - No publishing campaign matches for this user (go make a new one in the dashboard)
		 */
		@Override
		public void didFailToLoadInterstitial(String location) {
		    // Show a house ad or do something else when a chartboost interstitial fails to load

			Logger.d(TAG, "ChartBoost INTERSTITIAL '"+location+"' REQUEST FAILED");
			//Toast.makeText(context, "Interstitial '"+location+"' Load Failed",
			//		Toast.LENGTH_SHORT).show();
			handlePostAdServer();
			//handlePostTurnFinalAction(postTurnAction);
		}

		/*
		 * didDismissInterstitial(String location)
		 *
		 * This is called when an interstitial is dismissed
		 *
		 * Is fired on:
		 * - Interstitial click
		 * - Interstitial close
		 *
		 * #Pro Tip: Use the delegate method below to immediately re-cache interstitials
		 */
		@Override
		public void didDismissInterstitial(String location) {

			// Immediately re-caches an interstitial
			cb.cacheInterstitial(location);
			handlePostAdServer();
			//handlePostTurnFinalAction(postTurnAction);


			Logger.d(TAG, "ChartBoost INTERSTITIAL '"+location+"' DISMISSED");
			//Toast.makeText(context, "Dismissed Interstitial '"+location+"'",
			//		Toast.LENGTH_SHORT).show();
			
		}

		/*
		 * didCloseInterstitial(String location)
		 *
		 * This is called when an interstitial is closed
		 *
		 * Is fired on:
		 * - Interstitial close
		 */
		@Override
		public void didCloseInterstitial(String location) {
			Logger.i(TAG, "ChartBoost INSTERSTITIAL '"+location+"' CLOSED");
			//handlePostAdServer();
			//handlePostTurnFinalAction(postTurnAction);
			//Toast.makeText(context, "Closed Interstitial '"+location+"'",
			//		Toast.LENGTH_SHORT).show();
		}

		/*
		 * didClickInterstitial(String location)
		 *
		 * This is called when an interstitial is clicked
		 *
		 * Is fired on:
		 * - Interstitial click
		 */
		@Override
		public void didClickInterstitial(String location) {
			Logger.i(TAG, "ChartBoost DID CLICK INTERSTITIAL '"+location+"'");
			handlePostAdServer();
			//handlePostTurnFinalAction(postTurnAction);
			//Toast.makeText(context, "Clicked Interstitial '"+location+"'",
			//		Toast.LENGTH_SHORT).show();
		}

		/*
		 * didShowInterstitial(String location)
		 *
		 * This is called when an interstitial has been successfully shown
		 *
		 * Is fired on:
		 * - showInterstitial() success
		 */
		@Override
		public void didShowInterstitial(String location) {
			Logger.i(TAG, "ChartBoost INTERSTITIAL '" + location + "' SHOWN");
			//Toast.makeText(context, "Interstitial '"+location+"' shown",
			//		Toast.LENGTH_SHORT).show();
			
			//Dont do it here because boost does not use full page interstitial and 
			//tray can be seen through modal
			//handlePostTurnFinalAction(postTurnAction);
		}

		/*
		 * More Apps delegate methods
		 */

		/*
		 * shouldDisplayLoadingViewForMoreApps()
		 *
		 * Return false to prevent the pretty More-Apps loading screen
		 *
		 * Is fired on:
		 * - showMoreApps()
		 */
		@Override
		public boolean shouldDisplayLoadingViewForMoreApps() {
			return true;
		}

		/*
		 * shouldRequestMoreApps()
		 * 
		 * Return false to prevent a More-Apps page request
		 *
		 * Is fired on:
		 * - cacheMoreApps()
		 * - showMoreApps() if no More-Apps page is cached
		 */
		@Override
		public boolean shouldRequestMoreApps() {

			return true;
		}

		/*
		 * shouldDisplayMoreApps()
		 * 
		 * Return false to prevent the More-Apps page from displaying
		 *
		 * Is fired on:
		 * - showMoreApps() 
		 * - More-Apps page is loaded & ready to display
		 */
		@Override
		public boolean shouldDisplayMoreApps() {
			Logger.i(TAG, "ChartBoost SHOULD DISPLAY MORE APPS?");
			return true;
		}

		/*
		 * didFailToLoadMoreApps()
		 * 
		 * This is called when the More-Apps page has failed to load for any reason
		 * 
		 * Is fired on:
		 * - cacheMoreApps() failure
		 * - showMoreApps() failure if no More-Apps page was cached
		 * 
		 * Possible reasons:
		 * - No network connection
		 * - No publishing campaign matches for this user (go make a new one in the dashboard)
		 */
		@Override
		public void didFailToLoadMoreApps() {
			Logger.i(TAG, "ChartBoost MORE APPS REQUEST FAILED");
			//Toast.makeText(context, "More Apps Load Failed",
			//		Toast.LENGTH_SHORT).show();
		}

		/*
		 * didCacheMoreApps()
		 * 
		 * Is fired on:
		 * - cacheMoreApps() success
		 * - All assets are loaded
		 */
		@Override
		public void didCacheMoreApps() {
			Logger.i(TAG, "ChartBoost MORE APPS CACHED");
		}

		/*
		 * didDismissMoreApps()
		 *
		 * This is called when the More-Apps page is dismissed
		 *
		 * Is fired on:
		 * - More-Apps click
		 * - More-Apps close
		 */
		@Override
		public void didDismissMoreApps() {
			Logger.i(TAG, "ChartBoost MORE APPS DISMISSED");
			//Toast.makeText(context, "Dismissed More Apps",
			//		Toast.LENGTH_SHORT).show();
		}

		/*
		 * didCloseMoreApps()
		 *
		 * This is called when the More-Apps page is closed
		 *
		 * Is fired on:
		 * - More-Apps close
		 */
		@Override
		public void didCloseMoreApps() {
			Logger.i(TAG, "ChartBoost MORE APPS CLOSED");
			//Toast.makeText(context, "Closed More Apps",
			//		Toast.LENGTH_SHORT).show();
		}

		/*
		 * didClickMoreApps()
		 *
		 * This is called when the More-Apps page is clicked
		 *
		 * Is fired on:
		 * - More-Apps click
		 */
		@Override
		public void didClickMoreApps() {
			Logger.i(TAG, "ChartBoost MORE APPS CLICKED");
			//Toast.makeText(context, "Clicked More Apps",
			//		Toast.LENGTH_SHORT).show();
		}

		/*
		 * didShowMoreApps()
		 *
		 * This is called when the More-Apps page has been successfully shown
		 *
		 * Is fired on:
		 * - showMoreApps() success
		 */
		@Override
		public void didShowMoreApps() {
			Logger.i(TAG, "ChartBoost MORE APPS SHOWED");
		}

		/*
		 * shouldRequestInterstitialsInFirstSession()
		 *
		 * Return false if the user should not request interstitials until the 2nd startSession()
		 * 
		 */
		@Override
		public boolean shouldRequestInterstitialsInFirstSession() {
			return true;
		}
	};

	
    private void trackEvent(String action, String label, int value){
    	this.trackEvent(Constants.TRACKER_CATEGORY_GAMEBOARD, action, label, (long)value);
    }
	   
	public void trackEvent(String category, String action, String label, long value){
		try{
			this.getTracker().sendEvent(category, action,label, value);
		}
		catch (Exception e){
  			Logger.d(TAG, "trackEvent category=" + (category == null ? "null" : category) + " action=" + (action == null ? "null" : action) 
  					 + " label=" + (label == null ? "null" : label)  + " value=" + value +" e=" + e.toString());
  			
		}
	}
 
}
