package com.rozen.wordscroll.hooks;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.SortedSet;

import android.app.ActivityManager;
import android.content.Context;

import com.riotapps.wordbase.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.riotapps.wordbase.data.DatabaseHelper;
import com.riotapps.wordbase.utils.ApplicationContext;
import com.riotapps.wordbase.utils.Cache;
import com.riotapps.wordbase.utils.Logger;
import com.rozen.wordscroll.data.FragmentData;

public class FragmentService {
	private static final String TAG = FragmentService.class.getSimpleName();
	
	
	 
	boolean isLoaded = false;
	
	
	
	private Cache cache = null;
	
	
	private FragmentData data;
	
 
	public FragmentService(Context context){
		//this.context = context;

		this.data = new FragmentData(context);
		
		this.data.open();
		
	 
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		int memoryClassBytes = am.getMemoryClass() * 1024 * 1024;

		this.cache = new Cache(memoryClassBytes / 6);  //drive this from application context 
		
	}
 
	
	public void finish(){
	
		this.data.close();
		
	}
	
	public List<Fragment> getRandomFragments(int num){
		
		return this.data.getRandomFragments(num);
	}
	
	 
	public static void createDatabase(Context context){
		DatabaseHelper db = new DatabaseHelper(context);
		
		try {
			db.createDataBase();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			Logger.d(TAG, "createDatabase " + e.getMessage());
		}
		
		db = null;
	}

}
