package com.rozen.wordscroll.hooks;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.content.Context;

import com.rozen.wordscroll.hooks.Game;
import com.riotapps.wordbase.R;
import com.rozen.wordscroll.data.GameData;
import com.riotapps.wordbase.hooks.AlphabetService;
import com.riotapps.wordbase.hooks.Player;
import com.riotapps.wordbase.hooks.PlayerService;
import com.riotapps.wordbase.ui.Coordinate;
import com.riotapps.wordbase.utils.Check;
import com.riotapps.wordbase.utils.DesignByContractException;

public class GameService {

	public static Game createGame(Context ctx, Player contextPlayer) throws DesignByContractException{
		Check.Require(contextPlayer.getActiveGameId().length() == 0, ctx.getString(R.string.validation_create_game_duplicate));
		
		
		Game game = new Game();
		
		DateFormat df = new SimpleDateFormat("MMddyyyyHHmmss", Locale.US);
		Date now = Calendar.getInstance().getTime();        
	 
		game.setId(df.format(now));
		
    	game.setCreateDate(new Date());
    	game.setStatus(1); //active
    
    	game.setRandomVowel(AlphabetService.getRandomVowel());
    	game.setRandomConsonants(AlphabetService.getRandomConsonants());
    	game.setHopper(AlphabetService.getHopper(game.getRandomVowel(), game.getRandomConsonants()));
    	
    	for (int i = 0; i < game.getHopper().size(); i++) { //String letter : game.getHopper()){
    		Tile tile = new Tile();
    		tile.setLetter(game.getHopper().get(i));
    		tile.setPlayed(false);
    		tile.setId(i); //java.util.UUID.randomUUID().toString());
    		tile.setRow(1);
    		tile.setLocation(new Coordinate());
    		game.getRow1Tiles().add(tile);
    	}
    	contextPlayer.setActiveGameId(game.getId());
    	PlayerService.savePlayer(contextPlayer);
    	saveGame(game);
    	
		return game;
 
	}
	
	public static void saveGame(Game game){
	 
		GameData.saveGame(game);
	}
	
	
	public static Game getGame(String gameId){
		return GameData.getGame(gameId);
	}
	
	public static void removeGame(String gameId){
		 
		GameData.removeGame(gameId);
 	}

}
