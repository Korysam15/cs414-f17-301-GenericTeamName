package edu.colostate.cs.cs414.p5.gui.entities;
/**
 * 
 * 
 * @author Sam
 *
 */

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

public class Camera {
	
	
	private Vector3f position = new Vector3f(5, 50, -73);
	private float pitch = 360;  //90;
	private float yaw = 0;
	private float roll;
	private int startGameTimer=0;
	
	
	public Camera(){
		
	}
	
	public void move(){
		calculateZoom();
		calculatePitch();
		
		
		
		
	}
	
	public void invertPitch(){
		this.pitch = -pitch;
	}

	public Vector3f getPosition() {
		return position;
	}

	public float getPitch() {
		return pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public float getRoll() {
		return roll;
	}
	private void calculateZoom(){
		float zoomLevel = Mouse.getDWheel() * 0.03f;
		
		position.y+=zoomLevel;
	}
	
	private void calculatePitch(){
		
		if(Mouse.isButtonDown(1)){
			float pitchChange = Mouse.getDY() * 0.2f;
			pitch -= pitchChange;
			if(pitch < 0){
				pitch = 360;
			}else if(pitch > 360){
				pitch = 0;
			}
		}
	}
	
	public void setPitch(float pitch) {
		this.pitch = pitch;
	}


	
	public void startGame() {
		startGameTimer++;
		if(pitch>360) {
			pitch=0;
		}
		if(pitch<0) {
			pitch=360;
		}
		if(pitch!=90&&startGameTimer>200) {
			pitch+=2;
		}
		
	}

	
	
	
	

}
