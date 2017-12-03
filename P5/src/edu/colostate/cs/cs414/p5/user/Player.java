package edu.colostate.cs.cs414.p5.user;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import edu.colostate.cs.cs414.p5.banqi.BanqiGame;
import edu.colostate.cs.cs414.p5.client_server.client.AbstractClient;
import edu.colostate.cs.cs414.p5.client_server.client.Client;
import edu.colostate.cs.cs414.p5.client_server.logger.Logger;
import edu.colostate.cs.cs414.p5.console.AbstractConsole;
import edu.colostate.cs.cs414.p5.console.PlayerConsole;

public class Player {
	private static final Logger LOG = Logger.getInstance();
	public static InputStream SCANNER; 
	
	/* GLOBAL VARIABLES */
	private String email; // Unique
	private String password;
	private String nickName; // Unique
	private Invitation invite;
	private HashMap<Integer,BanqiGame> games;
	private Profile profile;
	private AbstractClient client;
	private AbstractConsole console;
	
	public Player() {
		LOG.debug("NEW PLAYER CONSTRUCTED");
		this.email = "";
		this.password = "";
		this.nickName = "";
		games = new HashMap<Integer,BanqiGame>();
	}
	
	public Player(String host, int port) throws IOException
	{
		this();
		this.client = new Client(host,port);
	}

	/* Constructor */
	public Player(String email,String password,String nickName,String host,int port) throws IOException
	{
		this();
		this.email = email;
		this.password = password;
		this.nickName = nickName;
		this.profile = new Profile(nickName);
		this.client = new Client(host,port);
	}
	
	public synchronized void setConsole(AbstractConsole console) 
	{
		this.console = console;
	}
	
	public HashMap<Integer,BanqiGame> getGames()
	{
		return this.games;
	}
	
	public synchronized void setEmail(String email)
	{
		LOG.debug("Setting email to: " + email);
		this.email = (email != null) ? email : "";
	}
	
	public synchronized void setNickName(String nickName)
	{
		if(this.nickName.equals(nickName)) {
			LOG.debug("New nickname and old nickname match");
			return;
		} else {
			if(nickName != null && !nickName.isEmpty()) {
				this.profile = new Profile(nickName);
				synchronized(this.games) {
					this.games.clear();
				}
			}
			LOG.debug("Setting nickname to: " + nickName);
			this.nickName = (nickName != null) ? nickName : "";
		}
	}
	
	public synchronized void setPassword(String password) 
	{
		LOG.debug("Setting password");
		this.password = (password != null) ? password : "";
	}
	
	public synchronized AbstractConsole getConsole()
	{
		return console;
	}
	
	/* Gets a Players nickName */
	public synchronized String getNickName()
	{
		LOG.debug("Returning Players nickName: " + nickName);
		return this.nickName;
	}
	
	public synchronized String getEmail()
	{
		LOG.debug("Returning Players email: " + email);
		return this.email;
	}
	
	public synchronized String getPassword()
	{
		LOG.debug("Returning players password");
		return this.password;
	}
	
	public synchronized void setClient(AbstractClient client) {
		this.client = client;
		if(!this.client.isReceiving()) {
			this.client.startReceiving();
		}
	}
	
	/* Get a Players Profile */
	public Profile getProfile()
	{
		return this.profile;
	}
	
	/* Prints the Profile of a user */
	public String getProfileInformation()
	{
		return this.profile.toString();
	}
	
	public BanqiGame getGame(int gameID) {
		synchronized(games) {
			return games.get(gameID);
		}
	}
	
	public void removeGame(int gameID) {
		synchronized(games) {
			if(games.containsKey(gameID)) {
				games.remove(gameID);
			}
		}
	}
	
	public void addGame(int gameID, BanqiGame game) {
		synchronized(games) {
			if(games.containsKey(gameID)) {
				throw new IllegalArgumentException("Can not have two games with the same game ID");
			} else {
				games.put(gameID,game);
				System.out.println("You are now a player in a new banqi game: " + gameID);
			}
		}
	}

	/* Creates a new invitation with a message and an arraylist of players to send it to */
	public void sendInvitation(String message,ArrayList<String> playersToInvite) throws IOException
	{
		this.invite = new Invitation(this.nickName,message,playersToInvite);
		this.client.sendToServer(invite.toTask());
	}
	
	/* Returns an instance of the Players Client */
	public synchronized AbstractClient getClient() 
	{
		return this.client;
	}
	
	public static void main(String[] args) throws IOException, InterruptedException {
		if(args.length != 2) 
		{
			System.out.println("Expected 2 arguments: <server-host> <server-port>");
			return;
		}
		else 
		{
			String host = args[0]; 
			int port;

			try {
				port = Integer.parseInt(args[1]);
				if(port <=0 )
				{
					throw new NumberFormatException();
				}
			} catch (NumberFormatException ex) { 
				System.out.println("Invalid port: " + args[1]);
				return;
			}
			Player p = new Player(host,port);
			p.getClient().startReceiving();
			AbstractConsole console = new PlayerConsole(p.getClient(),p);
			p.setConsole(console);
			console.accept();
		}
	}
}
