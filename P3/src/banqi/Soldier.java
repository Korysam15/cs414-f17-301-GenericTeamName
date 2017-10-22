package banqi;

public class Soldier extends Piece {

	public Soldier(boolean color) {
		super.rank=1;
		super.color=color;
		super.isOn=isOn;
		if(color){
			super.icon="\u2659 ";
		}
		else{
			super.icon="\u265F ";
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
