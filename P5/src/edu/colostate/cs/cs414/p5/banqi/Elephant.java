package edu.colostate.cs.cs414.p5.banqi;
/**
 * @author Sam Maxwell
 *
 */
public class Elephant extends Piece {
	public static final int RANK=5;
	public Elephant(boolean color) {
		super.rank=RANK;
		super.color=color;
		if(color){
			super.icon="R5";
		}
		else{
			super.icon="B5";
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
}
