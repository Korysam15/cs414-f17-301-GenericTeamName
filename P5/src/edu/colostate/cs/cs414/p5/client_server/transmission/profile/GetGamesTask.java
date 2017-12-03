package edu.colostate.cs.cs414.p5.client_server.transmission.profile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

import edu.colostate.cs.cs414.p5.banqi.BanqiGame;
import edu.colostate.cs.cs414.p5.client_server.server.game_server.game_manager.GameManager;
import edu.colostate.cs.cs414.p5.client_server.server.session.SessionManager;
import edu.colostate.cs.cs414.p5.client_server.transmission.TaskConstents;
import edu.colostate.cs.cs414.p5.client_server.transmission.game.OpenAllGamesTask;

public class GetGamesTask extends ProfileTask {
	
	public GetGamesTask(String playerWhoWants) {
		super(playerWhoWants);
	}
	
	public GetGamesTask(DataInputStream din) throws IOException {
		super(din);
	}

	@Override
	public int getTaskCode() {
		return TaskConstents.GET_GAMES_TASK;
	}

	@Override
	public void writeBytes(DataOutputStream dout) throws IOException {
		super.writeBytes(dout);		
	}

	@Override
	public void run() {
		GameManager gameManager = GameManager.getInstance();
		if(gameManager != null) {
			List<BanqiGame> playersGames = 
					gameManager.getPlayersGames(getRequester());
			sendGames(playersGames);
		}
	}
	
	private void sendGames(List<BanqiGame> playersGames) {
		SessionManager sessionManager = SessionManager.getInstance();
		if(sessionManager != null) {
			OpenAllGamesTask response = new OpenAllGamesTask(playersGames,getRequester());
			sessionManager.sendToClient(response, getRequester());
		}
	}
	
	
}
