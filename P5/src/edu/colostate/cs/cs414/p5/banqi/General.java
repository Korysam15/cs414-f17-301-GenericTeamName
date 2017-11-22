package edu.colostate.cs.cs414.p5.banqi;
/**
 * @author Sam Maxwell
 *
 */
public class General extends Piece {
	public static final int RANK=7;
	public General(boolean color) {
		super.rank=RANK;
		super.color=color;
		if(color){
			super.icon="R7";
		}
		else{
			super.icon="B7";
		}
		
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
