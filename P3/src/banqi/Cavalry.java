package banqi;
/**
 * @author Sam Maxwell
 *
 */
public class Cavalry extends Piece {

	public Cavalry(boolean color) {
		super.rank=3;
		super.color=color;
		super.isOn=isOn;
		if(color){
			super.icon="\u001b[1;31m3\u001b[0m";
		}
		else{
			super.icon="3";
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
