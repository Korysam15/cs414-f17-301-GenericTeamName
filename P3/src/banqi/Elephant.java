package banqi;
/**
 * @author Sam Maxwell
 *
 */
public class Elephant extends Piece {

	public Elephant(boolean color) {
		super.rank=5;
		super.color=color;
		super.isOn=isOn;
		if(color){
			super.icon="\u001b[1;31m5\u001b[0m";
		}
		else{
			super.icon="5";
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
}
