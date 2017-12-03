package edu.colostate.cs.cs414.p5.banqi;

public class BanqiPlayer {
	public String nickName;
	public String color;
	public boolean isTurn;
	
	public BanqiPlayer(String nickName)
	{
		this.nickName = nickName;
		this.color = "";
		this.isTurn = false;
	}
	
	public String getColor()
	{
		return this.color;
	}
	
	public void setColor(String color)
	{
		this.color = color;
	}
	
	public String getName() {
		return nickName;
	}
	
	public String toString()
	{
		return this.nickName + " " + this.color;
	}
	
	public static void main(String[] args)
	{
		BanqiPlayer p = new BanqiPlayer("KKUDDA");
		System.out.println(p);
	}
}
