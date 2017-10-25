package edu.colostate.cs.cs414.p4.banqi;
/**
 * @author Sam Maxwell
 *
 */
public abstract class Piece {
	
	protected int rank;   				// rank of the piece (1-7)
	
	protected boolean faceUp=false;     //whether or not the piece is face up
	protected boolean color;			//color of the piece (red = true    black = false)
	protected String icon;              // Icon is based on rank and color(using ANSI escape console: https://en.wikipedia.org/wiki/ANSI_escape_code#Escape_sequences)
	
	
	
	public void flipPiece()
	{
		faceUp=true;
	}

	public int getRank() {
		return rank;
	}

	public boolean isFaceUp() {
		return faceUp;
	}
	public boolean isColor() {
		return color;
	}
	public String getIcon() {
		return icon;
	}

}
