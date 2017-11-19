/**
 * 
 */
package edu.colostate.cs.cs414.p5.client_server.transmission;

/**
 * @author pflagert
 *
 */
public class TaskConstents {
	public static final int TASK = -1;
	public static final int REGISTER_TASK = 0;
	public static final int LOGIN_TASK = 1;
	public static final int FORWARD_TASK = 2;
	public static final int MULTI_FORWARD_TASK = 3;
	public static final int INVITE_TASK = 4;
	public static final int CREATGAME_TASK = 5;
	public static final int REJECTINVITE_TASK = 6;
	public static final int ACCEPT_INVITE_TASK = 7;
	public static final int MOVE_TASK = 8;
	public static final int FLIP_PIECE_TASK = 9;
	public static final int FORFEIT_TASK = 10;
	public static final int UPDATERECORD_TASK = 11;
	public static final int LOGOUT_TASK = 12;
	public static final int UNREGISTER_TASK = 13;
	public static final int GET_PROFILE_TASK = 14;
	public static final int DISPLAY_PROFILE_TASK = 15;
	public static final int LOGIN_GREETING_TASK = 16;
	public static final int LOGIN_ERROR_TASK = 17;
	public static final int REGISTER_GREETING_TASK = 18;
	public static final int REGISTRATION_ERROR_TASK = 19;
	public static final int EXIT_RESPONSE_TASK = 20;
	public static final int INVALID_GAME_TASK = 21;
	public static final int SERVER_DISCONNECTED_TASK = 22;
	public static final int GET_INVITES_TASK = 23;
	public static final int DISPLAY_INVITES_TASK = 24;
	public static final int GET_SENT_INVITES_TASK = 25;
	public static final int DISPLAY_SENT_INVITES_TASK = 26;
	public static final int GET_ALL_PLAYERS_TASK = 27;
	public static final int DISPLAY_PLAYERS_TASK = 28;
	/* public static final int TASK = 29;
	 public static final int TASK = 30;
	.....
	.....
	.....
	 */
	public static final int MESSAGE_TASK = Integer.MAX_VALUE;

}
