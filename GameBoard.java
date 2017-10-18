import java.util.Arrays;

public class GameBoard {

	private Square squaresOnBoard[]; 


	public GameBoard() {

		this.squaresOnBoard = createSquares() ;
	}

	public Square[] getSquaresOnBoard() {
		return squaresOnBoard;
	}
	public Square[] createSquares(){

		Square squares[]=new Square[32];

		for(int i=0;i<32;i++){
			squares[i]= new Square(i%8,i/8);
		}
		return squares;

	}
	public Square getSquare(int x,int y)
	{
		return squaresOnBoard[8*y+x];
	}

	@Override
	public String toString() {
		String board = null;
		for(int i=0;i<4;i++){
			board+="\n----------------------------------------\n";
			for(int j=0;j<8;j++){
				board+="| "+squaresOnBoard[8*i+j].getOn().rank+" |";
			}


		}
		board+="\n----------------------------------------";
		return board;
	}



}
