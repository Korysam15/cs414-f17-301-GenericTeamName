package edu.colostate.cs.cs414.p3.banqi;
/**
 * @author Sam Maxwell
 *
 */
public class Elephant extends Piece {

	public Elephant(boolean color) {
		super.rank=5;
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
