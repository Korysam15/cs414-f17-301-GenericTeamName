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
	private boolean promptTurn;
	private BanqiGame game;

	public OpenGameTask(BanqiGame game, boolean promptTurn) {
		super(game.getPlayerOne(),game.getPlayerTwo());
		this.promptTurn = promptTurn;
		this.game = game;
	}

	public OpenGameTask(BanqiGame game, String playersName) {
		this(game,game.getBanqiPlayer(playersName).isTurn);
	}

	public OpenGameTask(BanqiGame game) {
		this(game,false);
	}

	public OpenGameTask(DataInputStream din) throws IOException {
		super(din);
		this.promptTurn = din.readBoolean();
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
		dout.writeBoolean(promptTurn);
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
			addGame(player);
		}
	}

	private void addGame(Player player) {
		BanqiGame oldGame = player.getGame(getGameID());
		if(oldGame != null) {
			oldGame.setGameBoard(game.getGameBoard());
			this.game = oldGame;
		} else {
			player.addGame(game.getGameID(), game);
		}

		game.openConsole();

		if(promptTurn) {
			String currentPlayerNickname = ActivePlayer.getInstance().getNickName();
			String otherPlayer = null;
			if(game.getFirstPlayer().nickName.equals(currentPlayerNickname)) {
				otherPlayer = game.getSecondPlayer().nickName;
			} else {
				otherPlayer = game.getFirstPlayer().nickName;
			}
			game.promptTurn(player, otherPlayer);
		}
	}
}
