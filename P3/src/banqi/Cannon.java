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
			super.icon="\u2461";
		}
		else{
			super.icon="\u24EC";
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
