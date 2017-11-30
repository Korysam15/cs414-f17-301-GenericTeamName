package edu.colostate.cs.cs414.p5.client_server.server.game_server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Map;

import edu.colostate.cs.cs414.p5.banqi.BanqiGame;
import edu.colostate.cs.cs414.p5.client_server.logger.Logger;
import edu.colostate.cs.cs414.p5.client_server.transmission.game.FlipPieceTask;
import edu.colostate.cs.cs414.p5.client_server.transmission.game.MoveTask;

public abstract class GameManager {
	protected static final Logger LOG = Logger.getInstance();
	protected final Map<Integer,BanqiGame> gameMap;
	protected final Map<String,Set<BanqiGame>> playerGameMap;

	private static final GameManager instance = new FileGameManager();
	
	public static GameManager getInstance() {
		return instance;
	}
	
	
	protected GameManager() {
		gameMap = new HashMap<Integer,BanqiGame>();
		playerGameMap = new HashMap<String,Set<BanqiGame>>();
		buildGameMaps();
	}

	public int createGame(String playerOne, String playerTwo) {
		int gameID;
		synchronized(gameMap) {
			gameID = (Integer.MIN_VALUE+1) + gameMap.size();
			while(gameMap.containsKey(gameID)) gameID++;
			BanqiGame game = new BanqiGame(gameID,playerOne,playerTwo,false);
			addGame(game);
		}
		return gameID;
	}
	
	protected void addGameRestoredFromRecords(BanqiGame game) {
		int gameID = game.getGameID();
		String playerOne = game.getPlayerOne();
		String playerTwo = game.getPlayerTwo();

		synchronized(gameMap) {
			gameMap.put(gameID, game);
		}
		synchronized(playerGameMap) {
			addGameToPlayerGameMap(playerOne,game);
			addGameToPlayerGameMap(playerTwo,game);
		}
	}

	private void addGame(BanqiGame game) {
		int gameID = game.getGameID();
		String playerOne = game.getPlayerOne();
		String playerTwo = game.getPlayerTwo();

		synchronized(gameMap) {
			gameMap.put(gameID, game);
			addRecord(game);
		}
		synchronized(playerGameMap) {
			addGameToPlayerGameMap(playerOne,game);
			addGameToPlayerGameMap(playerTwo,game);
		}
	}

	private void addGameToPlayerGameMap(String player, BanqiGame game) {
		synchronized(playerGameMap) {
			Set<BanqiGame> playersGameSet = null;
			if(playerGameMap.containsKey(player)) {
				playersGameSet = playerGameMap.get(player);
			} else {
				playersGameSet = new HashSet<BanqiGame>();
				playerGameMap.put(player, playersGameSet);
			}

			if(playersGameSet.contains(game)) {
				LOG.info("Player: " + player + " already has the same BangiGame.. SKIPPING... gameID is: " + game.getGameID());
			} else {
				playersGameSet.add(game);
			}
		}
	}
	
	public List<BanqiGame> getPlayersGames(String player) {
		List<BanqiGame> ret = null;
		synchronized(playerGameMap) {
			if(playerGameMap.containsKey(player)) {
				ret = new ArrayList<BanqiGame>(playerGameMap.get(player));
			} else {
				ret = new ArrayList<BanqiGame>();
			}
		}
		return ret;
	}

	public BanqiGame getGame(int gameID) {
		synchronized(gameMap) {
			return gameMap.get(gameID);
		}
	}
	
	public boolean isValidMove(FlipPieceTask f) {
		return isValidMove(f.getGameID(),f.getX(),f.getY(),f.getPlayerOne());
	}
	
	public boolean isValidMove(MoveTask m) {
		return isValidMove(m.getGameID(),m.getFromX(),m.getFromY(),m.getToX(),m.getToY(),m.getPlayerOne());
	}
	
	public boolean isValidMove(int gameID, int flipOnX, int flipOnY, String playersName) {
		synchronized(gameMap) {
			if(gameMap.containsKey(gameID)) {
				BanqiGame game = gameMap.get(gameID);
				boolean ret = game.makeMove(flipOnX, flipOnY);
				if(ret) {
					game.swapTurns(playersName);
					updateRecord(game);
				}
				return ret;
			} else {
				LOG.error("GameID: " + gameID + " does not exist");
				return false;
			}
		}
	}

	public boolean isValidMove(int gameID, int fromX, int fromY, int toX, int toY, String playersName) {
		synchronized(gameMap) {
			if(gameMap.containsKey(gameID)) {
				BanqiGame game = gameMap.get(gameID);
				boolean ret = game.makeMove(fromX, fromY, toX, toY);
				if(ret) {
					game.swapTurns(playersName);
					updateRecord(game);
				}
				return ret;
			} else {
				LOG.error("GameID: " + gameID + " does not exist");
				return false;
			}
		}
	}

	public void closeGame(int gameID) {
		synchronized(gameMap) {
			if(gameMap.containsKey(gameID)) {
				removeGame(gameMap.get(gameID));
			} else {
				LOG.error("Tried to close a game that does not exist: gameID: " + gameID);
			}
		}
	}
	
	public void removePlayersGames(String player) {
		synchronized(gameMap) {
			synchronized(playerGameMap) {
				Set<BanqiGame> playersGameSet = playerGameMap.get(player);
				if(playersGameSet == null) {
					return;
				} else if(playersGameSet.isEmpty()) {
					return;
				} else {
					removeGames(playersGameSet);					
				}
			}
		}
	}
	
	private void removeGames(Set<BanqiGame> games) {
		synchronized(gameMap) {
			synchronized(playerGameMap) {
				if(games == null) {
					return;
				} else if(games.isEmpty()) {
					return;
				} else {
					int gameID;
					String playerOne;
					String playerTwo;
					for(BanqiGame game: games) {
						gameID = game.getGameID();
						playerOne = game.getPlayerOne();
						playerTwo = game.getPlayerTwo();
						removeGameFromPlayerGameMap(playerOne,game);
						removeGameFromPlayerGameMap(playerTwo,game);
						gameMap.remove(gameID);
					}
					removeRecords(games);
				}
			}
		}
	}
	
	private void removeGame(BanqiGame game) {
		synchronized(gameMap) {
			int gameID = game.getGameID();
			String playerOne = game.getPlayerOne();
			String playerTwo = game.getPlayerTwo();
			synchronized(playerGameMap) {
				removeGameFromPlayerGameMap(playerOne,game);
				removeGameFromPlayerGameMap(playerTwo,game);
			}
			if(gameMap.containsKey(gameID)) {
				gameMap.remove(gameID);
				removeRecord(game);
			} else {
				LOG.error("A BanqiGame with GameID: " + gameID + " does not exist");
			}
		}
	}
	
	private void removeGameFromPlayerGameMap(String player, BanqiGame game) {
		synchronized(playerGameMap) {
			if(playerGameMap.containsKey(player)) {
				Set<BanqiGame> playersGameSet = playerGameMap.get(player);
				if(playersGameSet.contains(game)) {
					playersGameSet.remove(game);
				} else {
					LOG.error("Player: " + player + " does not appear to have a BanqiGame with ID: " + game.getGameID());
				}
			} else {
				LOG.error("Player: " + player + " does not appear to have any games");
			}
		}
	}

	protected abstract void addRecord(BanqiGame game);

	protected abstract void updateRecord(BanqiGame game);
	
	protected abstract void removeRecord(BanqiGame game);
	
	protected abstract void removeRecords(Set<BanqiGame> games);

	protected abstract void buildGameMaps();

}