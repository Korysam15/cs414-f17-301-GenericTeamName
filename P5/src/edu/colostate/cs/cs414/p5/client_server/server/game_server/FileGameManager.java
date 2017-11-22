package edu.colostate.cs.cs414.p5.client_server.server.game_server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

import edu.colostate.cs.cs414.p5.banqi.BanqiGame;
import edu.colostate.cs.cs414.p5.banqi.GameBuilder;

public class FileGameManager extends GameManager {
	private static final File inputFile = new File("games");
	protected static final String SEPERATOR=",";
	
	private final Set<BanqiGame> savedGames;
	private final GameBuilder builder;
	
	public FileGameManager() {
		super();
		savedGames = new HashSet<BanqiGame>();
		builder = new GameBuilder(SEPERATOR);
	}

	@Override
	protected synchronized void addRecord(BanqiGame game) {
		try {
			PrintWriter pw = new PrintWriter(new FileWriter(inputFile,true));
			pw.println(builder.gameToString(game));
			pw.flush();
			pw.close();
			savedGames.add(game);
		} catch (IOException e) {
			LOG.error("Error when updating game file");
		}
	}

	@Override
	protected synchronized void updateRecord(BanqiGame game) {
		removeRecord(game);
		addRecord(game);
	}

	@Override
	protected synchronized void removeRecord(BanqiGame game) {		
		savedGames.remove(game);
		updateFile();
	}

	@Override
	protected synchronized void buildGameMaps() {
		try {
			if(!inputFile.exists()) {
				inputFile.createNewFile();
				return;
			} else {
				boolean corrupted = false;
				BanqiGame temp;
				BufferedReader br = new BufferedReader(new FileReader(inputFile));
				String line = null;
				while((line = br.readLine()) != null) {
					try {
						temp = builder.createGameFromString(line);
					} catch (Exception e) {
						LOG.error(e.getMessage());
						temp = null;
					}
					if(temp == null) {
						corrupted = true;
					} else {
						super.addGameRestoredFromRecords(temp);
						savedGames.add(temp);
					}
				}
				br.close();
				if(corrupted) {
					updateFile();
				}
			}
		} catch (FileNotFoundException e) {
			// shouldn't happen because of first if statement
		} catch (IOException e) {
		}
	}
	
	private synchronized void updateFile() {
		try {
			PrintWriter pw = new PrintWriter(new FileWriter(inputFile));
			for(BanqiGame game: savedGames) {
				pw.println(builder.gameToString(game));
			}
			pw.close();
		} catch(IOException e) {
			LOG.error(e.getMessage());
		}
	}

}
