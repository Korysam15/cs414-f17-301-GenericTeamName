package edu.colostate.cs.cs414.p5.banqi;

public class GameOverException extends Exception {
	private String winner;

	public GameOverException(String winner)
	{
		this.winner = winner;
	}
	
	public String getWinner()
	{
		return this.winner;
	}
}
