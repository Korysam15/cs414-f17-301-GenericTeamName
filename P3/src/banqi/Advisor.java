package banqi;

/**
 * @author Sam Maxwell
 *
 */
public class Advisor extends Piece {

	public Advisor(boolean color) {
		super.rank=6;
		super.color=color;
		if(color){
			super.icon="\u2465";
		}
		else{
			super.icon="\u24F0";
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
