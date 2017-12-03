package edu.colostate.cs.cs414.p5.user;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ProfileBuilder {
	
	private final String fieldSeperator;
	
	public ProfileBuilder(String fieldSeperator) {
		if(fieldSeperator == null) {
			throw new IllegalArgumentException("fieldSperator in ProfileBuilder() can not be null.");
		} else {
			this.fieldSeperator = fieldSeperator;
		}
	}
	
	public Profile createProfileFromString(String line) {
		return createProfileFromString(line,fieldSeperator);
	}
	
	public String profileToString(Profile profile) {
		return profileToString(profile,fieldSeperator);
	}

	
	public static Profile createProfileFromString(String line, String fieldSeperator) {
		String stringArray[] = line.split(fieldSeperator);
		List<String> strings = new LinkedList<String>(Arrays.asList(stringArray));
		
		String playersName = strings.remove(0);
		
		History playersHistory = getHistoryFromStrings(strings);
		
		return new Profile(playersName,playersHistory);
	}
	
	private static History getHistoryFromStrings(List<String> strings) {
		int wins = Integer.parseInt(strings.remove(0));
		int losses = Integer.parseInt(strings.remove(0));
		int draws = Integer.parseInt(strings.remove(0));
		int gamesPlayed = Integer.parseInt(strings.remove(0));
		
		return new History(wins,losses,draws,gamesPlayed);
	}
	
	public static String profileToString(Profile profile, String fieldSeperator) {
		StringBuilder ret = new StringBuilder();
		
		ret.append(profile.getName()+fieldSeperator);
		
		appendHistory(profile.getHistory(),fieldSeperator,ret);
		
		return ret.toString();
	}
	
	private static void appendHistory(History history, String fieldSeperator, StringBuilder ret) {
		ret.append(history.getWins()+fieldSeperator);
		ret.append(history.getLosses()+fieldSeperator);
		ret.append(history.getDraws()+fieldSeperator);
		ret.append(history.getGamesPlayed()+fieldSeperator);
	}
}
