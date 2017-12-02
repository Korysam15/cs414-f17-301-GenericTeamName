package edu.colostate.cs.cs414.p5.gui.entities;
/**
 * 
 * 
 * @author Sam
 *
 */
import org.lwjgl.util.vector.Vector3f;

import edu.colostate.cs.cs414.p5.gui.objconverter.ModelData;

public class EntityInfo {
	
	private String textureName;
	private ModelData data;
	private Vector3f position;
	private Vector3f rotation;
	private float scale;
	public String getTextureName() {
		return textureName;
	}
	public EntityInfo(String textureName, ModelData data, Vector3f position, Vector3f rotation, float scale) {
		
		this.textureName = textureName;
		this.data = data;
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
	}
	public ModelData getData() {
		return data;
	}
	public Vector3f getPosition() {
		return position;
	}
	public Vector3f getRotation() {
		return rotation;
	}
	public float getScale() {
		return scale;
	}

}
