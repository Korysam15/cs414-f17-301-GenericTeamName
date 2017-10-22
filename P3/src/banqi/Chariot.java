package banqi;

public class Chariot extends Piece {

	public Chariot(boolean color) {
		super.rank=4;
		super.color=color;
		super.isOn=isOn;
		if(color){
			super.icon="\u2658 ";
		}
		else{
			super.icon="\u265E ";
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
