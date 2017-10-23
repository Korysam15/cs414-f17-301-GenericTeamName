package banqi;
import java.io.IOException;
import java.util.ArrayList;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import client_server.transmission.FlipPieceTask;
import client_server.transmission.ForfeitTask;
import client_server.transmission.ForwardTask;
import client_server.transmission.MoveTask;
import client_server.transmission.Task;
import client_server.transmission.UpdateRecordTask;
import console.JavaConsole;
import user.Player;

/**
 * @author Sam Maxwell
 *
 */
public class BanqiGame {


	private JavaConsole console;
	private int gameID;            // unique id
	private GameBoard gameBoard;   // board that the game is played on
	private Piece pieces[];        // pieces in game
	private Player first;		   // first Player
	private Player second;		   // second Player
	private boolean test=false;	   // TESTING


	public static void main(String []args) throws IOException{
		Player first = new Player("Sam","alpha","maycellman","127.0.0.01",8080);
		Player second = new Player("Sam","alpha","maycellman","127.0.0.01",8080);
		BanqiGame game= new BanqiGame(1,first,second,true);
		for(Square s: game.gameBoard.getSquaresOnBoard()){
			System.out.println(s);
		}

		System.out.println(game.gameBoard);
		game.makeMove(game.getSquare("A1"));
		System.out.println(game.gameBoard);
		game.makeMove(game.getSquare("A2"));
		System.out.println(game.gameBoard);
		game.makeMove(game.getSquare("A1"),game.getSquare("A2"));
		System.out.println(game.gameBoard);
		game.makeMove(game.getSquare("B2"));
		System.out.println(game.gameBoard);
		game.makeMove(game.getSquare("B1"));
		System.out.println(game.gameBoard);
		game.makeMove(game.getSquare("C1"));
		System.out.println(game.gameBoard);
		game.makeMove(game.getSquare("C2"));
		System.out.println(game.gameBoard);
		game.makeMove(game.getSquare("C3"));
		System.out.println(game.gameBoard);
		game.makeMove(game.getSquare("C4"));
		System.out.println(game.gameBoard);
		game.makeMove(game.getSquare("B4"));
		System.out.println(game.gameBoard);
		game.makeMove(game.getSquare("B3"));
		System.out.println(game.gameBoard);
		game.makeMove(game.getSquare("B2"),game.getSquare("B3"));
		System.out.println(game.gameBoard);
		game.makeMove(game.getSquare("B4"),game.getSquare("B3"));
		System.out.println(game.gameBoard);
		game.makeMove(game.getSquare("D4"));
		System.out.println(game.gameBoard);
		game.makeMove(game.getSquare("D3"));
		System.out.println(game.gameBoard);
		game.makeMove(game.getSquare("D2"));
		System.out.println(game.gameBoard);
		game.makeMove(game.getSquare("D1"));
		System.out.println(game.gameBoard);
		game.makeMove(game.getSquare("C1"),game.getSquare("D1"));
		System.out.println(game.gameBoard);
		game.makeMove(game.getSquare("D1"),game.getSquare("D2"));
		System.out.println(game.gameBoard);


	}

	public BanqiGame(int gameID) {
		super();
		this.gameID = gameID;
		this.gameBoard= new GameBoard();
		this.pieces= new Piece[32];
		console = new JavaConsole();
		console.setTitle(this.first.getNickName());
		getAllPieces();

	}

	public BanqiGame(int gameID,Player first, Player second) {

		this.gameID = gameID;
		this.gameBoard= new GameBoard();
		this.pieces= new Piece[32];
		this.first=first;             
		this.second=second;
		console = new JavaConsole();
		console.setTitle(this.first.getNickName());
		getAllPieces();

	}
	public BanqiGame(int gameID,Player first, Player second, boolean test) {


		this.gameID = gameID;
		this.gameBoard= new GameBoard();
		this.pieces= new Piece[32];
		this.test=test;
		console = new JavaConsole();
//		console.setTitle(this.first.getNickName());
		getAllPieces();


	}

	private  void getAllPieces() {

		List<Integer> list = new ArrayList<Integer>();  // create list of 0-31
		for(int i=0;i<32;i++){
			list.add(i);
		}


		if(test){

			Collections.shuffle(list,new Random(123)); 
		}
		else{
			Collections.shuffle(list,new Random(gameID));                      //randomize list
		}



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


	public  boolean makeMove(Square from, Square to){
		if(from==null||to==null)
		{
			System.out.println("Not a valid move");
			return false;
		}
		if(from.getOn()==null){
			System.out.println("There is no piece there");
			return false;
		}
		if(from.getOn().faceUp==false){
			System.out.println("This piece must be flipped first");
			return false;
		}
		if(getValidMoves(from).contains(to)){        //check if the move is valid

			to.setOn(from.getOn());
			from.setOn(null);
			return true;
		}
		System.out.println("Not a valid move");
		return false;




	}
	public boolean makeMove(Square from){
		if (from==null)
		{
			System.out.println("Not a valid move");
			return false;
		}
		if(from.getOn()==null){
			System.out.println("There is no piece there");
			return false;
		}
		if(from.getOn().faceUp==false)
		{
			from.getOn().flipPiece();
			return true;
		}

		return false;

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
			int x = from.getX();
			int y = from.getY();
			while (x > 1){
				x--;
				Square next = gameBoard.getSquare(x, y);
				if(!next.isEmpty()){
					next = gameBoard.getSquare(x-1, y);
					if(!next.isEmpty()&&canOverTake(from,next)){
						validMoves.add(next);
						break;
					}
				}
			}
			x = from.getX();
			while (x < 6){
				x++;
				Square next = gameBoard.getSquare(x, y);
				if(!next.isEmpty()){
					next = gameBoard.getSquare(x+1, y);
					if(!next.isEmpty()&&canOverTake(from,next)){
						validMoves.add(next);
						break;
					}
				}
			}
			while (y > 1){
				y--;
				Square next = gameBoard.getSquare(x, y);
				if(!next.isEmpty()){
					next = gameBoard.getSquare(x, y-1);
					if(!next.isEmpty()&&canOverTake(from,next)){
						validMoves.add(next);
						break;
					}
				}
			}
			y = from.getY();
			while (y < 2){
				y++;
				Square next = gameBoard.getSquare(x, y);
				if(!next.isEmpty()){
					next = gameBoard.getSquare(x, y+1);
					if(!next.isEmpty()&&canOverTake(from,next)){
						validMoves.add(next);
						break;
					}
				}
			}
		}
		//else{
			if(from.getY()!=0){
				Square up =gameBoard.getSquare(from.getX(), from.getY()-1);
				if(up.isEmpty()||canOverTake(from,up)){   //check square above

					validMoves.add(up);
				}
			}
			if(from.getY()!=3){
				Square down =gameBoard.getSquare(from.getX(), from.getY()+1);
				if(down.isEmpty()||canOverTake(from,down)){  //check square below

					validMoves.add(down);
				}

			}
			if(from.getX()!=0){
				Square left =gameBoard.getSquare(from.getX()-1, from.getY());
				if(left.isEmpty()||canOverTake(from,left)){  //check square to the left

					validMoves.add(left);
				}

			}
			if(from.getX()!=7){
				Square right =gameBoard.getSquare(from.getX()+1, from.getY());
				if(right.isEmpty()||canOverTake(from,right)){  //check square to the right

					validMoves.add(right);
				}
			}

		//}


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

	public void promptTurn(Player p){
		System.out.println("Your Turn!");
		System.out.println(gameBoard);
		while(true)
		{

			System.out.println("Make a move!  ex. A1");
			Scanner scanner = new Scanner(System.in);
			String in1 = scanner.nextLine();
			if(in1.length()>2){
				System.out.println("Invalid Move");
			}
			Square from =getSquare(in1);

			if(from.isEmpty()){
				System.out.println("Invalid Move - No piece at: "+in1);
			}
			else if(!from.getOn().faceUp){
				flipPiece(from);
				System.out.println(gameBoard);
				break;
			}
			else{
				System.out.println("to");


				String in2 = scanner.nextLine();
				if(in2.length()>2){
					System.out.println("Invalid Move");
				}
				Square to =getSquare(in2);
				if(makeMove(from,to)){



					System.out.println(gameBoard);
					break;
				}
				System.out.println("Invalid Move - "+from.getOn().getClass()+" can't move like that");

			}
			//check for the right color 


		}

	}

	public void promptTurn(Player p, String otherPlayer){
		System.out.println("Your Turn!");
		System.out.println(gameBoard);
		Task notify = null;
		while(true)
		{
			System.out.println("Type 'forfeit' to forfeit a match or 'help' to get help.");
			System.out.println("Make a move!  ex. A1");
			Scanner scanner = new Scanner(System.in);
			String in1 = scanner.nextLine();
			if(in1.toLowerCase().equals("forfeit"))
			{
				System.out.print("Are you sure you want to forfiet? Type 'yes' to confirm or 'no' to keep playing: ");
				String choice = scanner.next();
				if(choice.toLowerCase().equals("yes"))
				{
					System.out.println("FORFEITING");
					// forfeit game, create forfiet task, update stats for both players
					ForfeitTask forfeit = new ForfeitTask(this.gameID, new UpdateRecordTask(false,true,false),p.getNickName() + " has forfeited! " + otherPlayer + " is the winner!");
					forfeit.run();
					forfeit.getUpdateRecordTask().setWon(true);
					forfeit.getUpdateRecordTask().setLoss(false);
					try {
						p.getClient().sendToServer(new ForwardTask(p.getNickName(),forfeit,otherPlayer));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				}
				if(choice.toLowerCase().equals("no"))
				{
					continue;
				}
			}
			else if(in1.toLowerCase().equals("help"))
			{
				System.out.println("Below is a list of each piece with their associated number that represents that piece.");
				System.out.println("White General: \u2466 \t\t Black General: \u2F41" 
				+ "White Advisor: \u2465 \t\t Black Advisor: \u24F0" 
				+ "White Elephant: \u2464 \t\t Black Elephant: \u24EF" 
				+ "White Chariot: \u2463 \t\t Black Chariot: \u24EE"
				+ "White Cavalry: \u2462 \t\t Black Cavalry: \u24ED"
				+ "White Cannon: \u2461 \t\t Black Cannon: \u24EC"
				+ "White Soldier: \u2460 \t\t Black Soldier: \u24EB");
				
				System.out.println("7: General\n6: Advisor\n5: Elephant\n4: Chariot\n3: Cavalry\n2: Soldier\n1: Cannon");
				System.out.println("Rules:\n Only pieces of equal or lower rank may be captured. However, A Cannon can capture any piece and can be captured by every piece except a Soldier.\n");
			}
			else
			{
				if(in1.length()>2){
					System.out.println("Invalid Move");
				}
				Square from =getSquare(in1);

				if(from.isEmpty()){
					System.out.println("Invalid Move - No piece at: "+in1);
				}
				else if(!from.getOn().faceUp){
					flipPiece(from);
					System.out.println(gameBoard);
					notify = new FlipPieceTask(p.getNickName(),this.gameID,from);
					break;

				}
				else{
					System.out.println("to");


					String in2 = scanner.nextLine();
					if(in2.length()>2){
						System.out.println("Invalid Move");
					}
					Square to =getSquare(in2);
					if(makeMove(from,to)){
						System.out.println(gameBoard);
						notify = new MoveTask(p.getNickName(),this.gameID,from,to);
						break;
					}
					System.out.println("Invalid Move - "+from.getOn().getClass()+" can't move like that");

				}
			}
			//check for the right color 
		}
		if(notify != null) {
			Task forward = new ForwardTask(p.getNickName(),notify,otherPlayer);
			try {
				p.getClient().sendToServer(forward);
			} catch (IOException e) {
			}
		}

	}

	public Square getSquare(String from){

		from=from.toUpperCase();
		char letter= from.charAt(0);
		char num=from.charAt(1);

		int x=(Character.getNumericValue(letter)-10);
		int y = num-49;

		Square s=gameBoard.getSquare(x, y);

		return s;







	}
	public Square getSquare(int x, int y) {
		return gameBoard.getSquare(x, y);
	}





}