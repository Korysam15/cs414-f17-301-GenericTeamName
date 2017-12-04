package edu.colostate.cs.cs414.p5.banqi;

public class GameOverException extends Exception {
	private static final long serialVersionUID = 6800335378637297753L;
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
