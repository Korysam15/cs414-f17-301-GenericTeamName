package edu.colostate.cs.cs414.p5.client_server.transmission.game;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.colostate.cs.cs414.p5.banqi.BanqiGame;
import edu.colostate.cs.cs414.p5.client_server.transmission.TaskConstents;
import edu.colostate.cs.cs414.p5.client_server.transmission.util.ReadUtils;
import edu.colostate.cs.cs414.p5.client_server.transmission.util.WriteUtils;

public class OpenAllGamesTask extends GameTask {

	private final List<OpenGameTask> openGameTasks;
	
	public OpenAllGamesTask(List<BanqiGame> games) {
		super();
		openGameTasks = new ArrayList<OpenGameTask>(games.size());
		for(BanqiGame game: games) {
			openGameTasks.add(new OpenGameTask(game));
		}
	}
	
	@SuppressWarnings("unchecked")
	public OpenAllGamesTask(DataInputStream din) throws IOException {
		super(din);
		this.openGameTasks = (List<OpenGameTask>) ReadUtils.readTaskList(din);
	}
	
	@Override
	public void writeBytes(DataOutputStream dout) throws IOException {
		super.writeBytes(dout);
		WriteUtils.writeTaskList(openGameTasks, dout);
	}
	
	@Override
	public int getGameID() {
		return UNASSIGNED_GAME_ID;
	}

	@Override
	public int getTaskCode() {
		return TaskConstents.OPEN_ALL_GAMES_TASK;
	}

	@Override
	public void run() {
		for(OpenGameTask task: openGameTasks) {
			task.run();
		}
	}

}
