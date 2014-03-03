package com.riotapps.loopd.data;

import java.util.ArrayList;
import java.util.List;

import com.riotapps.loopd.hooks.Fragment;
import com.riotapps.wordbase.data.DatabaseHelper;
import com.riotapps.wordbase.hooks.WordService;
import com.riotapps.wordbase.utils.Logger;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class FragmentData {
	private static final String TAG = FragmentData.class.getSimpleName();
	
	private SQLiteDatabase database;
	 
	  private DatabaseHelper dbHelper;
	 // private String[] allColumns = { DatabaseHelper.COLUMN_ID,
	//		  DatabaseHelper.COLUMN_COMMENT };

	  public FragmentData(Context context) {
	 //   dbHelper = new DatabaseHelper(context);
	    dbHelper = DatabaseHelper.getInstance(context);
	  }

	  public void open() throws SQLException {	  
		
	 
	    database = dbHelper.getReadableDatabase();
	 
	  }
	  
 
	  public void close() {
		  database.close();
		  dbHelper.close();
	  }

	  
	  public List<Fragment> getRandomFragments(int num) {
		List<Fragment> matches = new ArrayList<Fragment>();
	  	String queryf = "select Fragment, StartPosition from Fragment ORDER BY RANDOM() LIMIT " + num;
	  
		  Cursor c = database.rawQuery(queryf, null);
		  
		  if (c.getCount() > 0) {
			  c.moveToFirst();
			  while (c.isAfterLast() == false) 
			  {
				  Fragment match = new Fragment();
				  match.setLetters(c.getString(0));
				  match.setStartingPosition(c.getInt(1));
			      matches.add(match);
			      c.moveToNext();
			  }
			  
		  }
		  
		  c.close();
		  
		  return matches;
	  }
}
	
