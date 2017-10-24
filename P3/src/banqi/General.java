package banqi;
/**
 * @author Sam Maxwell
 *
 */
public class General extends Piece {

	public General(boolean color) {
		super.rank=7;
		super.color=color;
		super.isOn=isOn;
		if(color){
			super.icon="\u2466";
		}
		else{
			super.icon="\u24F1";
		}
		
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
