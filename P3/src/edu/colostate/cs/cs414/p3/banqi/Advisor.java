package edu.colostate.cs.cs414.p3.banqi;

/**
 * @author Sam Maxwell
 *
 */
public class Advisor extends Piece {

	public Advisor(boolean color) {
		super.rank=6;
		super.color=color;
		if(color){
			super.icon="<html>Uncolored Text! <font color=orange>Now some example Text with color!</font> more Uncolored Text!</html>";
		}
		else{
			super.icon="\u24F0";
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
