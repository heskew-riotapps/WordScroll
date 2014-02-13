package com.rozen.wordscroll;

 
import com.riotapps.wordbase.billing.IabHelper;
import com.riotapps.wordbase.utils.ApplicationContext;
import com.riotapps.wordbase.utils.Constants;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;

public class Splash extends com.riotapps.wordbase.Splash {
	private static final String TAG = Splash.class.getSimpleName();
 
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
 
		 
	}
	
	@Override
	 public void route(){
		((ApplicationContext)this.getApplication()).startNewActivity(this, Constants.ACTIVITY_CLASS_GAME_SURFACE);
	 }
	
}
