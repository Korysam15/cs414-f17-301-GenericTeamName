package edu.colostate.cs.cs414.p3.banqi;
/**
 * @author Sam Maxwell
 *
 */
public class Soldier extends Piece {

	public Soldier(boolean color) {
		super.rank=1;
		super.color=color;
		super.isOn=isOn;
		if(color){
			super.icon="\u2460";
		}
		else{
			super.icon="\u24EB";
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
