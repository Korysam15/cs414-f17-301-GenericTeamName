package edu.colostate.cs.cs414.p5.gui.models;

import edu.colostate.cs.cs414.p5.gui.textures.ModelTexture;

public class TexturedModel {
	
	private RawModel rawModel;
	private ModelTexture texture;

	
	public TexturedModel(RawModel model, ModelTexture texture){
		this.rawModel = model;
		this.texture = texture;
	}

	public RawModel getRawModel() {
		return rawModel;
	}

	public ModelTexture getTexture() {
		return texture;
	}

}
