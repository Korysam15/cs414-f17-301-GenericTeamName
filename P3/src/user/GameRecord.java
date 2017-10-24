package user;



public class GameRecord {
	Profile player1;
	Profile player2;
	EndGameState end;
	public GameRecord(Profile player1, Profile player2, EndGameState end){
		this.player1 = player1;
		this.player2 = player2;
		this.end = end;
	}
	private String getOutcomeMessage(){
		String outcome;
		switch(end){
		case DRAW:
			outcome = "Draw"; break;
		case PLAYER1ABANDON:
			outcome = player1.getName() + " abandoned. "+player2.getName() + " won.";
			break;
		case PLAYER1WON:
			outcome = player1.getName() + " won.";
			break;
		case PLAYER2ABANDON:
			outcome = player2.getName() + " abandoned. " +player1.getName() + " won.";
			break;
		case PLAYER2WON:
			outcome = player2.getName() + " won.";
			break;
		default:
			outcome = null;
			break;
			
		}
		return outcome;
	}
	public String toString(){
		return this.player1.getName() + " " + this.player2.getName() + " " + this.end.toString();
	}
	public String getMessage(){
		String info = "Game between "+player1.getName()+" and " + player2.getName() + "\n";
		String result = "Outcome was: " + this.getOutcomeMessage();
		return info + result;
	}
	public Profile getLoser(){
		switch(end){
		case DRAW:
			System.out.println("default");
			return null;
		case PLAYER1ABANDON:
			return player1;
		case PLAYER1WON:
			return player2;
		case PLAYER2ABANDON:
			return player2;
		case PLAYER2WON:
			return player1;
		default:
			System.out.println("default");
			return null;
		}
	}
	public Profile getWinner(){
		switch(end){
		case DRAW:
			return null;
		case PLAYER1ABANDON:
			return player2;
		case PLAYER1WON:
			return player1;
		case PLAYER2ABANDON:
			return player1;
		case PLAYER2WON:
			return player2;
		default:
			return null;			
		}
	}
	public boolean wasWinner(Profile p){
		if (p.equals(this.getWinner())){
			return true;
		}
		return false;
	}
	public boolean wasLoser(Profile p){
		if(p.equals(this.getLoser())){
			return true;
		}
		return false;
	}
	public boolean hadProfileInGame(Profile p){
		return p.equals(player1)||p.equals(player2);
	}
}
enum EndGameState {
	PLAYER1WON, PLAYER2WON, DRAW, PLAYER1ABANDON, PLAYER2ABANDON;
}