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
			System.err.println("SQL Driver not included in project");
		}
	     try {
			connect = DriverManager
			         .getConnection("jdbc:mysql://sql3.freesqldatabase.com/sql3207530?"
			                 + "user=sql3207530&password=QSkEyzIj7M");
			
		} catch (SQLException e) {
			System.err.println("Cannot connect to Server");
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
	            temp = builder.createGameFromString(GameString);
	            if(temp == null){
	            	
	            }
	           savedGames.add(temp);
	        }
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	@Override
	protected void addRecord(BanqiGame game) {
		if(connect==null){
			this.connect();
		}
		System.out.println(game.getGameID());
		System.out.println(builder.gameToString((game)));
		try {
			statement = connect.createStatement();
			statement
			         .executeUpdate("insert into Games values("+game.getGameID()+","+"'"+builder.gameToString(game)+"')");
			
		} catch (MySQLIntegrityConstraintViolationException e){
			System.err.println("Invite already exists in database");
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void updateRecord(BanqiGame game) {
		if(connect==null){
			this.connect();
		}
		System.out.println(game.getGameID());
		System.out.println(builder.gameToString((game)));
		try {
			statement = connect.createStatement();
			statement
			         .executeUpdate("UPDATE Games SET GameString='"+builder.gameToString(game)+"' where ID = "+game.getGameID());
			
		} catch (MySQLIntegrityConstraintViolationException e){
			e.printStackTrace();
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void removeRecord(BanqiGame game) {
		if(connect==null){
			this.connect();
		}
		try {
			statement = connect.createStatement();
			statement
			         .executeUpdate("delete from Games where ID = "+game.getGameID());
			
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	@Override
	protected void removeRecords(Set<BanqiGame> games) {
		for(BanqiGame game:games){
			removeRecord(game);
		}

	}

}
