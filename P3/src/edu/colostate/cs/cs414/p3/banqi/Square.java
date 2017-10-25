package edu.colostate.cs.cs414.p3.banqi;
/**
 * @author Sam Maxwell
 *
 */
public class Square {
	
	private int x,y; // x,y coordinates of the square
	private Piece on;
	
	
	@Override
	public String toString() {
		return "Square [x=" + x + ", y=" + y + ", on=" + on + "]";
	}
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
	

}
