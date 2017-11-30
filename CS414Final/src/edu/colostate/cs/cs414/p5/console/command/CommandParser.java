package edu.colostate.cs.cs414.p5.console.command;

import static edu.colostate.cs.cs414.p5.console.command.CommandConstants.noParamCommands;
import static edu.colostate.cs.cs414.p5.console.command.CommandConstants.noParamCommandsLength;
import static edu.colostate.cs.cs414.p5.console.command.CommandConstants.paramCommands;
import static edu.colostate.cs.cs414.p5.console.command.CommandConstants.paramCommandsLength;

import java.util.ArrayList;
import java.util.List;

public class CommandParser {
	public static final int DEFAULT_PARAMETER_LIST_SIZE = 10;
	
	private String validCommand;
	private String errorMessage;
	private List<String> params;
	private final int listSize;
	
	public CommandParser(int listSize) {
		if(listSize < 0) {
			throw new IllegalArgumentException("List size must be a positive Integer");
		} else {
			this.listSize = listSize;
		}
		reset();
	}
	
	public CommandParser() {
		this(DEFAULT_PARAMETER_LIST_SIZE);
	}
	
	public synchronized void reset() {
		validCommand = "";
		errorMessage = null;
		params = new ArrayList<String>(listSize);
	}
	
	public synchronized String getCommand() {
		return validCommand;
	}
	
	public synchronized String getErrorMessage() {
		return errorMessage;
	}
	
	public synchronized List<String> getParams() {
		return new ArrayList<String>(params);
	}
	
	public synchronized boolean parseCommand(String command) {
		if(parseCommandWithoutParam(command) || parseCommandWithParam(command)) {
			return true;
		}
		if(errorMessage == null) {
			errorMessage = "Unkown Command: " + command;
			return false;
		} else {
			return false;
		}
	}
	
	/**
	 * Handle's commands that do NOT require additional input from the user.
	 * @param command - The String that may represent a command that does not require arguments.
	 * @return True if and only if the command does represent a "no parameter command"
	 * Otherwise returns false.
	 */
	private boolean parseCommandWithoutParam(String command) {
		int i, length, 
		commandLength = command.length();
		for(i=0;i<noParamCommandsLength;i++) {
			if(command.startsWith(noParamCommands[i])) {
				if(commandLength >= (length = noParamCommands[i].length()) && 
						!command.substring(length, commandLength).matches(".*\\w.*")) { // white space after command is OK.
					validCommand = noParamCommands[i];
					return true;
				}
				else {
					errorMessage = "Expected '" + noParamCommands[i] + "' which does not support additional arguments.\nReceived: '" + command + "'"; 
					return false;
				}
			}
		}
		return false;
	}

	private boolean parseCommandWithParam(String command) {
		int i=0;
		for(;i<paramCommandsLength;i++) {
			if(command.startsWith(paramCommands[i]+" ")) {
				validCommand = paramCommands[i];
				String[] splitArgs = command.split(" ");
				for(int j=1;j<splitArgs.length;j++) {
					String param = splitArgs[j];
					param = param.trim();
					if(!param.isEmpty())
						params.add(param);
				}
				return true;
			} 
		}
		return false;
	}
}
