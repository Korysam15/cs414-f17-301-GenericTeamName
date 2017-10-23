package console;

import java.io.IOException;

import client_server.client.AbstractClient;
import client_server.transmission.LoginTask;
import client_server.transmission.RegisterTask;
import user.ActivePlayer;
import user.Player;

public class PlayerConsole extends AbstractConsole {
	private static final String[] noParamCommands = 
		{"exit","help","register","login","logout",};

	private static final int noParamCommandsLength = noParamCommands.length;

	/**
	 * We will store this here to simplify {@link ForwardTask}s
	 */
	private String playerNickName;

	/**
	 * Having this instance within the console should simplify sending {@link Task}s
	 */
	private AbstractClient client;

	public PlayerConsole(Player player) {
		if(player == null) {
			throw new IllegalArgumentException("The PlayerConsole requires a true Player");
		}
		this.player = player;
		outPutBeforeConsole = System.lineSeparator()+"> ";
		playerNickName = player.getNickName();
		client = player.getClient();
		nullCommands();
	}

	/**
	 * Sets a few instance variables to null
	 */
	private void nullCommands() {
		noParamCommand = null;
		paramCommand = null;
		errorMessage = null;
	}

	/**
	 * Handle's commands that do NOT require additional input from the user.
	 * @param command - The String that may represent a command that does not require arguments.
	 * @return True if and only if the command does represent a "no parameter command"
	 * Otherwise returns false.
	 */
	private boolean checkCommandsWithoutParam(String command) {
		int i, length, 
		commandLength = command.length();
		for(i=0;i<noParamCommandsLength;i++) {
			if(command.startsWith(noParamCommands[i])) {
				if(commandLength >= (length = noParamCommands[i].length()) && 
						!command.substring(length, commandLength).matches(".*\\w.*")) { // white space after command is OK.
					noParamCommand = noParamCommands[i];
					return true;
				}
				else {
					errorMessage = "Expected: '" + noParamCommands[i] + "'\nReceived: '" + command + "'"; 
					return false;
				}
			}
		}
		return false;
	}

	/**
	 * Handle's commands that DO require additional input from the user.
	 * @param command - The String that may represent a command that does require arguments.
	 * @return True if and only if the command does represent a "command that requires additional arguments"
	 * Otherwise returns false.
	 */

	/**
	 * Overrides {@link AbstractConsole#display}.
	 * Displays messages to a user through System.out.
	 * @param msg - The Object that represents a message that we want to be displayed to the user.
	 */
	@Override
	public void display(Object msg) {
		if(msg != null)
			System.out.println(msg.toString());
	}

	/**
	 * Handles the parsing and validation of the command.
	 * @param command - The command to be parsed and validated.
	 * @return true if and only if the command is a command handled by this class.
	 */
	@Override
	protected boolean acceptCommand(String command) {
		if(checkCommandsWithoutParam(command))
			return true;

		if(errorMessage == null)
			errorMessage = "Unkown Command: " + command;

		return false;
	}

	/**
	 * Implements desired functionality for commands that do NOT require parameters.
	 */
	private void handleCommandWithoutParam() {
		switch(noParamCommand) {
		case "exit":
			exit();
			break;
		case "help":
			help();
			break;
		case "register":
			register();
			break;
		case "login":
			login();
			break;
		case "view-profile":
			viewProfile();
			break;
		case "create-game":
			createGame();
			break;
		case "logout":
			logout();
			break;
		case "unregister":
			unregister();
			break;
		}
	}

	/**
	 * Implements desired functionality for commands.
	 */
	@Override
	protected void handleCommand() {
		if(noParamCommand != null)
			handleCommandWithoutParam();
		nullCommands();
	}

	/**
	 * Determines how to handle incorrect input from the user.
	 */
	@Override
	protected void handleCommandError() {
		error(errorMessage);
		nullCommands();
	}

	/**
	 * Simply calls super class method {@link AbstractConsole#accept()}.
	 */
	@Override
	public void run() {
		accept();		
	}

	@Override
	public int getParam() {
		return 0;
	}

	private void help() {
		String msg = "type 'help' to see this message.\n";
		if(client.isLoggedIn()) {
			msg += "type 'logout' to logout.\n" +
					"type 'unregister' to logout and remove your account.\n" +
					"type 'create-game' to create a game.\n" +
					"type 'view-profile' to view a player's profile.\n";
		} else {
			msg += "type 'login' to login to your account.\n" +
					"type 'register' to create a new account.\n";
		}
		msg += "type 'exit' to quit this program.";
		display(msg);
	}

	private void exit() {
		logout();
		try {
			Thread.sleep(1000);
			client.disconnectFromServer();
		} catch(Exception e) {

		}
		System.exit(0);
	}

	private void register() {
		try {
			String email = promptUser("Please enter in a valid Email:");
			String nickName = promptUser("Please enter your desired nickname:");
			String password = promptUser("Please enter your password:");
			client.sendToServer(new RegisterTask(email,nickName,password));
			ActivePlayer.setInstance(player);
			player.setEmail(email);
			player.setNickName(nickName);
			player.setPassword(password);
		} catch (IOException e) {
			display("Error occured while registering.");
		}
	}

	private void login() {
		try {
			String email = promptUser("Please enter in a valid Email:");
			String nickName = promptUser("Please enter your desired nickname:");
			String password = promptUser("Please enter your password:");
			client.sendToServer(new LoginTask(email,nickName,password));
			ActivePlayer.setInstance(player);
			player.setEmail(email);
			player.setNickName(nickName);
			player.setPassword(password);
		} catch (IOException e) {
			display("Error occured while logging in.");
		}
	}

	private void createGame() {

	}

	private void createGameInvites() {

	}

	private void viewProfile() {

	}

	private void viewProfile(String other) {

	}

	private void unregister() {

	}

	private void logout() {

	}
}
