
import java.util.ArrayList;

/**
 * @author nwilso222
 * Mock Server created by Nick to represnt information that would be stored on a database
 */
public class Server {
	public static ArrayList<GameRecord> history;
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	public static void RecordGame(Profile player1, Profile player2, EndGameState end){
		history.add(new GameRecord(player1, player2, end));
	}
	public static History queueHistory(Profile p){
		History ret = new History();
		for(GameRecord record : history){
			if(record.hadProfileInGame(p)){
				if(record.wasLoser(p)){
					ret.addLoss();
				}
				else if(record.wasWinner(p)){
					ret.addWin();
				}
				else{
					ret.addDraw();
				}
			}
		}
		return ret;
	}

}
