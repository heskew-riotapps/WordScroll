package com.riotapps.loopd.hooks;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.content.Context;

import com.riotapps.loopd.GameSurface;
import com.riotapps.loopd.data.GameData;
import com.riotapps.loopd.hooks.Game;
import com.riotapps.wordbase.R;
import com.riotapps.wordbase.hooks.AlphabetService;
import com.riotapps.wordbase.hooks.OpponentService;
import com.riotapps.wordbase.hooks.Player;
import com.riotapps.wordbase.hooks.PlayerService;
import com.riotapps.wordbase.ui.Coordinate;
import com.riotapps.wordbase.utils.Check;
import com.riotapps.wordbase.utils.Constants;
import com.riotapps.wordbase.utils.DesignByContractException;
import com.riotapps.wordbase.utils.Logger;

public class GameService {

	private static final String TAG = GameService.class.getSimpleName();

	public static Game createGame(Context ctx, Player contextPlayer) throws DesignByContractException{
		Logger.d(TAG, "createGame called");
		Check.Require(contextPlayer.getActiveGameId().length() == 0, ctx.getString(R.string.validation_create_game_duplicate));
		
		
		Game game = new Game();
		
		DateFormat df = new SimpleDateFormat("MMddyyyyHHmmss", Locale.US);
		Date now = Calendar.getInstance().getTime();        
	 
		game.setId(df.format(now));
		
    	game.setCreateDate(new Date());
    	game.setStatus(1); //active
    
    	//game.setRandomVowel(AlphabetService.getRandomVowel());
    	//game.setRandomConsonants(AlphabetService.getRandomConsonants());
    	game.setHopper(AlphabetService.getScrollLetters());
    	
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
		Logger.d(TAG, "saveGame called");
		GameData.saveGame(game);
	}
	
	
	public static Game getGame(String gameId){
		return GameData.getGame(gameId);
	}
	
	public static void removeGame(String gameId){
		 
		GameData.removeGame(gameId);
 	}
	
	public static void startGame(Game game){
		game.setStatus(2); //started
		game.setCountdown(1);
	//	saveGame(game);
	}
	
	public static int completeGame(Context context, Player player, Game game){
		 
		//Player player = PlayerService.getPlayer();
    	//add 1 to opponent's wins, save opponent 
    	//add 1 to player's losses, save player
    	player.setActiveGameId(Constants.EMPTY_STRING);
    	

    	///update high score
    	int highScore = game.getScore();
    	
		if (highScore > player.getHighScore()){
			player.setHighScore(highScore);
			PlayerService.savePlayer(player);
		}
		else {
			highScore = player.getHighScore();
		}
        	  
    	game.setStatus(3); //sets up enum for game status and playerGame status
     	
    	saveGame(game);

    	removeGame(game.getId());
				
    	return highScore;
	}


}
