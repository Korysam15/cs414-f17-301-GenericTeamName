package edu.colostate.cs.cs414.p3.banqi;
/**
 * @author Sam Maxwell
 *
 */
public class Elephant extends Piece {

	public Elephant(boolean color) {
		super.rank=5;
		super.color=color;
		super.isOn=isOn;
		if(color){
			super.icon="\u2464";
		}
		else{
			super.icon="\u24EF";
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
}
