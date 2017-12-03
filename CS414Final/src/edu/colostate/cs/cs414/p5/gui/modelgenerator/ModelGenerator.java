package edu.colostate.cs.cs414.p5.gui.modelgenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.swing.JOptionPane;

import org.lwjgl.util.vector.Vector3f;

import edu.colostate.cs.cs414.p5.banqi.GameBoard;
import edu.colostate.cs.cs414.p5.banqi.Piece;
import edu.colostate.cs.cs414.p5.gui.entities.Entity;
import edu.colostate.cs.cs414.p5.gui.models.TexturedModel;
import edu.colostate.cs.cs414.p5.gui.objconverter.OBJFileLoader;
import edu.colostate.cs.cs414.p5.gui.renderengine.Loader;
import edu.colostate.cs.cs414.p5.gui.textures.ModelTexture;


/**
 * 
 * 
 * @author Sam
 *
 */
public class ModelGenerator  {

	
	
	
	public ArrayList<Entity> generatePieceModels(GameBoard board, Loader loader){

		
		ArrayList<Entity> piecies = new ArrayList<Entity>();
		

		

		for(int i=0;i<32;i++) {
			TexturedModel piece;
			String textureName="";
			String modelName="";
			Piece banqiPiece = board.getSquare(i).getOn();
			
			switch(banqiPiece.getRank()) {

			case 1:  modelName="soldier_piece";
			textureName="soldier_";
			
			break;
			case 2: modelName="cannon_piece";
			textureName="cannon_";
			
			break;
			case 3:modelName="cavalry_piece"; 
			textureName="cavalry_";
			
			break;
			case 4:modelName="chariot_piece";
			textureName="chariot_";
			
			break;
			case 5:modelName="elephant_piece";
			textureName="elephant_";
			
			break;
			case 6:modelName="advisor_piece"; 
			textureName="advisor_";
			
			break;
			case 7:modelName="general_piece"; 
			textureName="general_";
			
			break;
			default:  piece=null;
			
			break;
			}
			if(banqiPiece.isRed()) {
				textureName+="red";
			}
			else {
				textureName+="black";
			}
			
			piece  =  new TexturedModel(OBJFileLoader.loadOBJ(modelName, loader),
					new ModelTexture(loader.loadTexture(textureName)));
			Entity pieceEntity = new Entity(piece,getBoardPosition(i), 180, 0, 0, 1f);
			
			
			piecies.add(pieceEntity);
		}


		return piecies;

	}
	
	public Vector3f getBoardPosition(int square) {




		Vector3f position;
		float x=0,y=-23,z=0;
		if(square/8==0) {
			z=-90;
		}
		else if(square/8==1) {
			z=-80;
		}
		else if(square/8==2) {
			z=-70;
		}
		else if(square/8==3) {
			z=-60;
		}

		switch(square%8){
		case 0: x=-30;
		break;

		case 1: x=-20;
		break;

		case 2: x=-10;
		break;

		case 3: x=0;
		break;

		case 4: x=10;
		break;

		case 5: x=20;
		break;

		case 6: x=30;
		break;

		case 7: x=40;
		break;

		}
		position = new Vector3f(x,y,z);
		return position;



	}
	public Entity mouseSelectPiece(ArrayList<Entity>piecies,Vector3f mouseLocation) {
		
		for(Entity piece: piecies) {
			if(piece.getPosition()==mouseLocation)
			{
				return piece;
			}
			
		}
		return null;
		
		
		
		
		
	}

	
	
	

}
