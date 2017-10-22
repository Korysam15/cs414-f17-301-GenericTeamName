package user;

public class Profile {
	/* Global Variables */
	private String name;
	protected History history;
	
	/* Constructor. Creates a new Profile based on a Player's NickName */
	public Profile(String name)
	{
		this.name = name;
		this.history = new History();
	}
	
	/**
	 * @see History
	 * @return the History object associated with this profile
	 */
	public History getHistory()
	{
		return this.history;
	}
	
	/**
	 * @see History for toString()
	 * @return the nickname and history of a Player
	 */
	public String toString()
	{
		return this.name + "\n" + this.history.toString();
	}

}
