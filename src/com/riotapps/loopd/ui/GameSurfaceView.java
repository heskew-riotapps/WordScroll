package com.riotapps.loopd.ui;

import java.util.List;

import com.riotapps.loopd.GameSurface;
import com.riotapps.loopd.hooks.Tile;
import com.riotapps.loopd.R;
import com.riotapps.wordbase.ui.Coordinate;
import com.riotapps.wordbase.ui.RectArea;
import com.riotapps.wordbase.utils.ApplicationContext;
import com.riotapps.wordbase.utils.ImageHelper;
import com.riotapps.wordbase.utils.Logger;
import com.riotapps.wordbase.utils.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.PorterDuff.Mode;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup.LayoutParams;


public class GameSurfaceView extends SurfaceView  implements SurfaceHolder.Callback {

	GameSurface parent;
	private static final String TAG = GameSurfaceView.class.getSimpleName();
	
	public void setParent(GameSurface parent){
		this.parent = parent;
	}
	
//	ApplicationContext appContext;
 
	GameSurfaceView me = this;
//	Context context;
	GameThread gameThread = null;
	boolean isThreadRunning = false;
	boolean isPaused = false;
	SurfaceHolder surfaceHolder;
	private SurfaceHolder holder;
	protected int height;
	protected boolean readyToDraw;
	private int fullWidth;
	private int absoluteTop;
	private int absoluteLeft;
	private Object movementTriggerTapcheckThreshold;
	private Object fullViewTileWidth;
	private boolean surfaceCreated;
	private static Bitmap bgTile;
	private static Bitmap bgTilePlayed;
	private static Bitmap bgTileBonus;
	private static Bitmap bgBorder;
	private static Bitmap bgStart;
	private static Bitmap bgStartPressed;
	private static Bitmap bgTileStarter;

	private int loopCount = 0;
	private int speedDirection = 0;
	
	private int speedStarterDistanceMin = 4;
	private int speedStarterDistanceMax = 6;

	private int speedDistanceMin = 6;
	private int speedDistanceMax = 13;
	private int lastSpeedChange = 0;
	private int updateSpeedInterval = 30;
	private int bonusCount = 0;
	
	private int backoffDistanceFromEdge = 5;
	private int speedDistance = 6;
	private float speedDistancePercentageOfTileSize = .03125f; ///????? change to dp conversion
	private float rowHeightDivisor = 3.5f;
	private int maxTileSize = 170;
	
	private int row1Position = 0;
	private int row2Position = 0;
//	private int tileStarterMaxDistance;
//	private float tileStarterPercentage = .3f;

	private int borderVisibleWidth = 24;
	private int tileSize = 160;
	private int tileGap = 10;
	private int row1_yPosition = 10;
	private int row2_yPosition = 180;
	private int row3_yPosition = 350;
	
	private int currentX = -1;
	private int currentY = -1;
	private int previousX = -1;
	private int previousY = -1;
	private long tapCheck = 0;
	private static final long SINGLE_TAP_DURATION_IN_MILLISECONDS = 550;
	private int tileOnDown = -1;
	private Tile tappedTile = null;
	private int verticalMidPoint;
	private int maxUsableHeightPerRow;
	private int borderTotalWidth;
	private boolean isDrawing;
	private int startWidth;
	private float startButtonPercentageOfHeight = .8f;
	private int maxStartButtonSize = 200;
	private int horizontalMidPoint;
	private int startXPosition;
	private RectArea startButtonArea;
	private boolean stopStarterTile = false;
	private boolean isStartButtonPressed = false;
	
	public boolean isReadyToDraw() {
		return readyToDraw;
	}

	public void setReadyToDraw(boolean readyToDraw) {
		this.readyToDraw = readyToDraw;
	}

	private Tile getTappedTile(){
		if (this.tileOnDown >= 0) {
			for (Tile tile : this.parent.getGame().getRow3Tiles()){
				if (this.tileOnDown == tile.getId()){
					return tile;
				}
			}
			for (Tile tile : this.parent.getGame().getRow2Tiles()){
					if (this.tileOnDown == tile.getId()){
						return tile;
					}
			}
			for (Tile tile : this.parent.getGame().getRow1Tiles()){
				if (this.tileOnDown == tile.getId()){
					return tile;
				}
			}
		}
		 
		return null;
	 
	}
 

	public int getTileOnDown() {
		return tileOnDown;
	}

	public void setTileOnDown(int tileOnDown) {
		this.tileOnDown = tileOnDown;
	}

	public void resetTileOnDown() {
		this.tileOnDown = -1;
	}
	
	public GameSurfaceView(Context context) {
		super(context);
		//this.construct(context);
	 
	}

	public GameSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void construct( GameSurface parent) {
		//Logger.w(TAG, "construct called");
		this.parent = parent;
		//this.appContext = appContext;
		this.parent.captureTime(TAG + " construct starting");
		
 
		  this.setZOrderOnTop(true);
		 this.holder = getHolder();
		 this.holder.addCallback(this);
		 this.gameThread = new GameThread(holder, this);
		
		 setFocusable(true);
 
		 this.holder.setFormat(PixelFormat.TRANSPARENT);// necessary
		 
		// isTablet = getResources().getBoolean(R.bool.isTablet);
		 
  		// if (this.parent.getResources().getInteger(R.integer.derived_device_screen_size) == Constants.SCREEN_SIZE_XLARGE || 
  		//  		 this.parent.getResources().getInteger(R.integer.derived_device_screen_size) == Constants.SCREEN_SIZE_LARGE){
		//	 this.autoZoom = false;
		// }
 	 
		 this.post(new Runnable() 
		    {   
		        @Override
		        public void run() {

		    		me.parent.captureTime(TAG + " runnable starting"); 	
		   		me.SetDerivedValues();
				me.parent.captureTime(TAG + " setDerivedValues ended");
		   	   // me.LoadTiles();
		   	   // me.LoadTray();
		   	  
		   	    Logger.w(TAG, "run called");
		   	    
		   	    
		  
		   	     LayoutParams lp = me.getLayoutParams();
			 	  lp.height = me.height;
			 	  
				  me.parent.captureTime(TAG + " load game starting");
			 	  //me.loadGame();
				  me.parent.captureTime(TAG + " load game ended");
			//	  // Apply to new dimension
			 	  me.setLayoutParams( lp );
			 //	  me.setInitialRecallShuffleState();
			//	  me.resetPointsView();
			 	  me.readyToDraw = true;
		        }

		     });
		 
			this.parent.captureTime(TAG + " construct ended");
	}

	//public void loadGame(){
	//	Logger.d(TAG,"loadGame game turn=" + this.parent.getGame().getTurn());
		
	 
	  //  this.parent.callback();
	//}

	
	private void SetDerivedValues(){
		me.getLayoutParams();
 
		int[] coordinates = new int[2];
		this.getLocationOnScreen(coordinates);
		
		this.absoluteTop = coordinates[1];
		this.absoluteLeft = coordinates[0];
		
	// 	Display display = this.parent.getWindowManager().getDefaultDisplay(); 
	// 	int w = display.getWidth();  // deprecated
	// 	int h = this.getHolder().getSurfaceFrame().bottom ; //display.getHeight();  // deprecated
		 
	 	//this.getHolder().getSurfaceFrame().bottom;
	 //	Logger.d(TAG, "SetDerivedValues getSurfaceFrame().bottom=" +  this.getHolder().getSurfaceFrame().bottom );
	 //	Logger.d(TAG, "SetDerivedValues getSurfaceFrame().top=" +  this.getHolder().getSurfaceFrame().top );
	 //	Logger.d(TAG, "SetDerivedValues getSurfaceFrame().left=" +  this.getHolder().getSurfaceFrame().left );
	 //	Logger.d(TAG, "SetDerivedValues getSurfaceFrame().right=" +  this.getHolder().getSurfaceFrame().right );
	 	
	 	DisplayMetrics displaymetrics = new DisplayMetrics();
	 	this.parent.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
	    //	int w  = displaymetrics.widthPixels;
	//  	Logger.d(TAG, "SetDerivedValues gameboard_button_area_height=" +  this.parent.getResources().getInteger(com.riotapps.word.R.integer.gameboard_button_area_height) );
//	 	Logger.d(TAG, "SetDerivedValues scoreboard_height=" +  this.parent.getResources().getInteger(com.riotapps.word.R.integer.scoreboard_height) );
//	  	Logger.d(TAG, "SetDerivedValues h1=" +  this.getHeight() );
  
	 	 this.fullWidth = this.getWidth();
	 	 this.row2Position = this.fullWidth;
	 	 
	 	 Logger.d(TAG, "fullWidth=" + fullWidth);
	 //	 this.movementTriggerTapcheckThreshold = this.fullWidth * this.movementTriggerTapcheckThresholdPercentageOfTotalWidth;
 
	 	 this.parent.captureTime("SetDerivedValues before getHeight");		
	 	  this.height = this.getHeight() - this.parent.getBottomHeight();
//		 		 Utils.convertDensityPixelsToPixels(parent, this.parent.getResources().getInteger(com.riotapps.wordbase.R.integer.gameboard_button_area_height));// - 
 
		 this.speedStarterDistanceMax = Utils.convertPixelsBasedOnXxhdpiReolution(this.parent, this.speedStarterDistanceMax);
		 this.speedStarterDistanceMin = Utils.convertPixelsBasedOnXxhdpiReolution(this.parent, this.speedStarterDistanceMin);
	 	 this.speedDistanceMax = Utils.convertPixelsBasedOnXxhdpiReolution(this.parent, this.speedDistanceMax);
	 	 this.speedDistanceMin = Utils.convertPixelsBasedOnXxhdpiReolution(this.parent, this.speedDistanceMin);
	 	 this.maxStartButtonSize = Utils.convertPixelsBasedOnXxhdpiReolution(this.parent, this.maxStartButtonSize);
	 	 this.maxTileSize = Utils.convertPixelsBasedOnXxhdpiReolution(this.parent, this.maxTileSize);
	 	  
	 	 this.verticalMidPoint = Math.round(this.height / 2); 
	 	 this.horizontalMidPoint = Math.round(this.fullWidth / 2); 	 	 
	 	 this.startWidth = Math.round(this.height * this.startButtonPercentageOfHeight );
	 	 
	 //	 this.startXPosition = horizontalMidPoint - Math.round(this.startWidth / 2);
	 //	 this.startXPosition = horizontalMidPoint - Math.round(this.startWidth / 2);
	 	 
	 	 this.startButtonArea = new RectArea();
	 	 this.startButtonArea.setTop(verticalMidPoint - Math.round(this.startWidth / 2));
	 	 this.startButtonArea.setLeft(horizontalMidPoint - Math.round(this.startWidth / 2));
	 	 this.startButtonArea.setRight(this.startButtonArea.getLeft() + this.startWidth);
	 	 this.startButtonArea.setBottom(this.startButtonArea.getTop() + this.startWidth);
	 	 
	 	 Logger.d(TAG, "setDerive vmp=" + this.verticalMidPoint + " hmp=" +  this.horizontalMidPoint + " sba.top=" + this.startButtonArea.getTop() + 
	 			" sba.left=" + this.startButtonArea.getLeft() + " sba.right=" + this.startButtonArea.getRight() + 
	 			" sba.bottom=" + this.startButtonArea.getBottom());
	 	 
	 	 this.maxUsableHeightPerRow = Math.round(this.height / this.rowHeightDivisor);
	 	 
	 	 this.tileSize =  this.maxUsableHeightPerRow;
	 	 if (this.tileSize > this.maxTileSize){
	 		 this.tileSize = this.maxTileSize; 
	 	 }
	 	 
	 	// this.tileStarterMaxDistance = Math.round(this.tileSize * this.tileStarterPercentage) + this.borderVisibleWidth; //convert relative dp to xxhdpi
	 	 
	 	 Logger.d(TAG, "SetDerivedValues h=" +  this.height + " tileSize=" + this.tileSize );
	 	 this.borderTotalWidth = Math.round(this.height / 4);
	 	 
	 //	 this.speedDistance = Math.round(this.tileSize * this.speedDistancePercentageOfTileSize);
	 	 
	 	 this.row2_yPosition = this.verticalMidPoint - Math.round(this.tileSize / 2);
	 	 
	 	 this.row1_yPosition = this.row2_yPosition - this.tileSize - this.tileGap;   
	 	 if (this.row1_yPosition < 0){
	 		 this.row1_yPosition = backoffDistanceFromEdge;
	 	 }
	 	this.row3_yPosition = this.row2_yPosition + this.tileSize + this.tileGap;
	 	 if (this.row1_yPosition > this.height){
	 		 this.row1_yPosition = this.height - this.backoffDistanceFromEdge;
	 	 }
	 	 
	 	this.parent.captureTime("SetDerivedValues before math");
	/* 	this.trayTileSize = Math.round(this.fullWidth / 7.50f);	
	 	int maxTrayTileSize = this.parent.getResources().getInteger(R.integer.maxTrayTileSize);
	 	int maxDraggingTileSize = this.parent.getResources().getInteger(R.integer.maxDraggingTileSize);
		if (this.trayTileSize > maxTrayTileSize){this.trayTileSize = maxTrayTileSize;}
		this.draggingTileSize  = Math.round(this.trayTileSize * 1.6f);
		if (this.draggingTileSize > maxDraggingTileSize){this.draggingTileSize = maxDraggingTileSize;}
		this.trayTileLeftMargin = Math.round(this.fullWidth - ((this.trayTileSize * 7) + (TRAY_TILE_GAP * 6))) / 2;
	 	this.trayTop = this.height - trayTileSize -  TRAY_VERTICAL_MARGIN; 
		this.bottomOfFullView = this.trayTop - TRAY_VERTICAL_MARGIN - TRAY_TOP_BORDER_HEIGHT - 1;
		this.topGapHeight = Math.round((this.bottomOfFullView - this.fullWidth) / 2);
		this.bottomGapHeight = this.bottomOfFullView - this.fullWidth -  this.topGapHeight;
	 */	 
		//this.fullViewTileWidth = Math.round(this.fullWidth/15) - this.tileGap; //-1 for the space between each tile
		
	 	 if (GameSurfaceView.bgTile == null) {
	 		 GameSurfaceView.bgTile = ImageHelper.decodeSampledBitmapFromResource(getResources(), R.drawable.letter_bg, this.tileSize, this.tileSize);
	 		 GameSurfaceView.bgTile = ImageHelper.getResizedBitmap(GameSurfaceView.bgTile, this.tileSize, this.tileSize);
		 }
	 	 
	 	 if (GameSurfaceView.bgTilePlayed == null) {
	 		 GameSurfaceView.bgTilePlayed = ImageHelper.decodeSampledBitmapFromResource(getResources(), R.drawable.letter_played_bg, this.tileSize, this.tileSize);
	 		GameSurfaceView.bgTilePlayed = ImageHelper.getResizedBitmap(GameSurfaceView.bgTilePlayed, this.tileSize, this.tileSize);
		 } 
	 	 
	 	 if (GameSurfaceView.bgTileBonus == null) {     
	 		 GameSurfaceView.bgTileBonus = ImageHelper.decodeSampledBitmapFromResource(getResources(), R.drawable.letter_bonus_bg, this.tileSize, this.tileSize);
	 		 GameSurfaceView.bgTileBonus = ImageHelper.getResizedBitmap(GameSurfaceView.bgTileBonus, this.tileSize, this.tileSize);
		 } 
	 	
	 	 if (GameSurfaceView.bgTileStarter == null) {     
	 		 GameSurfaceView.bgTileStarter = ImageHelper.decodeSampledBitmapFromResource(getResources(), R.drawable.letter_starter_bg, this.tileSize, this.tileSize);
	 		 GameSurfaceView.bgTileStarter = ImageHelper.getResizedBitmap(GameSurfaceView.bgTileStarter, this.tileSize, this.tileSize);
		 } 
	 	 
	 	 if (GameSurfaceView.bgBorder == null) {
	 		 GameSurfaceView.bgBorder = ImageHelper.decodeSampledBitmapFromResource(getResources(), R.drawable.border, this.borderTotalWidth, this.height);
	 		GameSurfaceView.bgBorder = ImageHelper.getResizedBitmap(GameSurfaceView.bgBorder, this.borderTotalWidth, this.height);
		 } 
	 	 
	 	if (GameSurfaceView.bgStart == null) {
	 		 GameSurfaceView.bgStart = ImageHelper.decodeSampledBitmapFromResource(getResources(), R.drawable.start, this.startWidth, this.startWidth);
	 		GameSurfaceView.bgStart = ImageHelper.getResizedBitmap(GameSurfaceView.bgStart, this.startWidth, this.startWidth);
		 } 
	 	
	 	if (GameSurfaceView.bgStartPressed == null) {
	 		 GameSurfaceView.bgStartPressed = ImageHelper.decodeSampledBitmapFromResource(getResources(), R.drawable.start_pressed, this.startWidth, this.startWidth);
	 		GameSurfaceView.bgStartPressed = ImageHelper.getResizedBitmap(GameSurfaceView.bgStartPressed, this.startWidth, this.startWidth);
		 } 
		 this.parent.captureTime("SetDerivedValues after bitmap loads");
		//Toast t = Toast.makeText(context, "Hello " +  this.height + " " + this.fullWidth + " " + getMeasuredHeight() , Toast.LENGTH_LONG);   
	    //t.show();
	}
	

	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		Logger.w(TAG, "surfaceChanged called");
		// TODO Auto-generated method stub
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Logger.w(TAG, "surfaceCreated called");
		this.parent.captureTime("surfaceCreated starting");
		this.startThread();
		this.surfaceCreated = true;
		this.parent.captureTime("surfaceCreated ending");
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Logger.w(TAG, "surfaceDest	royed called");
		if (this.parent != null) {
			this.parent.captureTime("surfaceDestroyed starting");
		}
	    this.stopThread();
	    this.surfaceCreated = false;
	    if (this.parent != null) {
	    	this.parent.captureTime("surfaceDestroyed ending");
	    }
	}
	
	private void dismissDialog(){
	//	if (this.dialog != null){
	//		dialog.dismiss();
	//		dialog = null;
	//	}
	}
	
	public void onPause() {
		Logger.w(TAG, "onPause called");
		this.dismissDialog();
		this.isPaused = true;
		if (!this.isDrawing){
			this.parent.setSurfaceViewReadyToPause(true);
		}
		this.readyToDraw = false;
		this.gameThread.onPause();
	//	 this.stopThread();  
	}
	
	public void onStop() {
		Logger.w(TAG, "onStop called");
		this.dismissDialog();
		 this.stopThread();
	}
	
//	public void onBackPressed() {
//		Logger.w(TAG, "onBackPressed called");
//		 this.stopThread();
//	}
	
	public void onRestart() {
		 Logger.w(TAG, "onRestart called");
		//  if (this.surfaceCreated) {this.startThread();}
	//	this.gameThread = null;
		 this.gameThread = new GameThread(holder, this);
		 this.holder.setFormat(PixelFormat.TRANSPARENT);
	//	 this.startThread();
		 
		 if (this.parent.getGame().isActive()){
			 me.readyToDraw = true;
		 }
	//	 this.startThread(); ///?????
	     if (this.surfaceCreated) {this.startThread();}
	}
	
	 public void onDestroy(){
		 Logger.w(TAG, "onDestroy called");
		this.surfaceDestroyed(this.surfaceHolder); 
		this.parent = null;
	 }
	
	 public void onWindowFocusChanged(){
		 Logger.w(TAG, "onWindowFocusChanged called");
		 this.stopThread();
		 }
	 
	public void onResume() { 
		Logger.w(TAG, "onResume called");
		
		//make sure surface has been created first because onresume is initially called before surfacecreated and starting the 
		//thread then kills things (canvas is null in onDraw)
		this.parent.captureTime("startThread starting");
		this.isPaused = false;
		
	 	if (this.surfaceCreated) {this.startThread();}
	 	
		this.parent.captureTime("startThread ended");
	}
	
	
	private void startThread(){
		
		Logger.w(TAG, "startThread called is thread alive " + this.gameThread.isAlive() + " isThreadRunning: " + this.isThreadRunning); 
		if (!this.isThreadRunning){ //() !=Thread.State.RUNNABLE) { 
			//if (!this.gameThread.isAlive()){
				this.gameThread.start();
				this.gameThread.setRunning(true);
				this.isThreadRunning = true;
			//}
		}
	//	else {
	//		this.gameThread.onResume();
	//	}
	}
	
	public void stopThreadLoop(){
	   
		this.readyToDraw = false;
		// this.gameThread.setRunning(false);
	}
	
	public void startThreadLoop(){
	    this.gameThread.setRunning(true);
	    this.gameThread.run();
	}
	
	private void stopThread(){
		// simply copied from sample application LunarLander:
	    // we have to tell thread to shut down & wait for it to finish, or else
	    // it might touch the Surface after we return and explode
		Logger.w(TAG, "stopThread called is thread alive " + this.gameThread.isAlive() + " isThreadRunning: " + this.isThreadRunning); 
		if (this.isThreadRunning){
	//		this.gameThread = null;
	//		this.isThreadRunning = false;
//		}
		
		    boolean retry = true;
		    this.gameThread.setRunning(false);
		    while (retry) {
		        try {
		        	this.gameThread.join();
		            retry = false;
		        } catch (InterruptedException e) {
		            // we will try it again and again...
		        }
		    }	
	    this.isThreadRunning = false;  
	    Logger.w(TAG, "stopThread is thread alive " + this.gameThread.isAlive());
		}
	}
	

	 @Override
	 public boolean onTouchEvent(MotionEvent event) {
		 
		 if (this.parent == null){
			 this.readyToDraw = false;
			 return true;
		 }

		  this.currentX = (int) event.getRawX() - this.absoluteLeft;
		  this.currentY = (int) event.getRawY() - this.absoluteTop;
		  long currentTouchTime = System.currentTimeMillis();
		  
		  Logger.d(TAG, "MotionEvent=" + event.getAction());
	     synchronized (this.gameThread.getSurfaceHolder()) {
             switch (event.getAction()) {
             
             case MotionEvent.ACTION_DOWN:  ///0
            	 this.tapCheck = currentTouchTime;
            	 
            	 Logger.d(TAG, "onTouchEVent  this.tapCheck=" +  this.tapCheck + " status=" + this.parent.getGame().getStatus());
 
	          
            	 this.previousX = this.currentX;
            	 this.previousY = this.currentY;
	  
            	 if (this.parent.getGame().isActive() || this.parent.getGame().isCompleted()){
	            		if (this.startButtonArea.isCoordinateWithinArea(this.currentX, this.currentY)){
						 Logger.d(TAG, "onTouchEVent ACTION_DOWN  this.startButtonArea.isCoordinateWithinArea=true");
						
						 this.isStartButtonPressed = true;
	            		}
	            	}
            	 else if (this.parent.getGame().isStarted()) {
            		 this.setTileOnDown(this.findTapTargetTile(this.currentX, this.currentY));
            	 }
            	 Logger.d(TAG, "onTouchEvent ACTION_DOWN tapped id=" + this.getTileOnDown());
            	 
            	  break;

             case MotionEvent.ACTION_UP: //1
            	 Logger.d(TAG, "onTouchEvent ACTION_UP tapped id=" + this.getTileOnDown());
            	 Logger.d(TAG, "onTouchEvent ACTION_UP tapCheck=" + this.tapCheck + " currentTouchTime=" + currentTouchTime + " diff=" + (currentTouchTime - this.tapCheck));
	            
            	 this.isStartButtonPressed = false;
            	 
            	 if (this.tapCheck > 0 && currentTouchTime - this.tapCheck <= SINGLE_TAP_DURATION_IN_MILLISECONDS) {
	            	if (this.parent.getGame().isActive() || this.parent.getGame().isCompleted()){
	            		if (this.startButtonArea.isCoordinateWithinArea(this.currentX, this.currentY)){
   						 Logger.d(TAG, "onTouchEVent  this.startButtonArea.isCoordinateWithinArea=true");
   						 this.parent.handleStartOnClick();
	   					 }
	   					 else {
	   						 Logger.d(TAG, "onTouchEVent  this.startButtonArea.isCoordinateWithinArea=false");
	   					 }
	            	}
	            	else if (this.parent.getGame().isStarted() && this.getTileOnDown() >= 0) {
	            		// int actionUpTile = this.findTapTargetTile(this.currentX, this.currentY);
	            		 Logger.d(TAG, "onTouchEVent UP tile this.isPaused=" + this.isPaused);
	            		 //Logger.d(TAG, "onTouchEvent ACTION_UP tapped actionUpTile=" + actionUpTile);
	            		 //if (actionUpTile == this.getTileOnDown()){
	            			 if (!this.isPaused){
	            				 if (this.parent.onTileClick(this.getTappedTile())){
		            				 this.getTappedTile().setPlayed(true);	 
		            			 }
	            			 }
	            		// }
	            		 //else {
	            		//	 this.resetTileOnDown();
	            		 //}
            		 }
            		 else {
            			 this.resetTileOnDown();
            		 }
            	 }
            	 else {
            		 this.resetTileOnDown();
            	 }
	            this.tapCheck = 0;
        	 
        	  break;

             case MotionEvent.ACTION_MOVE: //2
 
            	 break;
             }
	     }
	     
	     return true;
	 }
	 
 
	 private void resetPointsView(){
		
	 }
	 
	 private int findTapTargetTile(int xPosition, int yPosition){
		 int tileId = -1;
		 this.parent.captureTime(TAG + " FindTileFromPositionInFullViewMode loop starting");
		 
		 for (Tile tile : this.parent.getGame().getRow1Tiles()) { 
	    	 if (xPosition >= tile.getLocation().getxLocation() && xPosition <= (tile.getLocation().getxLocation() + this.tileSize + Math.round(this.tileGap / 4 )) &&
	    	 		 yPosition >= tile.getLocation().getyLocation() && yPosition <= tile.getLocation().getyLocation() + this.tileSize + Math.round(this.tileGap / 4 )){
	    		 return tile.getId();
	    	 }
	     } 
		 
		 for (Tile tile : this.parent.getGame().getRow2Tiles()) { 
	    	 if (xPosition >= tile.getLocation().getxLocation() && xPosition <= (tile.getLocation().getxLocation() + this.tileSize + Math.round(this.tileGap / 4 )) &&
	    	 		 yPosition >= tile.getLocation().getyLocation() && yPosition <= tile.getLocation().getyLocation() + this.tileSize + Math.round(this.tileGap / 4 )){
	    		 return tile.getId();
	    	 }
	     }
		 
		 for (Tile tile : this.parent.getGame().getRow3Tiles()) { 
	    	 if (xPosition >= tile.getLocation().getxLocation() && xPosition <= (tile.getLocation().getxLocation() + this.tileSize + Math.round(this.tileGap / 4 )) &&
	    	 		 yPosition >= tile.getLocation().getyLocation() && yPosition <= tile.getLocation().getyLocation() + this.tileSize + Math.round(this.tileGap / 4 )){
	    		 return tile.getId();
	    	 }
	     }
		 
		 this.parent.captureTime(TAG + " FindTileFromPositionInFullViewMode loop ended");
		 return tileId; //null;

	 }
	
	 //Lint made me do it  
	protected void drawFromThread(Canvas canvas) {
		 onDraw(canvas);
	 }
	 
	 @Override
	 protected void onDraw(Canvas canvas) {
		// this.parent.captureTime("onDraw starting");
		 this.isDrawing = true;
		 if (canvas == null){
			 Logger.d("TAG", "onDraw canvas is null");
			 return;
		 }
		 this.readyToDraw = false;
		
		 canvas.drawColor(0, Mode.CLEAR);
		 
		// this.parent.captureTime(TAG + " onDraw started");
		 
		 this.drawField(canvas);
		 
		 this.isDrawing = false;
		 if (this.isPaused){
			 this.parent.setSurfaceViewReadyToPause(true);
		 }
	//	 this.parent.captureTime("onDraw ended");
	 }
   
	
	private void drawField(Canvas canvas){
		
		setSpeed();

		int moveToRow2 = this.drawAndAdvanceTiles(canvas, this.parent.getGame().getRow1Tiles(), 1);
		int moveToRow3 = this.drawAndAdvanceTiles(canvas, this.parent.getGame().getRow2Tiles(), 2);
		int moveToRow1 = this.drawAndAdvanceTiles(canvas, this.parent.getGame().getRow3Tiles(), 3);
 
		if (moveToRow2 >= 0) {
			this.moveToNextRow(this.parent.getGame().getRow1Tiles(), this.parent.getGame().getRow2Tiles(), 2, this.row2_yPosition);
		}
		if (moveToRow3 >= 0) {
			this.moveToNextRow(this.parent.getGame().getRow2Tiles(), this.parent.getGame().getRow3Tiles(), 3, this.row3_yPosition);
		}
		if (moveToRow1 >= 0) {
			this.moveToNextRow(this.parent.getGame().getRow3Tiles(), this.parent.getGame().getRow1Tiles(), 1, this.row1_yPosition);
		}
 
		this.drawBorders(canvas);
		this.drawStartButton(canvas);
		
	//	Logger.d(TAG, "active=" + this.parent.getGame().isActive() + "first tile x loc=" + this.parent.getGame().getRow1Tiles().get(0).getLocation().getxLocation() +
	//		 " tileStarterMaxDistance=" + tileStarterMaxDistance);
	//	if (this.parent.getGame().isActive() && //not start yet
	//			this.parent.getGame().getRow1Tiles().get(0).getLocation().getxLocation() + this.tileSize >= this.tileStarterMaxDistance){ 
	//		//this.stopStarterTile  = true;
	//		this.readyToDraw = false;
	//	}
	//	else{
			this.readyToDraw = true; 
	//	}
	}
	
	
	private void drawStartButton(Canvas canvas){
		
		//work on down state of button 
		if (this.parent.getGame().isActive() || this.parent.getGame().isCompleted()){
			if (this.isStartButtonPressed){
				canvas.drawBitmap(GameSurfaceView.bgStartPressed, this.startButtonArea.getLeft(), startButtonArea.getTop(), null);
			}
			else {
				canvas.drawBitmap(GameSurfaceView.bgStart, this.startButtonArea.getLeft(), startButtonArea.getTop(), null);				
			}
		}
		
	}
	
	private void drawBorders(Canvas canvas){
	 
		canvas.drawBitmap(GameSurfaceView.bgBorder, 0 + this.borderVisibleWidth - this.borderTotalWidth, 0, null);
		canvas.drawBitmap(GameSurfaceView.bgBorder, this.fullWidth - this.borderVisibleWidth, 0, null);
	}
	
	private void setSpeed(){
		
		this.loopCount += 1;
		if (this.loopCount - this.lastSpeedChange >= this.updateSpeedInterval){
			this.lastSpeedChange = this.loopCount;
			
			int speedMax =  this.speedDistanceMax;
			int speedMin =  this.speedDistanceMin;
			
			if (!this.parent.getGame().isStarted()){
				 speedMax =  this.speedStarterDistanceMax;
				 speedMin =  this.speedStarterDistanceMin;
			}
			
			//faster
			if (this.speedDirection == 0 && this.speedDistance < speedMax ){ 
				this.speedDistance += 1;
			}
			//faster but we've reached the max, so flip speed direction
			else if (this.speedDirection == 0 && this.speedDistance >= speedMax ){ 
				this.speedDirection = 1;
				this.speedDistance -= 1;
			}
			//slower
			if (this.speedDirection == 1 && this.speedDistance > speedMin ){ 
				this.speedDistance -= 1;
			} 
			//slower but we've reached the min, so flip speed direction
			else if (this.speedDirection == 1 && this.speedDistance <= speedMin ){ 				
				this.speedDirection = 0;
				this.speedDistance += 1;
			}
		}
 	
	}
	
	private int drawAndAdvanceTiles(Canvas canvas, List<Tile> tiles, int currentRow){

		int direction = currentRow == 2 ? -1 : 1;
		int moveToNext = -1;
		
		for (int i = 0; i < tiles.size(); i++) {  
			Tile tile = tiles.get(i); 
 
			//set initial position
			if (currentRow == 1 && tile.getLocation().getxLocation() == 0 && tile.getLocation().getyLocation() == 0){
				tile.getLocation().setxLocation(this.row1Position - (i * (this.tileSize + this.tileGap)));
				tile.getLocation().setyLocation(this.row1_yPosition);

//				tile.setLocation(new Coordinate(this.row1Position - (i * (this.tileSize + this.tileGap)), 0, 0));
			}

			
			if ((tile.getLocation().getxLocation() + this.tileSize) > 0  && tile.getLocation().getxLocation() < this.fullWidth){
				this.drawTile(canvas, tile.getLocation().getxLocation(), tile.getLocation().getyLocation(), tile.getLetter(), tile.isPlayed(), tile.isBonus());
			}
			
			//if first tile is fully outside of the viewport, move it to the next 
			if (currentRow == 2 && i == 0 && (tile.getLocation().getxLocation() + this.tileSize < 0 )){
				moveToNext = i;
			}
			else if (currentRow != 2 && i == 0 && (tile.getLocation().getxLocation() > this.fullWidth)){
				moveToNext = i;
				//set to last position, after last tile in list (this might not work if there are only enough tiles for a single row
			}
			else {
				tile.getLocation().setxLocation(tile.getLocation().getxLocation() + (this.speedDistance * direction));
			}
		}
		
		return moveToNext;
	}
	
	
	private void moveToNextRow(List<Tile> sourceTiles, List<Tile> targetTiles, int targetRow, int yPosition){
		
		int direction = targetRow == 2 ? 1 : -1;
		int newPositionBase = targetRow == 2 ? this.fullWidth : 0;
		
		Tile source = sourceTiles.get(0);
 
		Tile target = new Tile();
		target.setId(source.getId());
		target.setLetter(source.getLetter());
		//add to end of target list
		int newPosition = newPositionBase  += ((this.tileSize * 2) * direction);
		if (targetTiles.size() > 0) {
			newPosition = targetTiles.get(targetTiles.size() - 1).getLocation().getxLocation();
		}
		 

		target.setBonus(this.isBonus());
		target.setPlayed(false);
		target.setLocation(new Coordinate());
		target.setRow(targetRow);
		target.getLocation().setxLocation(newPosition + ((this.tileSize + this.tileGap) * direction));
		target.getLocation().setyLocation(yPosition);
		
		targetTiles.add(target);
		sourceTiles.remove(0);
	}
	
	
	private boolean isBonus(){

		if (!this.parent.getGame().isStarted()){
			return false;
		}
	 	if (this.parent.getGame().getNumBonus() >= 4){
			return false;
		}
		else {
			//1 in a 50 chance of hitting a bonus
			if (com.riotapps.wordbase.utils.Utils.getRandomNumberFromRange(1, 200) == 10){
				this.parent.getGame().setNumBonus(this.parent.getGame().getNumBonus() + 1);
				return true;
			}
			return false;
		}
		 
	}
	
	private void drawTile(Canvas canvas, int xPosition, int yPosition, String letter, boolean isPlayed, boolean isBonus){
		if (isPlayed) { 
			canvas.drawBitmap(GameSurfaceView.bgTilePlayed, xPosition, yPosition, null);
		}
		else if (isBonus) { 
			canvas.drawBitmap(GameSurfaceView.bgTileBonus, xPosition, yPosition, null);
		}
		else if (!this.parent.getGame().isStarted()) {
			canvas.drawBitmap(GameSurfaceView.bgTileStarter, xPosition, yPosition, null);
		}
		else  {
			canvas.drawBitmap(GameSurfaceView.bgTile, xPosition, yPosition, null);  
		}
    	 Paint pLetter = new Paint();
    	 int baseTextColor = this.parent.getGame().isActive() ? Color.parseColor(this.parent.getString(R.color.game_board_tray_tile_starter_letter)) : Color.parseColor(this.parent.getString(R.color.game_board_tray_tile_letter));
    	 pLetter.setColor(isBonus ? Color.parseColor(this.parent.getString(R.color.game_board_bonus_tile_letter)) : baseTextColor );
    	 pLetter.setTextSize(Math.round(this.tileSize  * .70));
    	 pLetter.setAntiAlias(true); 
    	 pLetter.setTypeface(ApplicationContext.getLetterTypeface()); //(this.letterTypeface);
	     Rect boundsLetter = new Rect();
	     Rect boundsLetterHeight = new Rect();
	     
	     //always base vertical dimension on single letter (T).  based on the font, letters of different height were screwing up the even look
	     pLetter.getTextBounds("T", 0, 1, boundsLetterHeight);
	     pLetter.getTextBounds(letter, 0, letter.length(), boundsLetter); 
	     int letterWidth = (int)pLetter.measureText(letter);
	     
	     
	     //find the verticalMidPoint and scoot over 5% to the left and 5% down
	     int textLeft =  (int) (xPosition + Math.round(this.tileSize  * .5) - (Math.round(letterWidth / 2)));
	     int textTop =  (int) (yPosition + Math.round(this.tileSize  * .5) + (Math.round(boundsLetterHeight.height() / 2)));
	    //int textTop =  tile.getyPosition() + this.trayTileverticalMidPoint + Math.round(this.trayTileverticalMidPoint * .08f) + (Math.round(boundsLetterHeight.height() / 2));
	     
	   //  Logger.d(TAG, "drawTile xPos=" + xPosition + " mid=" + Math.round(this.tileSize  * .5)  + " letter=" + letter + " textLeft=" + textLeft + " letterW=" + boundsLetter.width());

	     canvas.drawText(letter, textLeft, textTop, pLetter);
	}
	 
}
