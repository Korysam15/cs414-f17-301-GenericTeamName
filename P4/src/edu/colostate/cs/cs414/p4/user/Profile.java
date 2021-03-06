package edu.colostate.cs.cs414.p4.user;



public class Profile {
	
	/* Global Variables */
	private History history;
	private String name;
	
	
	
	public static void main(String[] args) {
		Profile nick = new Profile("default_");
		System.out.println(nick.toString());
	}
	public Profile(String name){
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
	
	public String toString(){
		return this.name + "\n" + this.history.toString();
	}
	public String getName(){
		return this.name;
	}
	public boolean equals(Object o){
		if(o instanceof Profile){
			return ((Profile)o).name.equals(this.name);
		}
		else{
			return false;
		}
	}
	
}