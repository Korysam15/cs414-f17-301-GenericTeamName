package banqi;
/**
 * @author Sam Maxwell
 *
 */
public class Chariot extends Piece {

	public Chariot(boolean color) {
		super.rank=4;
		super.color=color;
		super.isOn=isOn;
		if(color){
			super.icon="\u001b[1;31m4\u001b[0m";
		}
		else{
			super.icon="4";
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
