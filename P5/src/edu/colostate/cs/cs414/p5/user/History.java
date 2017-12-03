package edu.colostate.cs.cs414.p5.user;



/**
 * @author Nick Wilson
 * 
 */
public class History {
	private int wins, losses, draws, gamesPlayed;
	/**
	 * History constructor;
	 */
	public History(){
		this.wins = 0;
		this.losses = 0;
		this.gamesPlayed = 0;
		this.draws = 0;
	}
	
	public void addDraw(){
		this.draws++;
		this.gamesPlayed++;
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
	 * @return the number of draws for this user
	 */
	public int getDraws() {
		return this.draws;
	}
	/**
	 * needs formatting to two decimals somehow
	 * @return the ratio of wins to games played
	 */
	public double getWinRatio(){
		Double winRatio = new Double((double)this.wins / (double)this.gamesPlayed);
		if(winRatio.isNaN())
		{
			return 0;
		}
		else
		{
			return winRatio;
		}		
	}
	/**
	 * needs formatting to two decimals somehow
	 * @return the ratio of losses to games played
	 */
	public double getLossRatio(){
		Double lossRatio = new Double((double)this.losses / (double)this.gamesPlayed);
		if(lossRatio.isNaN())
		{
			return 0;
		}
		else
		{
			return lossRatio;
		}
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
		ret += "Draws: " + this.draws + "\n";
		ret += "Games Played: "+this.gamesPlayed;
		return ret;
	}
	
	@Override
	public int hashCode() {
		return toString().hashCode();
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == null || !(o instanceof History)) {
			return false;
		} else {
			return (this.hashCode() == o.hashCode());
		}
	}
}