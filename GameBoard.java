
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

	

}
