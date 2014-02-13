package com.rozen.wordscroll;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;
import android.view.View;
 
import android.widget.PopupMenu;

import com.riotapps.wordbase.R;
import com.rozen.wordscroll.hooks.GameService;
import com.riotapps.wordbase.hooks.Player;
import com.riotapps.wordbase.interfaces.ICloseDialog;
import com.rozen.wordscroll.hooks.Game;
import com.rozen.wordscroll.ui.GameSurfaceView;
import com.riotapps.wordbase.ui.MenuUtils;
import com.riotapps.wordbase.utils.ApplicationContext;
import com.riotapps.wordbase.utils.Constants;
import com.riotapps.wordbase.utils.DesignByContractException;
import com.riotapps.wordbase.utils.Logger;

public class GameSurface  extends FragmentActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener, ICloseDialog{
	private static final String TAG = GameSurface.class.getSimpleName();
	private GameSurfaceView gameSurfaceView;
	
	private Game game;
	private Player player;
	
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
		
		this.setGame();
	    this.gameSurfaceView = (GameSurfaceView)findViewById(R.id.gameSurface);
	    this.gameSurfaceView.construct(this);
		 
	 //	this.setupPreloadTask();
	}
	@Override
	public void dialogClose(int resultCode) {
		// TODO Auto-generated method stub
		
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
		
	/*	//ivLetter.startAnimation(animation)
		
		AnimationSet animationSet = new AnimationSet(true);
		//AnimationUtils utils = new AnimationUtils();
		long time = AnimationUtils.currentAnimationTimeMillis();
		
 	    TranslateAnimation a = new TranslateAnimation(
	            Animation.RELATIVE_TO_SELF,0, Animation.ABSOLUTE,2,
	            Animation.RELATIVE_TO_SELF,0, Animation.ABSOLUTE,0);
	    a.setDuration(10);
	    a.setStartTime(time);
	    animationSet.addAnimation(a);
	    
	    TranslateAnimation b = new TranslateAnimation(
	            Animation.RELATIVE_TO_SELF,0, Animation.ABSOLUTE,2,
	            Animation.RELATIVE_TO_SELF,0, Animation.ABSOLUTE,0);
	    b.setDuration(100);
	    b.setStartTime(time + 200);
	    animationSet.addAnimation(b);
	    
	    TranslateAnimation c = new TranslateAnimation(
	            Animation.RELATIVE_TO_SELF,0, Animation.ABSOLUTE,2,
	            Animation.RELATIVE_TO_SELF,0, Animation.ABSOLUTE,0);
	    c.setDuration(100);
	    c.setStartTime(time + 400);
	    animationSet.addAnimation(c);
	    
	    TranslateAnimation d = new TranslateAnimation(
	            Animation.RELATIVE_TO_SELF,0, Animation.ABSOLUTE,2,
	            Animation.RELATIVE_TO_SELF,0, Animation.ABSOLUTE,0);
	    d.setDuration(100);
	    d.setStartTime(time + 600);
	    d.setFillAfter(true);
	    animationSet.addAnimation(d);
 */
		/* 

  RotateAnimation r = new RotateAnimation(0f, -90f,200,200); // HERE 
	    r.setStartOffset(1000);
	    r.setDuration(1000);
	    animationSet.addAnimation(r);
   
		Animation mAnimation = new TranslateAnimation(
	            TranslateAnimation.RELATIVE_TO_SELF, 0f,
	            TranslateAnimation.ABSOLUTE, 0f,
	            TranslateAnimation.RELATIVE_TO_SELF, 0f,
	            TranslateAnimation.RELATIVE_TO_PARENT, 1.0f);
	   mAnimation.setDuration(10000);
	  // mAnimation.setFillAfter(true);
	   mAnimation.setRepeatCount(-1);
	 //  mAnimation.setRepeatMode(Animation.REVERSE);
	   mAnimation.setInterpolator(new LinearInterpolator());
	   ivLetter.setAnimation(mAnimation);
	*/ 
//     ivLetter.startAnimation(animationSet);
		
		
	}
	
	public void callback(){
		
	}

	public void captureTime(String text){
		ApplicationContext.captureTime(TAG, text);
 
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
}
