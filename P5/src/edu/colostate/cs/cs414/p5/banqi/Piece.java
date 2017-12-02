package edu.colostate.cs.cs414.p5.banqi;

import org.lwjgl.util.vector.Vector3f;

import edu.colostate.cs.cs414.p5.gui.entities.Entity;

/**
 * @author Sam Maxwell
 *
 */
public abstract class Piece {
	protected Entity entity;
	protected int rank;   				// rank of the piece (1-7)
	
	protected boolean faceUp=false;     //whether or not the piece is face up
	protected boolean color;			//color of the piece (red = true    black = false)
	protected String icon;              // Icon is based on rank and color(using ANSI escape console: https://en.wikipedia.org/wiki/ANSI_escape_code#Escape_sequences)
	protected int position;
	
//	public Piece(int position) {
//		this.position=position;
//		
//	}
	
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
	public boolean isRed() {
		return isColor();
	}
	public boolean isBlack() {
		return !isRed();
	}
	public String getIcon() {
		return icon;
	}
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Piece)
		{
			Piece s=(Piece) obj;
			if(s.faceUp==faceUp&&s.rank==rank) {
				return true;
			}
			
		}
		return false ;
	}
	
	//public Vector3f getPosition() {
		
		
		
	//}

}
