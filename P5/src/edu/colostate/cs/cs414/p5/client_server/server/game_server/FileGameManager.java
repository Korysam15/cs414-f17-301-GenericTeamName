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
import edu.colostate.cs.cs414.p5.util.FileUtils;
/**
 * 
 * @author pflagert
 *
 */
public class FileGameManager extends GameManager {
	protected static final String SEPERATOR=",";
	
	private static final File inputFile = new File("games");
	private static final Set<BanqiGame> savedGames = new HashSet<BanqiGame>();
	private static final GameBuilder builder = new GameBuilder(SEPERATOR);
	
	protected FileGameManager() {
		super();
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
	protected synchronized void removeRecords(Set<BanqiGame> games) {
		savedGames.removeAll(games);
		updateFile();
	}

	@Override
	protected synchronized void buildGameMaps() {
		try {
			if(!inputFile.exists()) {
				inputFile.createNewFile();
				LOG.info("Created new file to store games: " + inputFile.getAbsolutePath());
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
					} else if(savedGames.contains(temp)) {
						corrupted = true;
					} else {
						super.addGameRestoredFromRecords(temp);
						savedGames.add(temp);
					}
				}
				br.close();
				if(corrupted) {
					LOG.info(inputFile.getAbsolutePath() + " may be corrupted. ");
					backupFile();
					updateFile();
				}
			}
		} catch (FileNotFoundException e) {
			LOG.error("Could not find file: " + inputFile.getAbsolutePath());
		} catch (IOException e) {
			LOG.error("IOException occurred when trying read: " + inputFile.getAbsolutePath());
		}
	}
	
	private synchronized void backupFile() {
		int i = 0;
		File backup = new File(inputFile.getName()+i);

		while(backup.exists()) 
			backup = new File(inputFile.getName() + (++i));
		
		LOG.info("Backing up: " + inputFile.getAbsolutePath() + " to " + backup.getAbsolutePath());
		FileUtils.copyFileUsingStream(inputFile,backup);
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
