package edu.colostate.cs.cs414.p5.client_server.server.game_server.game_manager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import edu.colostate.cs.cs414.p5.banqi.BanqiGame;
import edu.colostate.cs.cs414.p5.banqi.GameBuilder;

public class DatabaseGameManager extends GameManager {
	protected static final String SEPERATOR=",";
	private static Connection connect = null;
	private static Statement statement = null;
	private static ResultSet resultSet = null;
	private static final Set<BanqiGame> savedGames = new HashSet<BanqiGame>();
	private static final GameBuilder builder = new GameBuilder(SEPERATOR);
	// leave this protected
	protected DatabaseGameManager() {
		super();
		// nick put whatever else you need here.
		// note that stuff here will not used until after #buildGameMaps is called
		// therefore if you need something else, make it a "private static final" (above this constructor) 
	}

	public synchronized void connect(){
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e1) {
			LOG.error("SQL Driver not included in project: needed for " + getClass().getSimpleName());
		}
		try {
			connect = DriverManager
					.getConnection("jdbc:mysql://sql3.freesqldatabase.com/sql3207530?"
							+ "user=sql3207530&password=QSkEyzIj7M");

		} catch (SQLException e) {
			LOG.error("Cannot connect to Server in: " + getClass().getSimpleName());
		}
	}
	// this is called by the constructor in GameManager()
	// this is where you will add games

	// don't use the super method AKA (GameManager#addGame(BanqiGame game)
	// instead use the super method GameManager#addGameRestoredFromRecord(BanqiGame game)

	// see FileGameManager for examples
	@Override
	protected void buildGameMaps() {
		this.connect();
		try {
			statement = connect.createStatement();
			resultSet = statement
					.executeQuery("select * from Invites");
			BanqiGame temp = null;
			while (resultSet.next()) {
				String GameString = resultSet.getString("GameString");
				try {
					temp = builder.createGameFromString(GameString);
				} catch (Exception e) {
					temp = null;
					LOG.error("An error occurred trying to recreate a game from a string: Exception: " + e.getMessage());
				}
				if(temp == null){
					LOG.error("Outdated game storage format. Check database configuration.");
				} else {
					savedGames.add(temp);
				}
			}
		} catch (SQLException e) {
			LOG.error("A error occurred when trying to buildGameMaps due to SQLException: " + e.getMessage());
		}

	}

	@Override
	protected void addRecord(BanqiGame game) {
		if(connect==null){
			this.connect();
		}
		LOG.debug("Adding game with gameID: " + game.getGameID() + " to database.");
		LOG.debug("The games String representation is: " + builder.gameToString((game)));
		try {
			statement = connect.createStatement();
			statement.executeUpdate("insert into Games values(" + 
					game.getGameID() + "," + 
					"'" + builder.gameToString(game) + "')");

		} catch (MySQLIntegrityConstraintViolationException e){
			LOG.error("Game: " + game.getGameID() + " already exists in the database.");		
		} catch (SQLException e) {
			LOG.error("An error occurred when trying to ADD a new BanqiGame: GameID: " +
					game.getGameID() + ".\n" + 
					"This error was caused by SQLException: " + e.getMessage());
		}
	}

	@Override
	protected void updateRecord(BanqiGame game) {
		if(connect==null){
			this.connect();
		}
		LOG.debug("Updating game with gameID: " + game.getGameID() + " to database.");
		LOG.debug("The games String representation is: " + builder.gameToString((game)));
		try {
			statement = connect.createStatement();
			statement.executeUpdate("UPDATE Games SET GameString='" + 
					builder.gameToString(game) + "' where ID = "+game.getGameID());

		} catch (MySQLIntegrityConstraintViolationException e){
			LOG.error("Failed to update game with gameID: " + game.getGameID() + " in the database.\n" +
					"This error was caused by: " + e.getClass().getSimpleName() + ": " + e.getMessage());
		} catch (SQLException e) {
			LOG.error("An error occurred when trying to UPDATE a BanqiGame: GameID: " +
					game.getGameID() + ".\n" + 
					"This error was caused by SQLException: " + e.getMessage());
		}
	}

	@Override
	protected void removeRecord(BanqiGame game) {
		if(connect==null){
			this.connect();
		}
		try {
			statement = connect.createStatement();
			statement.executeUpdate("delete from Games where ID = "+game.getGameID());
		} catch (SQLException e) {
			LOG.error("An error occurred when trying to REMOVE a BanqiGame: GameID: " +
					game.getGameID() + ".\n" + 
					"This error was caused by SQLException: " + e.getMessage());
		}

	}

	@Override
	protected void removeRecords(Set<BanqiGame> games) {
		for(BanqiGame game:games){
			removeRecord(game);
		}

	}

}
