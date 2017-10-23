package user;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

import banqi.BanqiGame;
import client_server.client.AbstractClient;
import client_server.client.Client;
import client_server.transmission.LoginTask;
import client_server.transmission.MessageTask;
import client_server.transmission.RegisterTask;

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
	
	/* Gets a Players nickName */
	public String getNickName()
	{
		return this.nickName;
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

	/* --------------------------------- USER LOGIN ---------------------- */
	public static class UserLogin
	{
		/* Checks if a new player is already registered to the system */
		public static boolean checkIfRegistered(String email, String nickName)
		{
			// Query to server and check if the player has already been made
			return false;
		}

		/* Retrieves a players account from the server and will return the instance of that Player. */
		public static Player getPlayerAccount(String email, String nickName)
		{
			// Query to server and get Player account.
			return null;
		}
		
		/* Will prompt the user for their login/registration credentials. Will update the player variable. */
		public static Player Authenticate(String host,int port) throws IOException
		{
			Player player;
			Scanner scanner = new Scanner(System.in);
			while(true)
			{
				System.out.println("Type in 'register' to create a new account or type in 'login' to login to an already created account.\n");
				String user_input = scanner.nextLine();
				if(user_input.toLowerCase().equals("register"))
				{
					System.out.println("Please enter in a valid Email:\n");
					String email = scanner.nextLine();
					System.out.println("Please enter in a valid Password:\n");
					String password = scanner.nextLine();
					System.out.println("Please enter in a valid Nickname:\n");
					String nickName = scanner.nextLine();
					if(checkIfRegistered(email,nickName))
					{
						System.out.println("The account information you entered in is already in use. Please try a different username or nickname.\n");
					}
					else
					{
						// add player to server/database
						player = new Player(email,password,nickName,host,port);
						player.getClient().sendToServer(new RegisterTask(email,nickName,password));
						ActivePlayer.setInstance(player);
						break;
					}
				}
				else if(user_input.toLowerCase().equals("login"))
				{
					while(true)
					{
						System.out.println("Please enter in a valid Email:\n");
						String email = scanner.nextLine();
						System.out.println("Please enter in a valid Password:\n");
						String password = scanner.nextLine();
						System.out.println("Please enter in a valid Nickname:\n");
						String nickName = scanner.nextLine();
						if(true)
						{
							player = new Player(email,password,nickName,host,port);
							player.getClient().sendToServer(new LoginTask(email,nickName,password));
							ActivePlayer.setInstance(player);
							break;
						}
						else
						{
							System.out.println("The account information you entered in came back invalid. Please check that you spelt everything right.\n");
						}
					}
					break;
				}
				else
				{
					System.out.println("Invalid input. Please type in 'register' to register or 'login' to login.\n");
				}
			}
			return player;
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
				Player tanner = Authenticate(host,port);
				tanner.sendInvitation("Whats up dude!", new ArrayList<String>(Arrays.asList("John")));
			}
		}
	} 
}
