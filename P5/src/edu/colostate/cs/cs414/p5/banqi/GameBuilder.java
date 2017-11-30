package edu.colostate.cs.cs414.p5.banqi;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class GameBuilder {

	private final String fieldSeperator;

	private static final String PLAYER_COLOR_NOT_SET = "UNKOWN";
	private static final String PLAYERS_TURN = "YES";
	private static final String NOT_PLAYERS_TURN = "NO";
	
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
		String stringArray[] = line.split(fieldSeperator);
		List<String> strings = new LinkedList<String>(Arrays.asList(stringArray));
		
		int gameID = Integer.parseInt(strings.remove(0));
		BanqiPlayer playerOne = createPlayerFromStrings(strings,fieldSeperator);
		BanqiPlayer playerTwo = createPlayerFromStrings(strings,fieldSeperator);

		GameBoard board = createGameBoardFromStrings(strings,fieldSeperator);

		ret = new BanqiGame(gameID,playerOne,playerTwo,board);

		return ret;
	}

	private static BanqiPlayer createPlayerFromStrings(List<String> strings, String fieldSeperator) {
		BanqiPlayer ret = new BanqiPlayer(strings.remove(0));
		String color = strings.remove(0);
		if(!PLAYER_COLOR_NOT_SET.equals(color)) {
			ret.setColor(color);
		}
		String turn = strings.remove(0);
		ret.isTurn = PLAYERS_TURN.equals(turn);
		return ret;
	}

	private static GameBoard createGameBoardFromStrings(List<String> strings, String fieldSeperator) {
		GameBoard board = new GameBoard();
		Square squares[] = board.getSquaresOnBoard();
		int length = squares.length;
		for(int i=0;i<length;i++) {
			squares[i] = createSquareFromStrings(strings,fieldSeperator);
		}

		return board;
	}

	private static Square createSquareFromStrings(List<String> strings, String fieldSeperator) {
		int x = Integer.parseInt(strings.remove(0));
		int y = Integer.parseInt(strings.remove(0));
		Square ret = new Square(x,y);

		Piece piece = createPieceFromStrings(strings,fieldSeperator);
		ret.setOn(piece);
		return ret;
	}

	private static Piece createPieceFromStrings(List<String> strings, String fieldSeperator) {
		Piece ret;
		String pieceInfo = strings.remove(0);
		if(pieceInfo.equals(PIECE_IS_NULL)) {
			ret = null;
		} else {
			int rank = Integer.parseInt(pieceInfo);
			boolean faceUp = strings.remove(0).equals(PIECE_IS_FACE_UP);
			boolean color = strings.remove(0).equals(PIECE_IS_RED);

			ret = PieceFactory.getInstance().createPiece(rank, color);
			ret.faceUp = faceUp;
		}
		return ret;
	}


	public static String gameToString(BanqiGame game, String fieldSeperator) {
		StringBuilder ret = new StringBuilder();
		ret.append(game.getGameID()+fieldSeperator);

		appendPlayer(game.getFirstPlayer(),fieldSeperator,ret);
		appendPlayer(game.getSecondPlayer(),fieldSeperator,ret);
		appendGameBoard(game.getGameBoard(),fieldSeperator,ret);
		return ret.toString();
	}

	private static void appendPlayer(BanqiPlayer player, String fieldSeperator, StringBuilder ret) {
		ret.append(player.getName()+fieldSeperator);
		
		if(player.getColor() != null && !player.getColor().isEmpty()) {
			ret.append(player.getColor()+fieldSeperator);
		} else {
			ret.append(PLAYER_COLOR_NOT_SET+fieldSeperator);
		}
		
		if(player.isTurn) {
			ret.append(PLAYERS_TURN+fieldSeperator);
		} else {
			ret.append(NOT_PLAYERS_TURN+fieldSeperator);
		}
		
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
