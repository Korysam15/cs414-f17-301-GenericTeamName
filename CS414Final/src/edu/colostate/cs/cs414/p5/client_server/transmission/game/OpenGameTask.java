package edu.colostate.cs.cs414.p5.client_server.transmission.game;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import edu.colostate.cs.cs414.p5.banqi.BanqiGame;
import edu.colostate.cs.cs414.p5.banqi.GameBuilder;
import edu.colostate.cs.cs414.p5.client_server.logger.Logger;
import edu.colostate.cs.cs414.p5.client_server.transmission.TaskConstents;
import edu.colostate.cs.cs414.p5.client_server.transmission.util.ReadUtils;
import edu.colostate.cs.cs414.p5.client_server.transmission.util.WriteUtils;
import edu.colostate.cs.cs414.p5.user.ActivePlayer;
import edu.colostate.cs.cs414.p5.user.Player;

public class OpenGameTask extends GameTask {

	public static final String SEPERATOR = ",";
	private BanqiGame game;
	
	public OpenGameTask(BanqiGame game) {
		super(game.getPlayerOne(),game.getPlayerTwo());
		this.game = game;
	}
	
	public OpenGameTask(DataInputStream din) throws IOException {
		super(din);
		String gameAsString = ReadUtils.readString(din);
		BanqiGame game = null;
		try {
			game = GameBuilder.createGameFromString(gameAsString, SEPERATOR);
		} catch(Exception e) {
			game = null;
			Logger.getInstance().error("Failed to recreate a game from string in OpenGameTask");
			e.printStackTrace();
		}
		this.game = game;
	}
	
	@Override
	public void writeBytes(DataOutputStream dout) throws IOException {
		super.writeBytes(dout);
		String gameAsString = GameBuilder.gameToString(game, SEPERATOR);
		WriteUtils.writeString(gameAsString, dout);
	}
	
	@Override
	public int getGameID() {
		return (game != null) ? game.getGameID() : UNASSIGNED_GAME_ID;
	}

	@Override
	public int getTaskCode() {
		return TaskConstents.OPEN_GAME_TASK;
	}

	@Override
	public void run() {
		if(game == null)
			return;
		Player player = ActivePlayer.getInstance();
		if(player != null) {
			BanqiGame oldGame = player.getGame(getGameID());
			if(oldGame != null) {
				oldGame.setGameBoard(game.getGameBoard());
			} else {
				player.addGame(game.getGameID(), game);
				game.openConsole();
			}
		}

	}

}
