
/**
 * @author Nick Wilson
 * 
 */
public class History {
	private int wins, losses, gamesPlayed;
	/**
	 * History constructor;
	 */
	public History(){
		this.wins = 0;
		this.losses = 0;
		this.gamesPlayed = 0;
	}
	/**
	 * Resets all stats for this History
	 */
	public void reset(){
		this.wins = 0;
		this.losses = 0;
		this.gamesPlayed = 0;
	}
	/**
	 * Increments the win and game counter
	 */
	public void addWin(){
		this.wins++;
		this.gamesPlayed++;
	}
	/**
	 * Increments the loss and game counter
	 */
	public void addLoss(){
		this.losses++;
		this.gamesPlayed++;
	}
	/**
	 * @return the number of wins for this user
	 */
	public int getWins(){
		return this.wins;
	}
	/**
	 * @return the number of losses for this user
	 */
	public int getLosses(){
		return this.losses;
	}
	/**
	 * needs formatting to two decimals somehow
	 * @return the ratio of wins to games played
	 */
	public double getWinRatio(){
		return (double)this.wins / (double)this.gamesPlayed;
	}
	/**
	 * needs formatting to two decimals somehow
	 * @return the ratio of losses to games played
	 */
	public double getLossRatio(){
		return (double)this.losses / (double)this.gamesPlayed;
	}
	/**
	 * @return the number of games recorded for this History object
	 */
	public int getGamesPlayed(){
		return this.gamesPlayed;
	}
	
	public String toString(){
		String ret = "";
		ret += "Wins: "+this.wins + "\t\tWin Ratio: " + this.getWinRatio();
		ret += "\n";
		ret += "Losses: "+this.losses + "\tLoss Ratio: "+this.getLossRatio();
		ret += "\n";
		ret += "Games Played: "+this.gamesPlayed;
		return ret;
	}
}
