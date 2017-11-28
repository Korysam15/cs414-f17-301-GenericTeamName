package edu.colostate.cs.cs414.p5.banqi;

import java.util.Arrays;

/**
 * 
 * @author pflagert
 *
 */
public class GameBuilder {
	
	private final String fieldSeperator;
	
	private static final String PIECE_IS_NULL = "empty";
	private static final String PIECE_IS_FACE_UP = "U";
	private static final String PIECE_IS_FACE_DOWN = "D";
	private static final String PIECE_IS_BLACK = "B";
	private static final String PIECE_IS_RED = "R";
	
	public GameBuilder(String fieldSeperator) {
		if(fieldSeperator == null) {
			throw new IllegalArgumentException("Field seperator can not be null");
		}
		this.fieldSeperator = fieldSeperator;
	}
	
	public BanqiGame createGameFromString(String line) {
		return createGameFromString(line,this.fieldSeperator);
	}
	
	public String gameToString(BanqiGame game) {
		return gameToString(game,this.fieldSeperator);
	}
	
	public static BanqiGame createGameFromString(String line, String fieldSeperator) {
		BanqiGame ret = null;
		String strings[] = line.split(fieldSeperator);
		int gameID = Integer.parseInt(strings[0]);
		String playerOne = strings[1];
		String playerTwo = strings[2];
		
		String boardStrings[] = Arrays.copyOfRange(strings, 3, strings.length);
		
		GameBoard board = createGameBoardFromStrings(boardStrings,fieldSeperator);
		
		ret = new BanqiGame(gameID,playerOne,playerTwo,board);
		return ret;
	}
	
	private static GameBoard createGameBoardFromStrings(String strings[], String fieldSeperator) {
		GameBoard board = new GameBoard();
		Square squares[] = board.getSquaresOnBoard();
		int length = squares.length;
		for(int i=0;i<length;i++) {
			squares[i] = createSquareFromStrings(strings,fieldSeperator);
		}
		
		return board;
	}
	
	private static Square createSquareFromStrings(String strings[], String fieldSeperator) {
		int x = Integer.parseInt(strings[0]);
		int y = Integer.parseInt(strings[1]);
		Square ret = new Square(x,y);
		
		strings = Arrays.copyOfRange(strings, 2, strings.length);
		Piece piece = createPieceFromStrings(strings,fieldSeperator);
		ret.setOn(piece);
		return ret;
	}
	
	private static Piece createPieceFromStrings(String strings[], String fieldSeperator) {
		Piece ret;
		int index;
		if(strings[0].equals(PIECE_IS_NULL)) {
			ret = null;
			index = 1;
		} else {
			int rank = Integer.parseInt(strings[0]);
			boolean faceUp = strings[1].equals(PIECE_IS_FACE_UP);
			boolean color = strings[2].equals(PIECE_IS_RED);
			index = 3;
			
			ret = PieceFactory.getInstance().createPiece(rank, color);
			ret.faceUp = faceUp;
		}
		strings = Arrays.copyOfRange(strings, index, strings.length);
		return ret;
	}
	

	public static String gameToString(BanqiGame game, String fieldSeperator) {
		StringBuilder ret = new StringBuilder();
		ret.append(game.getGameID()+fieldSeperator);
		ret.append(game.getPlayerOne()+fieldSeperator);
		ret.append(game.getPlayerTwo()+fieldSeperator);
		appendGameBoard(game.getGameBoard(),fieldSeperator,ret);
		return ret.toString();
	}
	
	private static void appendGameBoard(GameBoard board, String fieldSeperator, StringBuilder ret) {
		for(Square square: board.getSquaresOnBoard()) {
			appendSquare(square,fieldSeperator,ret);
		}
	}
	
	private static void appendSquare(Square square, String fieldSeperator, StringBuilder ret) {
		ret.append(square.getX()+fieldSeperator);
		ret.append(square.getY()+fieldSeperator);
		appendPiece(square.getOn(),fieldSeperator,ret);
	}
	
	private static void appendPiece(Piece piece, String fieldSeperator, StringBuilder ret) {
		if(piece == null) {
			ret.append(PIECE_IS_NULL+fieldSeperator);
		} else {
			ret.append(piece.getRank()+fieldSeperator);
			String faceUp = (piece.isFaceUp()) ? PIECE_IS_FACE_UP : PIECE_IS_FACE_DOWN;
			ret.append(faceUp+fieldSeperator);
			String color = (piece.isRed()) ? PIECE_IS_RED : PIECE_IS_BLACK;
			ret.append(color+fieldSeperator);
		}
	}
}
