package banqi;

public class Cannon extends Piece {

	public Cannon(boolean color) {
		super.rank=2;
		super.color=color;
		super.isOn=isOn;
		if(color){
			super.icon="\u2656 ";
		}
		else{
			super.icon="\u265C ";
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
