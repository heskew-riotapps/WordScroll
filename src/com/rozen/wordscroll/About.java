package com.rozen.wordscroll;

import com.google.ads.AdRequest;
import com.google.ads.AdView;
import com.google.analytics.tracking.android.EasyTracker;
import com.riotapps.wordbase.R;
import com.riotapps.wordbase.hooks.PlayerService;
import com.riotapps.wordbase.hooks.StoreService;
import com.riotapps.wordbase.ui.MenuUtils;
import com.riotapps.wordbase.utils.ApplicationContext;
import com.riotapps.wordbase.utils.Constants;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;

public class About extends FragmentActivity implements View.OnClickListener{

	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.about);
	        
	     //   PlayerService.loadPlayerInHeader(this);
	        
	        loadViews();
	        
	        MenuUtils.hideMenu(this);
	        MenuUtils.setHeaderTitle(this, this.getString(R.string.header_title_about));
	        this.setupFonts();
	        
	        AdView adView = (AdView)this.findViewById(R.id.adView);
	    	if (StoreService.isHideBannerAdsPurchased(this)){	
				adView.setVisibility(View.GONE);
			}
	    	else {
	    		adView.loadAd(new AdRequest());
	    	}
	 }
	 
	 public void loadViews(){
	       //TextView tvSupportText = (TextView)this.findViewById(R.id.tvSupportText);
	       // TextView tvSupportLink = (TextView)this.findViewById(R.id.tvSupportLink);
	        TextView tvVersion = (TextView)this.findViewById(R.id.tvVersion);
	        TextView tvVersionName = (TextView)this.findViewById(R.id.tvVersionName);
	        TextView tvBuildNumber = (TextView)this.findViewById(R.id.tvBuildNumber);
	        TextView tvDeviceSpecs = (TextView)this.findViewById(R.id.tvDeviceSpecs); 
	      //  TextView tvFBLike = (TextView)this.findViewById(R.id.tvFBLike); 
	      //  TextView tvFBPageLink = (TextView)this.findViewById(R.id.tvFBPageLink); 
	        
	        String versionCode = this.getString(R.string.version);
	        String versionName = this.getString(R.string.versionName);
	      
	        tvBuildNumber.setText(String.format(this.getString(R.string.about_build_number), this.getString(R.string.build)));
	        tvVersion.setText(String.format(this.getString(R.string.about_version), versionCode));	
	        tvVersionName.setText(String.format(this.getString(R.string.version_name_display),versionName));
	        tvDeviceSpecs.setText(String.format(this.getString(R.string.about_device_specs), this.getString(R.string.derived_device_screen_size), this.getString(R.string.derived_device_resolution)));
	
	     //   tvSupportLink.setOnClickListener(this);
	     //   tvSupportText.setOnClickListener(this);
	      //  tvSupportText.setOnClickListener(this);
	     //   tvFBLike.setOnClickListener(this);
	     //   tvFBPageLink.setOnClickListener(this);

	 }
	
	@Override
	public void onClick(View v) {
		Intent browserIntent;
		
	//	if (v.getId() == R.id.tvSupportLink || v.getId() == R.id.tvSupportText) {
	//		browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(this.getString(R.string.about_support_link)));
	//		startActivity(browserIntent);
	// 	} //else if (v.getId() == R.id.tvFBLike || v.getId() == R.id.tvFBPageLink) {
		//	browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(this.getString(R.string.about_fb_link)));
		//	startActivity(browserIntent);
		//}
		
	}
 
	public void setupFonts(){

		TextView about_main = (TextView)findViewById(R.id.about_main);	
		TextView tvVersion = (TextView)findViewById(R.id.tvVersion);	
		TextView tvVersionName = (TextView)findViewById(R.id.tvVersionName);
		TextView tvBuildNumber = (TextView)findViewById(R.id.tvBuildNumber);
//		TextView tvSupportText = (TextView)findViewById(R.id.tvSupportText);
//		TextView tvSupportLink = (TextView)findViewById(R.id.tvSupportLink);
	//	TextView tvFBLike = (TextView)findViewById(R.id.tvFBLike);
	//	TextView tvFBPageLink = (TextView)findViewById(R.id.tvFBPageLink);

		about_main.setTypeface(ApplicationContext.getMainFontTypeface());
		tvVersion.setTypeface(ApplicationContext.getMainFontTypeface());
		tvVersionName.setTypeface(ApplicationContext.getMainFontTypeface());
		tvBuildNumber.setTypeface(ApplicationContext.getMainFontTypeface());
	//	tvSupportText.setTypeface(ApplicationContext.getMainFontTypeface());
	//	tvSupportLink.setTypeface(ApplicationContext.getMainFontTypeface());
//		tvFBLike.setTypeface(ApplicationContext.getMainFontTypeface());
//		tvFBPageLink.setTypeface(ApplicationContext.getMainFontTypeface());

	}
 

	@Override
	protected void onStart() {
		 
		super.onStart();
		 EasyTracker.getInstance().activityStart(this);
	}


	@Override
	protected void onStop() {
	 
		super.onStop();
		EasyTracker.getInstance().activityStop(this);
	}
}

