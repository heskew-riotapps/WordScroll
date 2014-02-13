package com.rozen.wordscroll.ui;

import java.util.ArrayList;
import java.util.List;

import com.riotapps.wordbase.R;
import com.riotapps.wordbase.hooks.AlphabetService;
 
 
import com.riotapps.wordbase.ui.Coordinate;
import com.riotapps.wordbase.ui.TrayTile;
import com.riotapps.wordbase.utils.ApplicationContext;
import com.riotapps.wordbase.utils.ImageHelper;
import com.riotapps.wordbase.utils.Logger;
import com.riotapps.wordbase.utils.Utils;
import com.rozen.wordscroll.GameSurface;
import com.rozen.wordscroll.hooks.Tile;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.Typeface;
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
	private static Bitmap bgTest;
	
	private int row1Position = 0;
	private int row2Position = 0;

	private int tileSize = 200;
	private int tileGap = 10;
	
	public boolean isReadyToDraw() {
		return readyToDraw;
	}

	public void setReadyToDraw(boolean readyToDraw) {
		this.readyToDraw = readyToDraw;
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
			 	  me.loadGame();
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

	public void loadGame(){
	//	Logger.d(TAG,"loadGame game turn=" + this.parent.getGame().getTurn());
		
	 
	    this.parent.callback();
	}

	
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
//	  	 Logger.d(TAG, "SetDerivedValues h=" +  h );
	 	 this.fullWidth = this.getWidth();
	 	 this.row2Position = this.fullWidth;
	 	 
	 	 Logger.d(TAG, "fullWidth=" + fullWidth);
	 //	 this.movementTriggerTapcheckThreshold = this.fullWidth * this.movementTriggerTapcheckThresholdPercentageOfTotalWidth;
 
	 	 this.parent.captureTime("SetDerivedValues before getHeight");		
	 	  this.height = this.getHeight() - 
		 		 Utils.convertDensityPixelsToPixels(parent, this.parent.getResources().getInteger(com.riotapps.wordbase.R.integer.gameboard_button_area_height));// - 
 
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
		
	 	 if (GameSurfaceView.bgTest == null) {
 		 GameSurfaceView.bgTest = ImageHelper.decodeSampledBitmapFromResource(getResources(), com.rozen.wordscroll.R.drawable.letter_bg, this.tileSize, this.tileSize);
 		
			 GameSurfaceView.bgTest = ImageHelper.getResizedBitmap(GameSurfaceView.bgTest, this.tileSize, this.tileSize);
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
		Logger.w(TAG, "surfaceDestroyed called");
		this.parent.captureTime("surfaceDestroyed starting");
	    this.stopThread();
	    this.surfaceCreated = false;
	    this.parent.captureTime("surfaceDestroyed ending");
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
		 
		 me.readyToDraw = true;
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

		 return false;
	 }
	 
 
	 private void resetPointsView(){
		
	 }
	 
	 
	
	 //Lint made me do it  
	protected void drawFromThread(Canvas canvas) {
		 onDraw(canvas);
	 }
	 
	 @Override
	 protected void onDraw(Canvas canvas) {
		// this.parent.captureTime("onDraw starting");
		 if (canvas == null){
			 Logger.d("TAG", "onDraw canvas is null");
			 return;
		 }
		 this.readyToDraw = false;
		 canvas.drawColor(0, Mode.CLEAR);
		 
		// this.parent.captureTime(TAG + " onDraw started");
		 
		 this.drawTest(canvas);
		
	//	 this.parent.captureTime("onDraw ended");
	 }
   
	private void drawTest(Canvas canvas){
	//	Logger.d(TAG, "drawTest started");
		 
	/*	 try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/ 
		int numLetters = this.parent.getGame().getHopper().size();
	/*  	
		for (int i = 0; i < numLetters; i++) {  
			 
			//if (this.parent.getGame().getHopper().get(i).equals("H")){
				if ((this.row1Position - (i * (this.tileSize + this.tileGap)) + this.tileSize) > 0  &&  
					this.row1Position - (i * (this.tileSize + this.tileGap)) < this.fullWidth){
					Logger.d(TAG, "about to draw testPosition=" + this.row1Position + " tile=" + this.parent.getGame().getHopper().get(i) + " i=" + i);
					this.drawTile(canvas, this.row1Position - (i * (this.tileSize + this.tileGap)), 0, this.parent.getGame().getHopper().get(i));
					
					//update tile location, save game on pause 
				}
				//else if (this.testPosition - (i * 190) > this.fullWidth){
				//	Logger.d(TAG, "kicking out of loop - location=" + (this.testPosition - (i * 190)));
				//	break;
				//}
			//}
		}
	  */
		for (int i = 0; i < numLetters; i++) {  
			Tile tile = this.parent.getGame().getRow1Tiles().get(i); 
			
			//set initial position
			if (tile.getLocation().getxLocation() == 0 && tile.getLocation().getyLocation() == 0){
				tile.getLocation().setxLocation(this.row1Position - (i * (this.tileSize + this.tileGap)));
				tile.getLocation().setyLocation(0);

//				tile.setLocation(new Coordinate(this.row1Position - (i * (this.tileSize + this.tileGap)), 0, 0));
			}
			if (i > 4 && i < 8){
				Logger.d(TAG, "about to check testPosition=" + this.row1Position + " tile=" + tile.getLetter() + " x=" + tile.getLocation().getxLocation() + " x+=" + (tile.getLocation().getxLocation()+ this.tileSize) + " i=" + i);
			}
		//	if (i > 4 && i < 8){
		//		Logger.d(TAG, " checking tile=" + tile.getLetter() + " row1Position=" + this.row1Position  + " i=" + i);
		//	}
			//if (this.parent.getGame().getHopper().get(i).equals("H")){
				if ((tile.getLocation().getxLocation() + this.tileSize) > 0  && tile.getLocation().getxLocation() < this.fullWidth){
					if (i > 4 && i < 8){
						Logger.d(TAG, "about to draw pos=" + tile.getLocation().getxLocation() + " tile=" + tile.getLetter() + " i=" + i);
					}
					this.drawTile(canvas, tile.getLocation().getxLocation(), tile.getLocation().getyLocation(), tile.getLetter());
					
					//update tile location, save game on pause 
				}
				//else if (tile.getRow() == 2 && (this.row2Position - (i * (this.tileSize + this.tileGap)) + this.tileSize) > 0  &&  this.row2Position - (i * (this.tileSize + this.tileGap)) < this.fullWidth){
				//	Logger.d(TAG, "about to draw testPosition=" + this.row2Position + " tile=" + tile.getLetter() + " i=" + i);
				//	this.drawTile(canvas, this.row1Position - (i * (this.tileSize + this.tileGap)), 0, tile.getLetter());
				//	
				//	//update tile location, save game on pause 
				//}
				
				//if a tile on the first row is fully outside of the viewport, move it down to the second row
				if (tile.getRow() == 1 && (tile.getLocation().getxLocation() > this.fullWidth)){
					if (i > 4 && i < 8){
						Logger.d(TAG, "letter " + tile.getLetter() + " moving to 2nd row");
					}
					tile.setRow(2);
					tile.getLocation().setxLocation(this.fullWidth + this.tileSize);
					tile.getLocation().setyLocation(210);
				}
				else if (tile.getRow() == 1){  
					if (i > 4 && i < 8){
						Logger.d(TAG, tile.getLetter() + " row 1 advancing location");
					}
					tile.getLocation().setxLocation(tile.getLocation().getxLocation() + 8);
				}
				
				else if (tile.getRow() == 2 && (tile.getLocation().getxLocation() + this.tileSize < 0 )){
					if (i > 4 && i < 8){
						Logger.d(TAG, "letter " + tile.getLetter() + " moving back to 1st row");
					}
					tile.setRow(1);
					
					//set to last position, after last tile in list (this might not work if there are only enough tiles for a single row
					int newPosition = this.parent.getGame().getHopperTiles().get(numLetters - 1).getLocation().getxLocation();
					tile.getLocation().setxLocation(newPosition - this.tileSize - this.tileGap); ///set to last position
					tile.getLocation().setyLocation(0);
				}
				else if (tile.getRow() == 2){  
					if (i > 4 && i < 8){
						Logger.d(TAG, tile.getLetter() + " row 2 advancing location");
					}
					tile.getLocation().setxLocation(tile.getLocation().getxLocation() - 8);
				}
				//else if (this.testPosition - (i * 190) > this.fullWidth){
				//	Logger.d(TAG, "kicking out of loop - location=" + (this.testPosition - (i * 190)));
				//	break;
				//}
			//}
		}
		
		//canvas.drawBitmap(GameSurfaceView.bgTest, this.testPosition - 525, 0, null);
	//	canvas.drawBitmap(GameSurfaceView.bgTest, this.testPosition - 350, 0, null);
	//	 canvas.drawBitmap(GameSurfaceView.bgTest, this.testPosition - 175, 0, null);
	//	 canvas.drawBitmap(GameSurfaceView.bgTest, this.testPosition, 0, null);
		// if (this.row1Position < 5000){
			 this.row1Position += 8;
			 this.readyToDraw = true;
		// }
	//	Logger.d(TAG, "drawTest ended");			 
	 
	}
	
	private void drawTile(Canvas canvas, int xPosition, int yPosition, String letter){
		 canvas.drawBitmap(GameSurfaceView.bgTest, xPosition, yPosition, null);
	 
    	 Paint pLetter = new Paint();
    	 pLetter.setColor(Color.parseColor(this.parent.getString(R.color.game_board_tray_tile_letter)));
    	 pLetter.setTextSize(Math.round(this.tileSize  * .70));
    	 pLetter.setAntiAlias(true); 
    	 pLetter.setTypeface(ApplicationContext.getLetterTypeface()); //(this.letterTypeface);
	     Rect boundsLetter = new Rect();
	     Rect boundsLetterHeight = new Rect();
	     
	     //always base vertical dimension on single letter (T).  based on the font, letters of different height were screwing up the even look
	     pLetter.getTextBounds("T", 0, 1, boundsLetterHeight);
	     pLetter.getTextBounds(letter, 0, letter.length(), boundsLetter); 
	     int letterWidth = (int)pLetter.measureText(letter);
	     
	     
	     //find the midpoint and scoot over 5% to the left and 5% down
	     int textLeft =  (int) (xPosition + Math.round(this.tileSize  * .5) - (Math.round(letterWidth / 2)));
	     int textTop =  (int) (yPosition + Math.round(this.tileSize  * .5) + (Math.round(boundsLetterHeight.height() / 2)));
	    //int textTop =  tile.getyPosition() + this.trayTileMidpoint + Math.round(this.trayTileMidpoint * .08f) + (Math.round(boundsLetterHeight.height() / 2));
	     
	   //  Logger.d(TAG, "drawTile xPos=" + xPosition + " mid=" + Math.round(this.tileSize  * .5)  + " letter=" + letter + " textLeft=" + textLeft + " letterW=" + boundsLetter.width());

	     canvas.drawText(letter, textLeft, textTop, pLetter);
	}
	 
	private void drawFullBoard(Canvas canvas){
	//	Logger.d(TAG, "drawFullBoard");
		 canvas.drawColor(0, Mode.CLEAR);
		 this.drawUpperGap(canvas);
	//	 this.drawFullView(canvas);
		 this.drawLowerGap(canvas);	
	}
	

	
	private void drawUpperGap(Canvas canvas){
		//3366dd
		
	
	}

	private void drawLowerGap(Canvas canvas){
	
		    
	}
	
	
	
	
	 
}
