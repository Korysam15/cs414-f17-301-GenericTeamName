package banqi;

public class Cavalry extends Piece {

	public Cavalry(boolean color) {
		super.rank=3;
		super.color=color;
		super.isOn=isOn;
		if(color){
			super.icon="\u2657 ";
		}
		else{
			super.icon="\u265D ";
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
