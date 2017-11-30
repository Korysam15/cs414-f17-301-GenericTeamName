package edu.colostate.cs.cs414.p5.banqi;
/**
 * @author Sam Maxwell
 *
 */
public class Soldier extends Piece {
	public static final int RANK = 1;
	public Soldier(boolean color) {
		super.rank=RANK;
		super.color=color;
		if(color){
			super.icon="R1";
		}
		else{
			super.icon="B1";
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
