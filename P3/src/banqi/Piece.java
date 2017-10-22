package banqi;
/**
 * @author Sam Maxwell
 *
 */
public abstract class Piece {
	
	protected int rank;   				// rank of the piece (1-7)
	protected Square isOn;  			// the square that the piece is on
	protected boolean faceUp=false;     //whether or not the piece is face up
	protected boolean color;			//color of the piece (red = true    black = false)
	protected String icon;              // Icon is based on rank and color(using ANSI escape console: https://en.wikipedia.org/wiki/ANSI_escape_code#Escape_sequences)
	
	
	
	@Override
	public String toString() {
		return this.getClass()+" [rank=" +rank  +  ", faceUp=" + faceUp + ", color=" + color + "]";
	}
	
	public void flipPiece()
	{
		faceUp=true;
	}

	

}
