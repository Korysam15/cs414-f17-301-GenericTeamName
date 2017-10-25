package edu.colostate.cs.cs414.p3.banqi;
/**
 * @author Sam Maxwell
 *
 */
public class General extends Piece {

	public General(boolean color) {
		super.rank=7;
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
