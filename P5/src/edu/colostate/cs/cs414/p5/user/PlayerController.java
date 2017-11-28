package edu.colostate.cs.cs414.p5.user;

import java.io.IOException;
import java.util.List;

import edu.colostate.cs.cs414.p5.client_server.client.AbstractClient;
import edu.colostate.cs.cs414.p5.client_server.transmission.Task;
import edu.colostate.cs.cs414.p5.client_server.transmission.game.invite.GetInvitesTask;
import edu.colostate.cs.cs414.p5.client_server.transmission.game.invite.GetSentInvitesTask;
import edu.colostate.cs.cs414.p5.client_server.transmission.profile.GetAllPlayersTask;
import edu.colostate.cs.cs414.p5.client_server.transmission.profile.GetGamesTask;
import edu.colostate.cs.cs414.p5.client_server.transmission.profile.GetProfileTask;
import edu.colostate.cs.cs414.p5.client_server.transmission.registration_login.LoginTask;
import edu.colostate.cs.cs414.p5.client_server.transmission.registration_login.LogoutTask;
import edu.colostate.cs.cs414.p5.client_server.transmission.registration_login.RegisterTask;
import edu.colostate.cs.cs414.p5.client_server.transmission.registration_login.UnregisterTask;
import edu.colostate.cs.cs414.p5.client_server.transmission.util.ForwardTask;
import edu.colostate.cs.cs414.p5.util.Encryptor;
import edu.colostate.cs.cs414.p5.util.LoggedInException;
import edu.colostate.cs.cs414.p5.util.NotLoggedInException;

public class PlayerController {
	
	private AbstractClient client;
	private Player player;
	
	public PlayerController(Player player, AbstractClient client) {
		if(client == null || player == null) {
			throw new IllegalArgumentException(this.getClass().getSimpleName() + " does not support null argumnets in the constructor.");
		} else {
			player.setClient(client);
			this.player = player;
			this.client = client;
		}
	}
	
	private synchronized void sendTask(Task t) throws IOException {
		client.sendToServer(t);
	}
	
	private void methodRequiresLoggedIn(String methodName) throws NotLoggedInException {
		if(!client.isLoggedIn()) {
			throw new NotLoggedInException("You must be logged in before you can perform method: " + methodName);
		}
	}
	
	private void methodRequiresLoggedOff(String methodName) throws LoggedInException {
		if(client.isLoggedIn()) {
			throw new LoggedInException("You must be logged out before you can perform method: " + methodName);
		}
	}
	
	public synchronized boolean isPlayerLoggedIn() {
		return client.isLoggedIn();				
	}
	
	private synchronized void setPlayerInfo(String email,String password) {
		player.setEmail(email);
		player.setPassword(password);
	}
	
	private synchronized void setPlayerInfo(String email, String nickname, String password) {
		setPlayerInfo(email,password);
		player.setNickName(nickname);
		
	}
	
	public synchronized void login(String email, String password) throws IOException, LoggedInException {
		methodRequiresLoggedOff("login");
		password = Encryptor.encryptPassword(password);
		setPlayerInfo(email,password);
		Task login = new LoginTask(email,password);
		sendTask(login);
	}
	
	public synchronized void register(String email, String nickname, String password) throws IOException, LoggedInException  {
		methodRequiresLoggedOff("register");
		password = Encryptor.encryptPassword(password);
		setPlayerInfo(email,nickname,password);
		Task register = new RegisterTask(email, nickname, password);
		sendTask(register);
	}
	
	public synchronized void logout() throws IOException, NotLoggedInException  {
		methodRequiresLoggedIn("logout");
		String email = player.getEmail();
		setPlayerInfo(null,null,null);
		Task logout = new LogoutTask(email);
		sendTask(logout);
	}
	
	public synchronized void unregister() throws IOException, NotLoggedInException  {
		methodRequiresLoggedIn("unregister");
		String email = player.getEmail(),
				nickname = player.getNickName(),
				password = player.getPassword();
		setPlayerInfo(null,null,null);
		Task unregister = new UnregisterTask(email,nickname,password);
		sendTask(unregister);
	}
	
	public synchronized void viewProfile(String playerToView) throws NotLoggedInException, IOException {
		methodRequiresLoggedIn("viewProfile");
		String nickname = player.getNickName();
		
		if(playerToView == null) {
			playerToView = nickname;
		}
		
		Task getProfile = new GetProfileTask(nickname, playerToView);
		Task forward = new ForwardTask(nickname,getProfile,playerToView);
		sendTask(forward);
	}
	
	public synchronized void viewInvites() throws IOException, NotLoggedInException  {
		methodRequiresLoggedIn("viewInvites");
		String nickname = player.getNickName();
		Task getInvites = new GetInvitesTask(nickname);
		sendTask(getInvites);
	}
	
	public synchronized void viewSentInvites() throws IOException, NotLoggedInException  {
		methodRequiresLoggedIn("viewSentInvites");
		String nickname = player.getNickName();
		Task getSentInvites = new GetSentInvitesTask(nickname);
		sendTask(getSentInvites);
	}
	
	public synchronized void createGame(List<String> playersToInvite, String message) throws IOException, NotLoggedInException {
		methodRequiresLoggedIn("createGame");
		String nickname = player.getNickName();
		Invitation invite = new Invitation(nickname,message,playersToInvite);
		Task inviteTask = invite.toTask();
		sendTask(inviteTask);
	}
	
	public synchronized void showPlayers() throws NotLoggedInException, IOException {
		methodRequiresLoggedIn("showPlayers");
		String nickname = player.getNickName();
		Task showPlayers = new GetAllPlayersTask(nickname);
		sendTask(showPlayers);
	}
	
	public synchronized void openGames() throws NotLoggedInException, IOException {
		methodRequiresLoggedIn("openGames");
		String nickname = player.getNickName();
		Task getGames = new GetGamesTask(nickname);
		sendTask(getGames);
	}
	
	public synchronized void disconnect() throws IOException {
		client.disconnectFromServer();
	}
}
