package console;

import client_server.client.AbstractClient;
import user.Player;

public class PlayerConsole extends AbstractConsole {
	private static final String[] noParamCommands = 
		{"quit"};
	
	private static final int noParamCommandsLength = noParamCommands.length;
	
	private static final String[] paramCommands = 
		{"create-game", "connect-to-server","send-message"};
	
	private static final int paramCommandsLength = paramCommands.length;
	
	private int parameter;
	
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
		outPutBeforeConsole = "\n> ";
		nullCommands();
	}
	
	/**
	 * Sets a few instance variables to null
	 */
	private void nullCommands() {
		noParamCommand = null;
		paramCommand = null;
		errorMessage = null;
		parameter = 0;
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
	private boolean checkCommandsWithParam(String command) {
		int i, length, 
		commandLength = command.length();
		for(i=0;i<paramCommandsLength;i++) {
			if(command.startsWith(paramCommands[i])) {
				try {
					length = paramCommands[i].length()+1;
					if(commandLength > length) { // used trim to remove white space
						parameter = Integer.parseInt(command.substring(length, commandLength).trim());
						paramCommand = paramCommands[i];
						return true;
					}
					// throw exception to enter catch clause... Rather than duplicating code
					else
						throw new NumberFormatException();
				} catch(NumberFormatException e) {
					errorMessage = "Expected: '" + paramCommands[i] + " <number>" +
							"'\nReceived: '" + command + "'";
					return false;
				}
			}
		}
		return false;
	}
	
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
		else if(errorMessage == null && checkCommandsWithParam(command))
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
		case "quit":
			// handle quit command
			break;
		
			/* case "other-command-1":
			// handle another command
			break; */
		/* case "other-command-2":
			// handle another command
			break; */
		/* case "other-command-3:":
			// handle another command
			break; */
		
		// default: // ignore
		
		}
	}
	
	/**
	 * Implements desired functionality for commands that DO require parameters.
	 */
	private void handleCommandWithParam() {
		switch(paramCommand) {
		case "create-game":
			// handle create-game
			break;
		case "connect-to-server":
			// handle
			break;
		case "send-message":
			// handle
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
		else if(paramCommand != null) {
			handleCommandWithParam();
		}
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
		return parameter;
	}
}
