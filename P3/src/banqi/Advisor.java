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
			super.icon="\u001b[1;31m6\u001b[0m";
		}
		else{
			super.icon="6";
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
