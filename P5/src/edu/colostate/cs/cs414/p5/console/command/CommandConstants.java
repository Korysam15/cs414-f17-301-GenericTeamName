package edu.colostate.cs.cs414.p5.console.command;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class CommandConstants {
	public static final String WHITE="\033[0;37m";
	public static final String NORM="\033[0m";
	public static final String[] noParamCommands = 
		{"exit","help","register","login",
				"logout","unregister","create-game","open-games","view-profile",
				"view-invites", "view-sent-invites", "clear", "show-players",
				"set-log-level"};

	public static final String[] paramCommands =
		{"login", "register", "create-game","view-profile", "help", "set-log-level"};

	public static final int noParamCommandsLength = noParamCommands.length;
	public static final int paramCommandsLength = paramCommands.length;

	public static final String[][] COMMAND_HELP = {
			{"help", "help: displays this message.\n" +
					"  Usage: help <command>\n" +
					"   If no <command> is given, help will display a list\n" +
					"     of available commands and a brief description.\n" +
					"   If a <command> is given, help will display the usage of <command>\n"
			},
			{"clear", "clear: clears the terminal of any text.\n" +
			"  Usage: clear\n"},
			{"exit", "exit: quits this program.\n" +
			"  Usage: exit\n"},
			{"logout", "logout: logs you out of your current session.\n" +
			"  Usage: logout\n"},
			{"unregister", "unregister: implys 'logout', however your account will be removed.\n" +
					"  Usage: unregister\n" +
					"    Note that you will be prompted for confirmation\n" +
					"      before your account is removed.\n"
			},
			{"create-game", "create-game: creates a game and invites player(s) to the game.\n" +
					"  Usage: create-game [invite#1] [invite#2] [invite#3] ... [invite#]\n"+
					"    (where invite* is the nickname of a player to invite)\n" +
					"\n" +
					"    In the example below (1) you would be prompted to enter all\n"+
					"      of the nicknames of the players in which you would like to invite.\n" +
					"    Example 1:" + WHITE + " create-game\n" + NORM +
					"\n" +
					"    In the example below (2) pflagert would be invited to your game.\n" +
					"    Example 2:" + WHITE + " create-game pflagert\n" + NORM +
					"\n" +
					"    In the last example below (3) pflagert, sam, kory, nick, and jared\n"+
					"      would all be invited to join games with you.\n" +
					"    Example 3:" + WHITE + " create-game pflagert sam kory nick jared\n" + NORM +
					"\n" +
					"  Note: If you invite the same player multiple times,\n" + 
					"    in a single call to create-game,\n" + 
					"    only one invite to that player will be sent.\n"
			},
			{"view-profile", "view-profile: allows you to view another players profile.\n" +
					"  Usage: view-profile <nickname>\n" +
					"\n" +
					"  In the example below (1) you will be prompted to enter the nickname\n" +
					"    of the player who's profile, you would like to view.\n" +
					"  Example 1:" + WHITE + " view-profile\n" + NORM +
					"\n" +
					"  In the example below (2) 'pflagert'(s) profile will be displayed.\n" +
					"  Example 2:" + WHITE + " view-profile pflagert\n" + NORM
			},
			{"view-invites", "view-invites: allows you to accept or reject invitations\n"+
							 "              that were sent to you from other players.\n" +
							 "  Usage: view-invites\n"
							 },
			{"view-sent-invites", "view-sent-invites: to view invitations that you have sent\n" +
								  "                   (and haven't been accepted/rejected).\n" +
								  "  Usage: view-sent-invites\n"
			},
			{"show-players","show-players: shows a list of players and their status\n" +
							"  Usage: what-players\n" +
							"  Note: that a player's status is online, offline, or unkown.\n"
			},
			{"login", "login: to login to your account.\n" +
					"  Usage: login <email> <password>\n" +
					"  Note that <email> and <password> are optional arguments.\n" +
					"\n" +
					"    In the example below (1) you will be prompted to enter\n" + 
					"      <email> and <password>.\n" +
					"    Example 1:" + WHITE + " login\n" + NORM +
					"\n" +
					"    In the example below (2) you will be prompted for <password>.\n" +
					"    Example 2:" + WHITE + " login user@example.com\n" + NORM +
					"\n" +
					"    In the example below (3) you will NOT be prompted for anything.\n" +
					"    Example 3:" + WHITE + " login user@example.com users_password\n" + NORM +
					"\n" +
					"  Note <email> always precedes <password> thus if 1 argument is given\n" +
					"  it is expected to be your email.\n" +
					"\n"
			},
			{"register","register: to create a new account.\n" +
					"  Usage: register <email> <nickname> <password>\n" +
					"\n" +
					"    In the example below (1) you will be prompted to enter\n" + 
					"      <email>, <nickname>, and <password.\n"+
					"    Example 1:" + WHITE + " register\n" + NORM +
					"\n" +
					"    In the example below (2) you will be prompted to enter\n" +
					"      <nickname> and <password>.\n" +
					"    Example 2:" + WHITE + " register user@example.com\n" + NORM +
					"\n" +
					"    In the example below (3) you will be prompted to enter <password>.\n" +
					"    Example 3:" + WHITE + " register user@example.com user_nickname\n" + NORM +
					"\n" +
					"    In the example below (4) you will NOT be prompted for anything.\n" +
					"    Example 4:" + WHITE + " register user@example.com user_nickname users_password\n" + NORM +
					"\n" +
					"  Note that <email> always precedes <nickname>\n"+
					"    and <nickname> always precedes <password>\n" +
					"    If 1 argument is given it is expected to be your email.\n"+
					"    If 2 arguments are given the first argument is expected to be\n" +
					"    to be your email and the second is expected to be your nickname.\n"
			},
	};

	private static final Map<String,String> HELP_MAP = new HashMap<String,String>();
	static {
		for(int i=0;i<COMMAND_HELP.length;i++) {
			HELP_MAP.put(COMMAND_HELP[i][0], COMMAND_HELP[i][1]);
		}
	}
	
	public static final Map<String,String> COMMAND_HELP_MAP = 
			Collections.unmodifiableMap(new HashMap<String,String>(HELP_MAP));


	public static final String HELP_HEADER = "Available Commands [ help, clear, exit, ";
	public static final String ALWAYS_AVAILABLE_HELP = 
			WHITE+"help"+NORM+": displays this message.\n" +
					WHITE+"clear"+NORM+": clears the text on the console.\n" +
					WHITE+"exit"+NORM+": to quit this program.\n";
	public static final String HELP_FOOTER = 
			"\n" +
			"For more information on one of the above commands:\n" +
					"   help "+WHITE+"<command>"+NORM+"\n";
	public static final String LOGGED_IN_HELP =
			HELP_HEADER + "logout, unregister, create-game, view-profile, view-invites, view-sent-invites ]\n\r" +
					ALWAYS_AVAILABLE_HELP +
					WHITE+"logout"+NORM+": allows you to logout of your current session.\n\r" +
					WHITE+"unregister"+NORM+": implys logout, but your account will also be removed.\n" +
					WHITE+"create-game"+NORM+": to create a game and invite players to the game.\n" +
					WHITE+"view-profile"+NORM+": allows you to view a player's profile.\n" +
					WHITE+"view-invites"+NORM+": allows you to accept or reject pending invitations.\n" +
					WHITE+"view-sent-invites"+NORM+": allows you to view pending invitations that you sent.\n" +
					WHITE+"show-players"+NORM+": shows a list of player's and their status\n"+
					HELP_FOOTER;
	public static final String NOT_LOGGED_IN_HELP =
			HELP_HEADER + "login, register ]\n\r" +
					ALWAYS_AVAILABLE_HELP +
					WHITE+"login"+NORM+": to login to your account.\n" +
					WHITE+"register"+NORM+": to create a new account.\n" +
					HELP_FOOTER;
}
