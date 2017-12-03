package edu.colostate.cs.cs414.p5.user;



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

	public void setHistory(History history) {
		this.history = history;
	}
	
	/**
	 * @see History
	 * @return the History object associated with this profile
	 */
	public History getHistory()
	{
		return this.history;
	}
	
	@Override
	public String toString(){
		return this.name + "\n" + this.history.toString();
	}
	
	public String getName(){
		return this.name;
	}
	
	@Override
	public int hashCode() {
		return toString().hashCode();
	}
	
	@Override
	public boolean equals(Object o){
		if(o == null || !(o instanceof Profile)) {
			return false;
		} else {
			return this.hashCode() == o.hashCode();
		}
	}
	
}