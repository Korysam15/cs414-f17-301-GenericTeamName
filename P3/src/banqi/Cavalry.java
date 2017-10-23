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
			super.icon="\u2462";
		}
		else{
			super.icon="\u24ED";
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
