package edu.colostate.cs.cs414.p4.banqi;
/**
 * @author Sam Maxwell
 *
 */
public class Square {
	
	
	private int x,y; // x,y coordinates of the square
	private Piece on;
	
	
	
	public Square(int x, int y) {
		
		this.x = x;
		this.y = y;
		
	}
	public void setOn(Piece on) {
		this.on = on;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public Piece getOn() {
		return on;
	}
	public boolean isEmpty(){
		return on==null;
	}
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Square)
		{
			Square s=(Square) obj;
			return s.x==this.x&&s.y==y;
			
		}
		return false ;
	}
	

}
