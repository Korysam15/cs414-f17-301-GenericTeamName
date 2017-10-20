package clark.user;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import clark.game.BanqiGame;
import pflagert.client.AbstractClient;
import pflagert.client.Client;
import pflagert.transmission.CreateGameTask;
import pflagert.transmission.InviteTask;
import pflagert.transmission.MessageTask;

public class Player {
	/* GLOBAL VARIABLES */
	private String email; // Unique
	private String password;
	private String nickName; // Unique
	private Invitation invite;
	private BanqiGame game;
	private Profile profile;
	private AbstractClient client;

	/* Constructor */
	public Player(String email,String password,String nickName,String host,int port) throws IOException
	{
		this.email = email;
		this.password = password;
		this.nickName = nickName;
		this.profile = new Profile(nickName);
		this.client = new Client(host,port);
	}
	
	public String getNickName()
	{
		return this.nickName;
	}

	/* ------------------------ ADDED CODE ----------------------------------------- */
	/* Allows a player to make a new Banqi Game */
	public void createNewGame() throws IOException
	{
		// User id and timestampe for id of game board
		this.game = new BanqiGame(0);
//		this.client.sendToServer(new CreateGameTask(this.game,this,new Player("Sam","alpha","maycellman","127.0.0.01",8080)));
	}

	/* Creates a new invitation with a message and an arraylist of players to send it to */
	public void sendInvitation(String message,ArrayList<Player> playersToInvite) throws IOException
	{
		this.invite = new Invitation(this.nickName,message,playersToInvite);
		this.client.sendToServer(new InviteTask(this.invite));
	}

	public void acceptInvitation()
	{
		// CREATE ACCEPT TASK
		// send response accept
	}

	public void rejectInvitation() throws IOException
	{
		this.client.sendToServer(new MessageTask("reject invitation"));
	}
	
	/* ------------------------------------------------------------------------------------- */

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
		public static void Authenticate(String host,int port) throws IOException
		{
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
						Player player = new Player(email,password,nickName,host,port);
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
						if(checkIfRegistered(email,nickName))
						{
							Player player = getPlayerAccount(email,nickName);
							player.client = new Client(host,port);
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
			scanner.close();
			
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
				Authenticate(host,port);
			}
		}
	}
}
