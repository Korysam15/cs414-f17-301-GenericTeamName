package edu.colostate.cs.cs414.p4.client_server.server.game_server;

import java.util.HashMap;
import java.util.Map;

import edu.colostate.cs.cs414.p4.banqi.BanqiGame;
import edu.colostate.cs.cs414.p4.client_server.transmission.game.FlipPieceTask;
import edu.colostate.cs.cs414.p4.client_server.transmission.game.MoveTask;

public class GameManager {
	
	private final Map<Integer,BanqiGame> gameMap;
	
	public GameManager() {
		gameMap = new HashMap<Integer,BanqiGame>();
	}
	
	public void createGame() {
		
	}
	
	public BanqiGame getGame(int gameID) {
		return gameMap.get(gameID);
	}
	
	public boolean isValidMove(int gameID, FlipPieceTask flip) {
		return false;
	}
	
	public boolean isValidMove(int gameID, MoveTask move) {
		return false;
	}
	
	public void saveGameWinner(int gameID, String winner) {
		
	}
	
	public void saveGameLoser(int gameID, String loser) {
		
	}
	
	public void saveGameTie(int gameID) {
		
	}
	
	public void closeGame(int gameID) {
		
	}
}
