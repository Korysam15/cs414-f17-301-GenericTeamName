package edu.colostate.cs.cs414.p5.client_server.server.session;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import edu.colostate.cs.cs414.p5.client_server.logger.Logger;
import edu.colostate.cs.cs414.p5.client_server.server.AbstractServer;
import edu.colostate.cs.cs414.p5.client_server.server.ActiveServer;
import edu.colostate.cs.cs414.p5.client_server.server.game_server.game_manager.GameManager;
import edu.colostate.cs.cs414.p5.client_server.server.game_server.invite_manager.GameInviteManager;
import edu.colostate.cs.cs414.p5.client_server.server.game_server.profile_manager.ProfileManager;
import edu.colostate.cs.cs414.p5.client_server.server.registry.AbstractRegistry;
import edu.colostate.cs.cs414.p5.client_server.server.registry.ActiveRegistry;
import edu.colostate.cs.cs414.p5.client_server.transmission.Task;
import edu.colostate.cs.cs414.p5.client_server.transmission.registration_login.EntryTask;
import edu.colostate.cs.cs414.p5.client_server.transmission.registration_login.ExitTask;
import edu.colostate.cs.cs414.p5.client_server.transmission.registration_login.LoginTask;
import edu.colostate.cs.cs414.p5.client_server.transmission.registration_login.LogoutTask;
import edu.colostate.cs.cs414.p5.client_server.transmission.registration_login.RegisterTask;
import edu.colostate.cs.cs414.p5.client_server.transmission.registration_login.UnregisterTask;
import edu.colostate.cs.cs414.p5.client_server.transmission.registration_login.response.ExitResponseTask;
import edu.colostate.cs.cs414.p5.client_server.transmission.registration_login.response.LoginErrorTask;
import edu.colostate.cs.cs414.p5.client_server.transmission.registration_login.response.LoginGreetingTask;
import edu.colostate.cs.cs414.p5.client_server.transmission.registration_login.response.RegisterGreetingTask;
import edu.colostate.cs.cs414.p5.client_server.transmission.registration_login.response.RegistrationErrorTask;
import edu.colostate.cs.cs414.p5.client_server.transmission.registration_login.response.ServerDisconnectedTask;

public class SessionManager implements SessionTaskManager {
	
	//// LOG INFO TIMER SETTINGS \\\\
	public static final int SECOND = 1000;
	public static final int MINUTE = 60 * SECOND; 
	public static final int INFO_TIMER_INITIAL_DELAY = 10; // 10 seconds 
	public static final int INFO_TIMER_SUBSEQUENT_RATE = 10; // 10 minutes
	private static final int TIMER_INITIAL_DELAY = INFO_TIMER_INITIAL_DELAY * SECOND;
	private static final int TIMER_RATE = INFO_TIMER_SUBSEQUENT_RATE * MINUTE;
	
	private static final Logger LOG = Logger.getInstance();
	
	private static final SessionManager instance = new SessionManager();
	
	public static final SessionManager getInstance() {
		return instance;
	}
	
	/**
	 * The instance variable clientMap is protected so that classes that extend AbstractServer 
	 * can access the map.
	 * The map contains a key value which is the java.nio.channels.SelectionKey 
	 * which is important for sending and receiving messages to/from clients. 
	 * The entry in the map is a ClientSession.
	 */	
	private Map<SelectionKey,ClientSession> clientMap;
	
	/**
	 * Keeps track of registered clients
	 */
	private Map<String,ClientSession> loggedInClients;
	
	/**
	 * Prints information regarding the number of loggedInClients and
	 * who is logged in.
	 */
	private Timer logInfoTimer;
	
	private SessionManager() {
		this.clientMap = new HashMap<SelectionKey,ClientSession>();
		this.loggedInClients = new HashMap<String,ClientSession>();
		this.logInfoTimer = new Timer();
		startTimer();
	}
	
	private void startTimer() {
		StatusTask timersTask = new StatusTask();
		this.logInfoTimer.schedule(timersTask,
									TIMER_INITIAL_DELAY,
									TIMER_RATE);
	}
	
	public ClientSession getClient(SelectionKey key) {
		synchronized(clientMap) {
			return clientMap.get(key);
		}
	}
	
	public ClientSession getLoggedInClient(String ID) {
		synchronized(loggedInClients) {
			return loggedInClients.get(ID);
		}
	}
	
	public boolean isClientOnline(String ID) {
		synchronized(loggedInClients) {
			return (loggedInClients.get(ID) != null);
		}
	}
	
	private boolean clientIsLoggedIn(String ID) {
		return isClientOnline(ID);
	}
	
	public boolean sendToClient(Task t, String clientID) {
		if(t == null) {
			LOG.error("Task will not be sent to '" + clientID + "' because task is null.");
		} else {
			ClientSession client = getLoggedInClient(clientID);
			if(client != null) {
				try {
					LOG.info("Sending Task: " + t + " to '" + clientID + "' on [" + client + "]");
					client.send(t);
					return true; // only successful if we make it to this line
				} catch(IOException e) {
					LOG.error("An IOException occured while sending Task: " + t + " to '" + clientID + "' on [" + client + "]");
				}
			} else {
				LOG.debug("Target client '" + clientID + "' is not online");
			}
		}
		return false;
	}
	
	public List<ClientSession> getClients() {
		List<ClientSession> sessions = new LinkedList<ClientSession>();
		synchronized(clientMap) {
			for(ClientSession session: clientMap.values()) {
				sessions.add(session);
			}
		}
		return sessions;
	}
	
	public void clientConnected(ClientSession client, SelectionKey key) {
		synchronized(clientMap) {
			clientMap.put(key, client);
		}
		LOG.info("New client connected from [" + client + "].");
	}
	
	public void clientDisconnected(ClientSession client, SelectionKey key) {
		if(key != null) {
			key.cancel();
		}
		synchronized(clientMap) {
			if(clientMap.containsKey(key)) { 
				clientMap.remove(key);
				LOG.info("Client [" + client +"] disconnected.");
			}
		}
		removeLoggedInClient(client);
	}
	
	@Override
	public void handleTask(Task t, ClientSession client) {
		Task response = null;
		if(t instanceof EntryTask) {
			response = loginClient(client,(EntryTask)t);
		} else if(t instanceof ExitTask) {
			response = handleExitTask(client,(ExitTask)t);
		}
		if(response != null) {
			try {
				client.send(response);
			} catch(IOException e) {
				LOG.error("Could not send response Task: " + response + " to " + client);
			}
		} else {
			String id;
			if( (id = client.getID()) != null && clientIsLoggedIn(id)) {
				AbstractServer server = ActiveServer.getInstance();
				server.handleTask(t, client);
			} else {
				LOG.info("Client on [" + client + "] submitted Task: " + t + " before logging in." );
			}
		}
	}
	
	@Override
	public Task handleExitTask(ClientSession client, ExitTask t) {
		Task response = null;
		if(t instanceof UnregisterTask) {
			response = unregisterClient(client,(UnregisterTask) t);
		} else if(t instanceof LogoutTask) {
			response = logoutClient(client,(LogoutTask) t);
		} else {
			LOG.error("Unsupported ExitTask: " + t + " received in SessionManager");
		}
		return response;
	}
	
	@Override
	public Task unregisterClient(ClientSession client, UnregisterTask t) {
		AbstractRegistry registry = ActiveRegistry.getInstance();
		boolean success;
		
		if(registry != null) {
			success = registry.unregisterUser(client.getEmail());
		} else {
			success = false;
		}
		
		String nickname = client.getID();
		
		GameInviteManager.getInstance().removeAllInvitationsFromAndToUser(nickname);
		GameManager.getInstance().removePlayersGamesAsLoss(nickname);
		ProfileManager.getInstance().removeProfile(nickname);
		

		String responseMessage = (success) ? "Successfully unregistered '"+client.getID()+"'" :
			"An error occurred unregistering '" + client.getID() +"'";
		ExitResponseTask response = new ExitResponseTask(responseMessage);
		
		
		removeLoggedInClient(client);
		client.setEmail(null);
		client.setID(null);
		
		return response;
	}
	
	@Override
	public Task logoutClient(ClientSession client, LogoutTask t) {
		ExitResponseTask response = new ExitResponseTask("Good Bye " + client.getID() + "!");
		removeLoggedInClient(client);
		client.setEmail(null);
		client.setID(null);
		return response;
	}
	
	@Override
	public Task loginClient(ClientSession client, EntryTask t) {
		if(t == null) {
			throw new IllegalArgumentException("EntryTask should not be null");
		} else if(t instanceof LoginTask) {
			return loginClient(client,(LoginTask)t);
		} else if(t instanceof RegisterTask) {
			return loginClient(client,(RegisterTask)t);
		} else {
			throw new IllegalArgumentException("Unkown EntryTask type: " + t);
		}
	}
	
	@Override
	public Task loginClient(ClientSession client, LoginTask t) {
		AbstractRegistry registry = ActiveRegistry.getInstance();
		Task response = null;
		if(registry != null) {
			String msg = registry.isValidLogin(t);
			if(msg == null) {
				String nickname = registry.getUser(t.getEmail()).getNickname();
				client.setID(nickname);
				client.setEmail(t.getEmail());
				addLoggedInClient(client,nickname);
				String greeting = createGreetingMessage(nickname);
				response = new LoginGreetingTask(greeting,nickname);
			} else {
				response = new LoginErrorTask(msg);
			}
		} else {
			response = new LoginErrorTask("Error occured while logging in.");
		}
		return response;
	}
	
	public Task loginClient(ClientSession client, RegisterTask t) {
		AbstractRegistry registry = ActiveRegistry.getInstance();
		Task response = null;
		if(registry != null) {
			String msg = registry.registerNewUser(t);
			if(msg == null) {
				String nickname = t.getNickname();
				client.setID(nickname);
				client.setEmail(t.getEmail());
				addLoggedInClient(client,nickname);
				response = new RegisterGreetingTask("Welcome " + nickname + "!");
				ProfileManager.getInstance().addProfile(nickname);
			} else {
				response = new RegistrationErrorTask(msg);
			}
		} else {
			response = new RegistrationErrorTask("Error occured while logging in.");
		}
		return response;
	}
	
	private String createGreetingMessage(String nickname) {
		int pendingInvitations = GameInviteManager.getInstance().getInvitationsToUser(nickname).size();
		int games = GameManager.getInstance().getPlayersGames(nickname).size();
		String greeting = "Welcome Back " + nickname + "!";
		if(games > 0 && pendingInvitations > 0) {
			return greeting + " You have " + games + " active game" + ((games > 1) ? "s.\n" : ".\n") +
					"You also have " + pendingInvitations + " pending game invitation" + ((pendingInvitations > 1) ? "s." : ".");
		} else if(games > 0) {
			return greeting + " You have " + games + " active game" + ((games > 1) ? "s." : ".");
		} else if(pendingInvitations > 0){
			return greeting + " You have " + pendingInvitations + " pending game invitation" + ((pendingInvitations > 1) ? "s." : ".");
		} else {
			return greeting;
		}
	}
	
	private void removeLoggedInClient(ClientSession client) {
		synchronized(loggedInClients) {
			String id = client.getID();
			if(id != null && loggedInClients.containsKey(id)) {
				loggedInClients.remove(id);
				LOG.info(id + " has logged off from [" + client + "].");
				client.setEmail(null);
				client.setID(null);
			}
		}
	}
	
	private void addLoggedInClient(ClientSession client, String nickname) {
		synchronized(loggedInClients) {
			if(loggedInClients.containsKey(nickname)) {
				ClientSession first = loggedInClients.get(nickname);
				LOG.info(nickname + " has logged in twice: first on [" + first + "] and another on [" + client + "].\n" +
						"Logging " + nickname + " off from: [" + first + "]");
				Task logout = new ServerDisconnectedTask("You have been logged off because you have logged in from a different session.");
				sendToClient(logout,nickname);
				removeLoggedInClient(first);
				
			}
			loggedInClients.put(nickname, client);
			LOG.info(nickname + " has logged in from [" + client + "].");
		}
	}
	
	public String getAllClientsStatus() {
		StringBuilder ret = new StringBuilder();
		synchronized(clientMap) {
			int total = clientMap.size();
			int loggedIn;
			ClientSession temp;
			String tempID;
			synchronized(loggedInClients) {
				loggedIn = loggedInClients.size(); 
			}
			ret.append("There are a total of " + total + 
					" clients connected, and " + loggedIn + " are logged in.");
			if(total > 0) {
				ret.append("\n");
				for(Map.Entry<SelectionKey, ClientSession> entry: clientMap.entrySet()) {
					total--;
					temp = entry.getValue();
					tempID = temp.getID();
					if(tempID != null) {
						ret.append(tempID + " is connected on: [" + temp + "].");
					} else {
						ret.append("Client: " + temp + " is not logged in.");
					}
					if(total > 0) {
						ret.append("\n");
					}
				}
			}
		}
		return ret.toString();
	}
	
	// see SessionManager#startTimer()
	private static class StatusTask extends TimerTask {
		private static int PREVIOUSLY_LOGGED_INFO = 0; 
		@Override
		public void run() {
			String toLog;
			AbstractServer server;
			if((server = ActiveServer.getInstance()) != null) {
				toLog = server.getStatus() + "\n" + SessionManager.getInstance().getAllClientsStatus();
			} else {
				toLog = SessionManager.getInstance().getAllClientsStatus();
			}
			
			int toLogCode = toLog.hashCode();
			if(toLogCode != PREVIOUSLY_LOGGED_INFO) { // prevent duplicated logs
				LOG.info(toLog);
				PREVIOUSLY_LOGGED_INFO = toLogCode;
			}
		}
		
	}
}
