package com.rozen.wordscroll;

import com.google.ads.AdRequest;
import com.google.ads.AdView;
import com.google.analytics.tracking.android.EasyTracker;
import com.riotapps.wordbase.hooks.StoreService;
import com.riotapps.wordbase.ui.MenuUtils;
import com.riotapps.wordbase.utils.ApplicationContext;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

public class FullRules extends FragmentActivity{
	
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.fullrules);
	        
	        TextView t2 = (TextView) findViewById(R.id.tvFullRulesBasics_2);
	        t2.setMovementMethod(LinkMovementMethod.getInstance());
	        
	        TextView tvFullRulesDictionary2 = (TextView) findViewById(R.id.tvFullRulesDictionary_2);
	        tvFullRulesDictionary2.setMovementMethod(LinkMovementMethod.getInstance());

	  
	        MenuUtils.hideMenu(this);
	        MenuUtils.setHeaderTitle(this, this.getString(R.string.header_title_rules));
	        
	        this.setupFonts();
	        
	        AdView adView = (AdView)this.findViewById(R.id.adView);
	    	if (StoreService.isHideBannerAdsPurchased(this)){	
				adView.setVisibility(View.GONE);
			}
	    	else {
	    		adView.loadAd(new AdRequest());
	    	}
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
	
	private void setupFonts(){
	
		TextView tvFullRulesBasics_1 = (TextView)findViewById(R.id.tvFullRulesBasics_1);	
		TextView tvFullRulesBasics_2 = (TextView)findViewById(R.id.tvFullRulesBasics_2);
		TextView tvFullRulesBasics_3 = (TextView)findViewById(R.id.tvFullRulesBasics_3);
		TextView tvFullRulesBasics_4 = (TextView)findViewById(R.id.tvFullRulesBasics_4);
		TextView tvFullRulesBasics_5 = (TextView)findViewById(R.id.tvFullRulesBasics_5);
		TextView tvFullRulesBasics_6 = (TextView)findViewById(R.id.tvFullRulesBasics_6);
		TextView tvFullRulesBasics_7 = (TextView)findViewById(R.id.tvFullRulesBasics_7);

		TextView tvFullRulesLetters_1 = (TextView)findViewById(R.id.tvFullRulesLetters_1);
		TextView tvFullRulesLetters_2 = (TextView)findViewById(R.id.tvFullRulesLetters_2);
		
		
		
 		TextView tvFullRulesScoring_1 = (TextView)findViewById(R.id.tvFullRulesScoring_1);
		TextView tvFullRulesScoring_2 = (TextView)findViewById(R.id.tvFullRulesScoring_2);
		TextView tvFullRulesScoring_3 = (TextView)findViewById(R.id.tvFullRulesScoring_3);
		TextView tvFullRulesScoring_4 = (TextView)findViewById(R.id.tvFullRulesScoring_4);
		TextView tvFullRulesScoring_5 = (TextView)findViewById(R.id.tvFullRulesScoring_5);
		TextView tvFullRulesScoring_6 = (TextView)findViewById(R.id.tvFullRulesScoring_6);
		TextView tvFullRulesScoring_7 = (TextView)findViewById(R.id.tvFullRulesScoring_7);
		
 		TextView tvFullRulesLetters_1_num = (TextView)findViewById(R.id.tvFullRulesLetters_1_num);
		TextView tvFullRulesLetters_2_num = (TextView)findViewById(R.id.tvFullRulesLetters_2_num);
		TextView tvFullRulesLetters_3_num = (TextView)findViewById(R.id.tvFullRulesLetters_3_num);
		TextView tvFullRulesLetters_4_num = (TextView)findViewById(R.id.tvFullRulesLetters_4_num);
		TextView tvFullRulesLetters_5_num = (TextView)findViewById(R.id.tvFullRulesLetters_5_num);
		TextView tvFullRulesLetters_6_num = (TextView)findViewById(R.id.tvFullRulesLetters_6_num);
		TextView tvFullRulesLetters_7_num = (TextView)findViewById(R.id.tvFullRulesLetters_7_num);
		TextView tvFullRulesLetters_8_num = (TextView)findViewById(R.id.tvFullRulesLetters_8_num);
		TextView tvFullRulesLetters_9_num = (TextView)findViewById(R.id.tvFullRulesLetters_9_num);
		TextView tvFullRulesLetters_10_num = (TextView)findViewById(R.id.tvFullRulesLetters_10_num);
		TextView tvFullRulesLetters_11_num = (TextView)findViewById(R.id.tvFullRulesLetters_11_num);
		TextView tvFullRulesLetters_12_num = (TextView)findViewById(R.id.tvFullRulesLetters_12_num);
		TextView tvFullRulesLetters_13_num = (TextView)findViewById(R.id.tvFullRulesLetters_13_num);
		TextView tvFullRulesLetters_14_num = (TextView)findViewById(R.id.tvFullRulesLetters_14_num);
		TextView tvFullRulesLetters_15_num = (TextView)findViewById(R.id.tvFullRulesLetters_15_num);
		TextView tvFullRulesLetters_16_num = (TextView)findViewById(R.id.tvFullRulesLetters_16_num);
		TextView tvFullRulesLetters_17_num = (TextView)findViewById(R.id.tvFullRulesLetters_17_num);
		TextView tvFullRulesLetters_18_num = (TextView)findViewById(R.id.tvFullRulesLetters_18_num);
		TextView tvFullRulesLetters_19_num = (TextView)findViewById(R.id.tvFullRulesLetters_19_num);
		TextView tvFullRulesLetters_20_num = (TextView)findViewById(R.id.tvFullRulesLetters_20_num);
		TextView tvFullRulesLetters_21_num = (TextView)findViewById(R.id.tvFullRulesLetters_21_num);
		TextView tvFullRulesLetters_22_num = (TextView)findViewById(R.id.tvFullRulesLetters_22_num);
		TextView tvFullRulesLetters_23_num = (TextView)findViewById(R.id.tvFullRulesLetters_23_num);
		TextView tvFullRulesLetters_24_num = (TextView)findViewById(R.id.tvFullRulesLetters_24_num);
		TextView tvFullRulesLetters_25_num = (TextView)findViewById(R.id.tvFullRulesLetters_25_num);
		TextView tvFullRulesLetters_26_num = (TextView)findViewById(R.id.tvFullRulesLetters_26_num);
  		TextView tvFullRulesDictionary_1 = (TextView)findViewById(R.id.tvFullRulesDictionary_1);
		TextView tvFullRulesDictionary_2 = (TextView)findViewById(R.id.tvFullRulesDictionary_2);
	
	 	tvFullRulesBasics_1.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesBasics_2.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesBasics_3.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesBasics_4.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesBasics_5.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesBasics_6.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesBasics_7.setTypeface(ApplicationContext.getMainFontTypeface());
		
		tvFullRulesLetters_1.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesLetters_2.setTypeface(ApplicationContext.getMainFontTypeface());

	 	tvFullRulesScoring_1.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesScoring_2.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesScoring_3.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesScoring_4.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesScoring_5.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesScoring_6.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesScoring_7.setTypeface(ApplicationContext.getMainFontTypeface());
		
		
	 	tvFullRulesLetters_1_num.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesLetters_2_num.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesLetters_3_num.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesLetters_4_num.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesLetters_5_num.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesLetters_6_num.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesLetters_7_num.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesLetters_8_num.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesLetters_9_num.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesLetters_10_num.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesLetters_11_num.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesLetters_12_num.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesLetters_13_num.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesLetters_14_num.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesLetters_15_num.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesLetters_16_num.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesLetters_17_num.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesLetters_18_num.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesLetters_19_num.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesLetters_20_num.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesLetters_21_num.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesLetters_22_num.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesLetters_23_num.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesLetters_24_num.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesLetters_25_num.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesLetters_26_num.setTypeface(ApplicationContext.getMainFontTypeface());
	 	tvFullRulesDictionary_1.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesDictionary_2.setTypeface(ApplicationContext.getMainFontTypeface());
	}
}
