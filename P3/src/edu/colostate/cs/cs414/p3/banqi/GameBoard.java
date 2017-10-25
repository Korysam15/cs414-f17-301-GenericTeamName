package edu.colostate.cs.cs414.p3.banqi;
/**
 * @author Sam Maxwell
 *
 */
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
		

		if(y>3||x>7)
		{
			
			return null;
		}
		return squaresOnBoard[8*y+x];
	}

	@Override
	public String toString() {

		String board = "       A       B       C       D       E       F       G       H  \n";
		for(int i=0;i<4;i++){

			board+="   # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # \n";
			board+="   #       #       #       #       #       #       #       #       #\n";
			board+=i+1+"  ";

			for(int j=0;j<8;j++){

				if(squaresOnBoard[8*i+j].getOn()==null){

					board+="#       ";

				}
				else if(!squaresOnBoard[8*i+j].getOn().faceUp){
					board+="#   "+0+"   ";
				}
				else{

					board+="#   "+squaresOnBoard[8*i+j].getOn().icon+"   ";


				}

			}
			board+="#\n   #       #       #       #       #       #       #       #       #\n";



		}
		board+="   # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # \n";
		return board;
	}



}
