package edu.colostate.cs.cs414.p5.client_server.transmission.profile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

import edu.colostate.cs.cs414.p5.banqi.BanqiGame;
import edu.colostate.cs.cs414.p5.client_server.server.game_server.GameManager;
import edu.colostate.cs.cs414.p5.client_server.server.session.SessionManager;
import edu.colostate.cs.cs414.p5.client_server.transmission.Task;
import edu.colostate.cs.cs414.p5.client_server.transmission.TaskConstents;
import edu.colostate.cs.cs414.p5.client_server.transmission.game.OpenAllGamesTask;
import edu.colostate.cs.cs414.p5.client_server.transmission.util.ReadUtils;
import edu.colostate.cs.cs414.p5.client_server.transmission.util.WriteUtils;

public class GetGamesTask extends Task {
	private final String playerWhoWants;
	
	public GetGamesTask(String playerWhoWants) {
		this.playerWhoWants = playerWhoWants;
	}
	
	public GetGamesTask(DataInputStream din) throws IOException {
		this.playerWhoWants = ReadUtils.readString(din);
	}

	@Override
	public int getTaskCode() {
		return TaskConstents.GET_GAMES_TASK;
	}

	@Override
	public void writeBytes(DataOutputStream dout) throws IOException {
		WriteUtils.writeString(playerWhoWants, dout);		
	}

	@Override
	public void run() {
		GameManager gameManager = GameManager.getInstance();
		if(gameManager != null) {
			List<BanqiGame> playersGames = 
					gameManager.getPlayersGames(playerWhoWants);
			sendGames(playersGames);
		}
	}
	
	private void sendGames(List<BanqiGame> playersGames) {
		SessionManager sessionManager = SessionManager.getInstance();
		if(sessionManager != null) {
			OpenAllGamesTask response = new OpenAllGamesTask(playersGames);
			sessionManager.sendToClient(response, playerWhoWants);
		}
	}
	
	
}
