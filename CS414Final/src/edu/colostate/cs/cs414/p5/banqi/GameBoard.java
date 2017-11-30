package edu.colostate.cs.cs414.p5.banqi;

import java.util.ArrayList;

import org.lwjgl.util.vector.Vector3f;

import edu.colostate.cs.cs414.p5.gui.entities.Entity;
import edu.colostate.cs.cs414.p5.gui.models.TexturedModel;
import edu.colostate.cs.cs414.p5.gui.objconverter.OBJFileLoader;
import edu.colostate.cs.cs414.p5.gui.renderengine.Loader;
import edu.colostate.cs.cs414.p5.gui.textures.ModelTexture;

/**
 * @author Sam Maxwell
 *
 */
public class GameBoard {

	private Square squaresOnBoard[]; 


	public Square getSquare(int i) {
		System.out.println();
		return squaresOnBoard[i];
	}

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

		String board = "        A        B        C        D        E        F        G        H  \n";
		for(int i=0;i<4;i++){

			board+="    ######## ######## ######## ######## ######## ######## ######## ########\n";
			board+="   #        #        #        #        #        #        #        #        #\n";
			board+=i+1+"  ";

			for(int j=0;j<8;j++){

				if(squaresOnBoard[8*i+j].getOn()==null){

					board+="#        ";

				}
				else if(!squaresOnBoard[8*i+j].getOn().faceUp){
					board+="#   "+"00"+"   ";
				}
				else {

					board+="#   "+squaresOnBoard[8*i+j].getOn().icon+"   ";
					

				}
				

			}
			board+="#\n   #        #        #        #        #        #        #        #        #\n";



		}
		board+="    ######## ######## ######## ######## ######## ######## ######## ########\n";
		return board;
	}

	public ArrayList<Entity> generateBoardModel(Loader loader) {
		System.out.println("here");

		ArrayList<Entity> board = new ArrayList<Entity>(); 
		TexturedModel whiteSquare = new TexturedModel(OBJFileLoader.loadOBJ("square", loader),
				new ModelTexture(loader.loadTexture("square_white")));
		TexturedModel blackSquare = new TexturedModel(OBJFileLoader.loadOBJ("square", loader),
				new ModelTexture(loader.loadTexture("square_black")));
		
		
		for(int i= -30; i<50;i+=10) {




			if(Math.abs(i/10)%2==1) {
				Entity squareEntity = new Entity(blackSquare, new Vector3f(i, -25, -90), 0, 0, 0, 1f);

				board.add(squareEntity);


			}
			else {
				Entity BsquareEntity = new Entity(whiteSquare, new Vector3f(i, -25, -90), 0, 0, 0, 1f);

				board.add(BsquareEntity);



			}
		}	
		for(int i= -30; i<50;i+=10) {




			if(Math.abs(i/10)%2==1) {
				Entity BsquareEntity = new Entity(whiteSquare, new Vector3f(i, -25, -80), 0, 0, 0, 1f);

				board.add(BsquareEntity);

			}
			else {
				Entity squareEntity = new Entity(blackSquare, new Vector3f(i, -25, -80), 0, 0, 0, 1f);

				board.add(squareEntity);


			}
		}
		
		for(int i= -30; i<50;i+=10) {




			if(Math.abs(i/10)%2==1) {
				Entity squareEntity = new Entity(blackSquare, new Vector3f(i, -25, -70), 0, 0, 0, 1f);

				board.add(squareEntity);
			}
			else {
				Entity BsquareEntity = new Entity(whiteSquare, new Vector3f(i, -25, -70), 0, 0, 0, 1f);

				board.add(BsquareEntity);

			}
		}
		
		
		
		
		
		


		for(int i= -30; i<50;i+=10) {




			if(Math.abs(i/10)%2==1) {
				Entity squareEntity = new Entity(whiteSquare, new Vector3f(i, -25, -60), 0, 0, 0, 1f);

				board.add(squareEntity);
			}
			else {
				Entity BsquareEntity = new Entity(blackSquare, new Vector3f(i, -25, -60), 0, 0, 0, 1f);

				board.add(BsquareEntity);

			}

		}



	

		

		

		return board;


	}
//	public Piece getPiece(Vector3f position) {
//		
//
//		int square=0;
//
//		
//		if(position.x==-20) {
//
//			square+=1;
//
//		}
//		if(position.x==-10) {
//
//			square+=2;
//
//		}
//		if(position.x==0) {
//
//			square+=3;
//
//		}
//		if(position.x==10) {
//
//			square+=4;
//
//		}
//		if(position.x==20) {
//
//			square+=5;
//
//		}
//		if(position.x==40) {
//
//			square+=6;
//
//		}
//		if(position.x==50) {
//
//			square+=7;
//
//		}
//		if(position.z==-80) {
//			square+=8;
//		}
//		if(position.z==-70) {
//			square+=16;
//		}
//		if(position.z==-60) {
//			square+=24;
//		}
//		
//		return pieces[square];
//
//
//
//	}


}
