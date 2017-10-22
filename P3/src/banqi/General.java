package banqi;

public class General extends Piece {

	public General(boolean color) {
		super.rank=7;
		super.color=color;
		super.isOn=isOn;
		if(color){
			super.icon="\u001B[31m \u2654 ";
		}
		else{
			super.icon="\u265A ";
		}
		
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
