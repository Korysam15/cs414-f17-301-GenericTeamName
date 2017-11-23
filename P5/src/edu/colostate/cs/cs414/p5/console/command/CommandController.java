package edu.colostate.cs.cs414.p5.console.command;

import static edu.colostate.cs.cs414.p5.console.command.CommandConstants.COMMAND_HELP_MAP;
import static edu.colostate.cs.cs414.p5.console.command.CommandConstants.LOGGED_IN_HELP;
import static edu.colostate.cs.cs414.p5.console.command.CommandConstants.NOT_LOGGED_IN_HELP;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.colostate.cs.cs414.p5.client_server.logger.Logger;
import edu.colostate.cs.cs414.p5.client_server.logger.Logger.LOG_LEVEL;
import edu.colostate.cs.cs414.p5.console.AbstractConsole;
import edu.colostate.cs.cs414.p5.user.PlayerController;
import edu.colostate.cs.cs414.p5.util.LoggedInException;
import edu.colostate.cs.cs414.p5.util.NotLoggedInException;

public class CommandController {
	private static final Logger LOG = Logger.getInstance();
	private final AbstractConsole console;
	private final PlayerController playerController;
	private final CommandParser parser;

	public CommandController(AbstractConsole console, PlayerController playerController, CommandParser parser) {
		if(console == null || playerController == null || parser == null ) {
			throw new IllegalArgumentException("null arguments are not allowed in: " + this.getClass().getSimpleName());
		}
		this.console = console;
		this.playerController = playerController;
		this.parser = parser;
	}

	public CommandController(AbstractConsole console, PlayerController playerController) {
		this(console,playerController,new CommandParser());
	}

	public void handleCommand(String command) {
		if(parser.parseCommand(command)) {
			performCommand();
		} else {
			console.error(parser.getErrorMessage());
		}
		parser.reset();
	}

	private void performCommand() {
		String command = parser.getCommand();
		attemptCommand(command);
	}

	private void attemptCommand(String command) {
		List<String> params = parser.getParams();
		try {
			switch(command) {
			case "exit":
				exit();
				break;
			case "help":
				help(params);
				break;
			case "register":
				register(params);
				break;
			case "login":
				login(params);
				break;
			case "view-profile":
				viewProfile(params);
				break;
			case "create-game":
				createGame(params);
				break;
			case "logout":
				logout();
				break;
			case "unregister":
				unregister();
				break;
			case "view-invites":
				viewInvites();
				break;
			case "view-sent-invites":
				viewSentInvites();
				break;
			case "clear":
				clear();
				break;
			case "show-players":
				showPlayers();
				break;
			case "open-games":
				openGames();
				break;
			case "set-log-level":
				setLogLevel(params);
				break;
			default:
				console.error("Unkown command '" + command + "'");
				break;
			}
		} catch (NotLoggedInException e1) {
			LOG.debug(e1.getMessage());
			console.error("You must be logged in before you can: " + command);
		} catch (LoggedInException e2) {
			LOG.debug(e2.getMessage());
			console.error("You must be logged off before you can: " + command);
		} catch (IOException e3) {
			LOG.error(e3.getMessage());
			console.error("An error occurred while communicating with the server for: " + command);
		}
	}

	private void clear() {
		console.clear();		
	}

	private void register(List<String> params) throws IOException, LoggedInException {
		String email = null;
		String nickname = null;
		String password = null;
		try {
			if(params.size() <= 0 || params.size() > 3) {
				if(params.size() > 3) {
					console.error("Command 'register' only allows 3 aditional arguments <email> <nickname> <password>");
				}
				email = console.promptUser("Please enter in a valid Email:");
				nickname = console.promptUser("Please enter in a valid nickname: ");
				password = console.promptUser("Please enter your password:");
			} else if(params.size() == 1) {
				email = params.get(0);
				nickname = console.promptUser("Please enter in a valid nickname: ");
				password = console.promptUser("Please enter your password:");
			} else if(params.size() == 2) {
				email = params.get(0);
				nickname = params.get(1);
				password = console.promptUser("Please enter your password:");
			} else {
				email = params.get(0);
				nickname = params.get(1);
				password = params.get(2);
			} 
		} catch (IOException e) {
			LOG.error(e.getMessage());
			console.error("An error occurred while trying read your input.");
			return;
		}

		playerController.register(email, nickname, password);
	}

	private void login(List<String> params) throws IOException, LoggedInException {
		String email = null;
		String password = null;
		try {
			if(params.size() <= 0 || params.size() > 2) {
				if(params.size() > 2) {
					console.error("Command 'login' only allows 2 aditional arguments <email> <password>");
				}
				email = console.promptUser("Please enter in a valid Email:");
				password = console.promptUser("Please enter your password:");
			} else if(params.size() == 1) {
				email = params.get(0);
				password = console.promptUser("Please enter your password:");
			} else {
				email = params.get(0);
				password = params.get(1);
			} 
		} catch (IOException e) {
			LOG.error(e.getMessage());
			console.error("An error occurred while trying read your input.");
			return;
		}

		playerController.login(email, password);

	}

	private void logout() throws IOException, NotLoggedInException {
		playerController.logout();
	}

	private void unregister() throws IOException, NotLoggedInException {
		boolean unregister = false;
		console.warning("Your account will be deleted if you unregister. Are you sure you want to unregister and remove your account?");
		String response;
		try {
			response = console.promptUser("Type 'yes' if you are sure you want to delete your account, otherwise click enter.");
			response = response.toLowerCase();
			if(response.equals("yes")) {
				unregister = true;
				console.warning("Your account will be removed.");
			} else {
				unregister = false;
				console.notice("Your account will not removed.");
			}
		} catch (IOException e) {
			LOG.error(e.getMessage());
			console.error("An error occurred while trying to read your input.");
			console.notice("Your account will not be removed");
			unregister = false;
			return;
		}

		if(unregister) {
			playerController.unregister();
		}
	}

	private void viewProfile(List<String> params) throws NotLoggedInException, IOException {
		String playerToView = null;
		if(params.size() == 1) {
			playerToView = params.get(0);
		} else if(params.size() == 0 || params.size() > 1) {
			if(params.size() > 1) {
				console.error("Command 'view-profile' only allows 1 aditional argument <nickname>");
			}
			try {
				playerToView = console.promptUser("Enter the nickname of the player's profile you would"
						+ " like to view or click enter to view your own:");
				playerToView = playerToView.trim();
				if(playerToView.isEmpty()) {
					playerToView = null; // this will make it display their own
				}
			} catch (IOException e) {
				LOG.error(e.getMessage());
				console.error("An error occurred while trying to read your input.");
				return;
			}
		}

		playerController.viewProfile(playerToView);
	}

	private void createGame(List<String> params) throws IOException, NotLoggedInException {
		Set<String> toInvite = createInviteSet(params);
		String message = null;
		if(toInvite == null) {
			// error occurred while building invite list
			return;
		} else if(toInvite.isEmpty()) {
			console.warning("You didn't invite anyone.");
		} else {
			try {
				message = console.promptUser("Type a message you would like to send with your invitation: ");
			} catch (IOException e) {
				LOG.error(e.getMessage());
				console.error("An error occurred while trying to read your input.");
				return;
			}
		}

		playerController.createGame(new ArrayList<String>(toInvite), message);

	}

	private Set<String> createInviteSet(List<String> params) {
		Set<String> toInvite = new HashSet<String>();
		if(params.size() <= 0) {
			String next = "";
			do {
				try {
					next = console.promptUser("Enter the nickname name of the player you would like to invite"
							+ " or click enter to send the invititations: ");
				} catch (IOException e) {
					LOG.error(e.getMessage());
					console.error("An error occurred while trying to read your input.");
					return null;
				}
				next = next.trim();
				if(!next.isEmpty()) {
					toInvite.add(next);
				} else {
					break;
				}
			} while(true);
		} else {
			for(String next: params) {
				next = next.trim();
				if(!next.isEmpty()) {
					toInvite.add(next);
				}
			}
		}
		return toInvite;
	}

	private void showPlayers() throws NotLoggedInException, IOException {
		playerController.showPlayers();
	}
	
	private void openGames() throws NotLoggedInException, IOException {
		playerController.openGames();
	}

	private void viewSentInvites() throws IOException, NotLoggedInException {
		playerController.viewSentInvites();
	}

	private void viewInvites() throws IOException, NotLoggedInException {
		playerController.viewInvites();
	}

	private void exit() {
		try {
			if(playerController.isPlayerLoggedIn()) {
				playerController.logout();
			}
			Thread.sleep(1000);
			playerController.disconnect();
		} catch(Exception e) {
			LOG.error(e.getMessage());
		}
		System.exit(0);
	}

	private void help(List<String> params) {
		if(params.size() <= 0) {
			if(playerController.isPlayerLoggedIn()) {
				console.displayNoPrompt(LOGGED_IN_HELP);
			} else {
				console.displayNoPrompt(NOT_LOGGED_IN_HELP);
			}
		} else {
			String commandToGiveHelp = params.get(0);
			String helpMSG = COMMAND_HELP_MAP.get(commandToGiveHelp);
			if(helpMSG == null) {
				helpMSG = "Unkown command: '" + commandToGiveHelp + "' type help to see the list of available commands.\n";
			}
			console.displayNoPrompt(helpMSG);
		}
	}
	
	private void setLogLevel(List<String> params) {
		String logLevel = "";
		if(params.size() <= 0 || params.size() > 1) {
			if(params.size() > 1) {
				console.error("Invalid number of arguments");
			}
			StringBuilder builder = new StringBuilder();
			builder.append("Valid Log Levels Are: \n");
			for(LOG_LEVEL level: Logger.LOG_LEVEL.values()) {
				builder.append(level+"\n");
			}
			console.notice(builder.toString());
			
			try {
				logLevel = console.promptUser("Enter the log level you would like: ");
			} catch (IOException e) {
				LOG.error(e.getMessage());
				console.error("An error occurred while trying to read your input.");
				return;
			}
		} else {
			logLevel = params.get(0);
		}
		
		logLevel = logLevel.toUpperCase();
		
		LOG_LEVEL desiredLevel;
		try {
			desiredLevel = LOG_LEVEL.valueOf(logLevel);
		} catch(Exception e) {
			desiredLevel = null;
		}
		
		if(desiredLevel == null) {
			console.error("Unkown LOG LEVEL: " + logLevel);
		} else { 
			LOG.setLogLevel(desiredLevel);
			console.notice("Log level set to: " + desiredLevel);
		}
	}
}
