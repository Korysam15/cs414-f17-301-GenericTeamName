

public class Profile {
	
	
	
	public static void main(String[] args) {
		Profile nick = new Profile("Nick");
		System.out.println(nick.toString());
	}
	public Profile(String name){
		this.name = name;
	}
	private String name;
	
	/**
	 * @see History
	 * @return the History object associated with this profile
	 */
	
	public String toString(){
		return this.name;
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
