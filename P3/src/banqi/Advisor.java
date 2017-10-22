package banqi;


public class Advisor extends Piece {

	public Advisor(boolean color) {
		super.rank=6;
		super.color=color;
		if(color){
			super.icon="\u2655 ";
		}
		else{
			super.icon="\u265B ";
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
