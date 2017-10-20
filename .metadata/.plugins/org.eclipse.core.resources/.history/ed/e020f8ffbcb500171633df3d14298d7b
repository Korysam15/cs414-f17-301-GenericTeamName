import java.util.ArrayList;

import java.util.Collections;
import java.util.List;

public class BanqiGame {

	private int gameID;            // unique id
	private GameBoard gameBoard;   // board that the game is played on
	private Piece pieces[];        // pieces in game

	public static void main(String []args){

		BanqiGame game= new BanqiGame(1);
		for(Square s: game.gameBoard.getSquaresOnBoard()){
			System.out.println(s);
		}

		System.out.println(game.gameBoard);

		//System.out.println(game.isvalidMove(game.gameBoard.getSquaresOnBoard()[16],game.gameBoard.getSquaresOnBoard()[8]));  //true
		//System.out.println(game.isvalidMove(game.gameBoard.getSquaresOnBoard()[16],game.gameBoard.getSquaresOnBoard()[9]));  //false


	}
	public BanqiGame(int gameID) {
		super();
		this.gameID = gameID;
		this.gameBoard= new GameBoard();
		this.pieces= new Piece[32];
		getAllPieces();

	}

	private  void getAllPieces() {

		List<Integer> list = new ArrayList<Integer>();  // create list of 0-31
		for(int i=0;i<32;i++){
			list.add(i);
		}

		Collections.shuffle(list);                      //randomize list


		//red pieces
		pieces[0]=new General(true);									// x1 General
		gameBoard.getSquaresOnBoard()[list.get(0)].setOn(pieces[0]);    

		pieces[1]=new Advisor(true);									// x2 Advisor
		gameBoard.getSquaresOnBoard()[list.get(1)].setOn(pieces[1]);
		pieces[2]=new Advisor(true);
		gameBoard.getSquaresOnBoard()[list.get(2)].setOn(pieces[2]);


		pieces[3]=new Elephant(true);									// x2 Elephant
		gameBoard.getSquaresOnBoard()[list.get(3)].setOn(pieces[3]);	
		pieces[4]=new Elephant(true);
		gameBoard.getSquaresOnBoard()[list.get(4)].setOn(pieces[4]);

		pieces[5]=new Chariot(true);									// x2 Chariot
		gameBoard.getSquaresOnBoard()[list.get(5)].setOn(pieces[5]);
		pieces[6]=new Chariot(true);
		gameBoard.getSquaresOnBoard()[list.get(6)].setOn(pieces[6]);

		pieces[7]=new Cavalry(true);									// x2 Cavalry
		gameBoard.getSquaresOnBoard()[list.get(7)].setOn(pieces[7]);
		pieces[8]=new Cavalry(true);
		gameBoard.getSquaresOnBoard()[list.get(8)].setOn(pieces[8]);

		pieces[9]=new Cannon(true);										// x2 Cannon
		gameBoard.getSquaresOnBoard()[list.get(9)].setOn(pieces[9]);
		pieces[10]=new Cannon(true);
		gameBoard.getSquaresOnBoard()[list.get(10)].setOn(pieces[10]);

		pieces[11]=new Soldier(true);									// x5 Soldier
		gameBoard.getSquaresOnBoard()[list.get(11)].setOn(pieces[11]);
		pieces[12]=new Soldier(true);
		gameBoard.getSquaresOnBoard()[list.get(12)].setOn(pieces[12]);
		pieces[13]=new Soldier(true);
		gameBoard.getSquaresOnBoard()[list.get(13)].setOn(pieces[13]);
		pieces[14]=new Soldier(true);
		gameBoard.getSquaresOnBoard()[list.get(14)].setOn(pieces[14]);
		pieces[15]=new Soldier(true);
		gameBoard.getSquaresOnBoard()[list.get(15)].setOn(pieces[15]);


		//black pieces
		pieces[16]=new General(false);
		gameBoard.getSquaresOnBoard()[list.get(16)].setOn(pieces[16]);

		pieces[17]=new Advisor(false);
		gameBoard.getSquaresOnBoard()[list.get(17)].setOn(pieces[17]);
		pieces[18]=new Advisor(false);
		gameBoard.getSquaresOnBoard()[list.get(18)].setOn(pieces[18]);

		pieces[19]=new Elephant(false);
		gameBoard.getSquaresOnBoard()[list.get(19)].setOn(pieces[19]);
		pieces[20]=new Elephant(false);
		gameBoard.getSquaresOnBoard()[list.get(20)].setOn(pieces[20]);

		pieces[21]=new Chariot(false);
		gameBoard.getSquaresOnBoard()[list.get(21)].setOn(pieces[21]);
		pieces[22]=new Chariot(false);
		gameBoard.getSquaresOnBoard()[list.get(22)].setOn(pieces[22]);

		pieces[23]=new Cavalry(false);
		gameBoard.getSquaresOnBoard()[list.get(23)].setOn(pieces[23]);
		pieces[24]=new Cavalry(false);
		gameBoard.getSquaresOnBoard()[list.get(24)].setOn(pieces[24]);

		pieces[25]=new Cannon(false);
		gameBoard.getSquaresOnBoard()[list.get(25)].setOn(pieces[25]);
		pieces[26]=new Cannon(false);
		gameBoard.getSquaresOnBoard()[list.get(26)].setOn(pieces[26]);

		pieces[27]=new Soldier(false);
		gameBoard.getSquaresOnBoard()[list.get(27)].setOn(pieces[27]);
		pieces[28]=new Soldier(false);
		gameBoard.getSquaresOnBoard()[list.get(28)].setOn(pieces[28]);
		pieces[29]=new Soldier(false);
		gameBoard.getSquaresOnBoard()[list.get(29)].setOn(pieces[29]);
		pieces[30]=new Soldier(false);
		gameBoard.getSquaresOnBoard()[list.get(30)].setOn(pieces[30]);
		pieces[31]=new Soldier(false);
		gameBoard.getSquaresOnBoard()[list.get(31)].setOn(pieces[31]);





	}




	/*Check every time whether the piece clicked is faceUp 
	 * or
	 * Have the server know.
	 * If the piece clicked is face up wait for another click then call isvalidMove. 
	 * If the piece clicked is face down immediately call flipPiece  */



	public  void movePiece(Square from, Square to){


		if(getValidMoves(from).contains(to)){        //check if the move is valid

			to.setOn(from.getOn());
			from.setOn(null);
		}






	}
	
	public boolean flipPiece(Square from){

		if(from.getOn().faceUp==false)
		{
			from.getOn().flipPiece();
			return true;
		}
		return false;

	}

	public ArrayList<Square> getValidMoves(Square from){


		ArrayList<Square> validMoves= new ArrayList<Square>();


		if(from.isEmpty()){
			return validMoves;
		}

		if(from.getOn() instanceof Cannon){

			//Canon Stuff

		}
		else{

			Square up =gameBoard.getSquare(from.getX(), from.getY()-1);
			if(from.getY()!=0&&(up.isEmpty()||canOverTake(from,up))){   //check square above

				validMoves.add(up);
			}


			Square down =gameBoard.getSquare(from.getX(), from.getY()+1);
			if(from.getY()!=3&&(down.isEmpty()||canOverTake(from,down))){  //check square below

				validMoves.add(down);
			}


			Square left =gameBoard.getSquare(from.getX()-1, from.getY());
			if(from.getX()!=0&&(left.isEmpty()||canOverTake(from,left))){  //check square to the left

				validMoves.add(left);
			}


			Square right =gameBoard.getSquare(from.getX()+1, from.getY());
			if(from.getX()!=7&&(right.isEmpty()||canOverTake(from,right))){  //check square to the right

				validMoves.add(right);
			}


		}


		return validMoves;
	}

	public boolean canOverTake(Square from, Square to)
	{
		if(from.getOn().faceUp&&to.getOn().faceUp){     //make sure both pieces are face up

			if(from.getOn() instanceof Soldier && to.getOn() instanceof General){ //soldier can overTake general
				return true;
			}


			return from.getOn().rank>=to.getOn().rank;
		}
		return false;

	}





}
