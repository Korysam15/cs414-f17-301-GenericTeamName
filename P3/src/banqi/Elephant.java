package banqi;

public class Elephant extends Piece {

	public Elephant(boolean color) {
		super.rank=5;
		super.color=color;
		super.isOn=isOn;
		if(color){
			super.icon="E ";
		}
		else{
			super.icon="e";
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
}
