package banqi;
/**
 * @author Sam Maxwell
 *
 */
public class Cannon extends Piece {

	public Cannon(boolean color) {
		super.rank=2;
		super.color=color;
		super.isOn=isOn;
		if(color){
			super.icon="\u001b[1;31m2\u001b[0m";
		}
		else{
			super.icon="2";
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
