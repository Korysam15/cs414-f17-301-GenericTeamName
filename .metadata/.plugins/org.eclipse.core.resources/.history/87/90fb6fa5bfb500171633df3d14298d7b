

public class Profile {
	
	
	
	public static void main(String[] args) {
		Profile nick = new Profile("Nick");
		nick.history.addWin();
		nick.history.addWin();
		nick.history.addWin();
		nick.history.addWin();
		nick.history.addLoss();
		System.out.println(nick.toString());
	}
	public Profile(String name){
		this.name = name;
		this.history = new History();
	}
	private String name;
	protected History history;
	/**
	 * @see History
	 * @return the History object associated with this profile
	 */
	public History getHistory(){
		return this.history;
	}
	public String toString(){
		return this.name + "\n" + this.history.toString();
	}

}
