package banqi;
/**
 * @author Sam Maxwell
 *
 */
public class Soldier extends Piece {

	public Soldier(boolean color) {
		super.rank=1;
		super.color=color;
		super.isOn=isOn;
		if(color){
			super.icon="\u001b[1;31m1\u001b[0m";
		}
		else{
			super.icon="1";
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
