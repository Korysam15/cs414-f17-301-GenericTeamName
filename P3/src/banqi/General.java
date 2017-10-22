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
			super.icon="\u001b[1;31m7\u001b[0m";
		}
		else{
			super.icon="7";
		}
		
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
