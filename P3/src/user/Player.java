package user;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import banqi.BanqiGame;
import client_server.client.AbstractClient;
import client_server.client.Client;
import console.AbstractConsole;
import console.PlayerConsole;

public class Player {
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
	
	
	public Player(String host, int port) throws IOException
	{
		this.client = new Client(host,port);
	}

	/* Constructor */
	public Player(String email,String password,String nickName,String host,int port) throws IOException
	{
		this.email = email;
		this.password = password;
		this.nickName = nickName;
		games = new HashMap<Integer,BanqiGame>();
		this.profile = new Profile(nickName);
		this.client = new Client(host,port);
	}
	
	public void setConsole(AbstractConsole console) 
	{
		this.console = console;
	}
	
	public void setEmail(String email)
	{
		this.email = email;
	}
	
	public void setNickName(String nickName)
	{
		games = new HashMap<Integer,BanqiGame>();
		this.profile = new Profile(nickName);
		this.nickName = nickName;
	}
	
	public void setPassword(String password) 
	{
		this.password = password;
	}
	
	public AbstractConsole getConsole()
	{
		return console;
	}
	
	/* Gets a Players nickName */
	public String getNickName()
	{
		return this.nickName;
	}
	
	public String getEmail()
	{
		return this.email;
	}
	
	public String getPassword()
	{
		return this.password;
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
	public AbstractClient getClient() 
	{
		return this.client;
	}

	public String toString()
	{
		return "Email: " + this.email + "\nNickname: " + this.nickName + "\nPassword: " + this.password + "\nProfile: " + this.profile.toString();
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
			AbstractConsole console = new PlayerConsole(p);
			p.setConsole(console);
			console.accept();
		}
	}
}
