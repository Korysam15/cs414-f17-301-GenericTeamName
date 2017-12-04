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
	TexturedModel[] models;
	public 	ModelGenerator(Loader loader) {
		models= new TexturedModel[14];

		TexturedModel piece  =  new TexturedModel(OBJFileLoader.loadOBJ("soldier_piece", loader),
				new ModelTexture(loader.loadTexture("soldier_red")));
		models[0]=piece;
		TexturedModel piece2  =  new TexturedModel(OBJFileLoader.loadOBJ("soldier_piece", loader),
				new ModelTexture(loader.loadTexture("soldier_black")));
		models[1]=piece2;

		TexturedModel piece3  =  new TexturedModel(OBJFileLoader.loadOBJ("cannon_piece", loader),
				new ModelTexture(loader.loadTexture("cannon_red")));
		models[2]=piece3;
		TexturedModel piece4  =  new TexturedModel(OBJFileLoader.loadOBJ("cannon_piece", loader),
				new ModelTexture(loader.loadTexture("cannon_black")));
		models[3]=piece4;
		TexturedModel piece5  =  new TexturedModel(OBJFileLoader.loadOBJ("cavalry_piece", loader),
				new ModelTexture(loader.loadTexture("cavalry_red")));
		models[4]=piece5;
		TexturedModel piece6  =  new TexturedModel(OBJFileLoader.loadOBJ("cavalry_piece", loader),
				new ModelTexture(loader.loadTexture("cavalry_black")));
		models[5]=piece6;

		TexturedModel piece7  =  new TexturedModel(OBJFileLoader.loadOBJ("chariot_piece", loader),
				new ModelTexture(loader.loadTexture("chariot_red")));
		models[6]=piece7;
		TexturedModel piece8  =  new TexturedModel(OBJFileLoader.loadOBJ("chariot_piece", loader),
				new ModelTexture(loader.loadTexture("chariot_black")));
		models[7]=piece8;
		TexturedModel piece9  =  new TexturedModel(OBJFileLoader.loadOBJ("elephant_piece", loader),
				new ModelTexture(loader.loadTexture("elephant_red")));
		models[8]=piece9;
		TexturedModel piece10  =  new TexturedModel(OBJFileLoader.loadOBJ("elephant_piece", loader),
				new ModelTexture(loader.loadTexture("elephant_black")));
		models[9]=piece10;
		TexturedModel piece11  =  new TexturedModel(OBJFileLoader.loadOBJ("advisor_piece", loader),
				new ModelTexture(loader.loadTexture("advisor_red")));
		models[10]=piece11;
		TexturedModel piece12  =  new TexturedModel(OBJFileLoader.loadOBJ("advisor_piece", loader),
				new ModelTexture(loader.loadTexture("advisor_black")));
		models[11]=piece12;

		TexturedModel piece13  =  new TexturedModel(OBJFileLoader.loadOBJ("general_piece", loader),
				new ModelTexture(loader.loadTexture("general_red")));
		models[12]=piece13;
		TexturedModel piece14  =  new TexturedModel(OBJFileLoader.loadOBJ("general_piece", loader),
				new ModelTexture(loader.loadTexture("general_black")));
		models[13]=piece14;








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





	}public ArrayList<Entity> generatePieceModels(GameBoard gb){
		ArrayList<Entity> entities= new ArrayList<Entity>();
		for(int i=0; i<gb.getSquaresOnBoard().length;i++) {
			if(gb.getSquaresOnBoard()[i].getOn()!=null) {
				entities.add(getEntity(gb.getSquaresOnBoard()[i].getOn(),i));
			}
		}
		return entities;
	}



	public Entity getEntity(Piece p,int position) {
		String textureName;
		int i=0;
		int rotX=0;
		switch(p.getRank()) {





		case 2: 
			i+=2;

			break;
		case 3:
			i+=4;

			break;
		case 4:
			i+=6;

			break;
		case 5:
			i+=8;

			break;
		case 6:
			i+=10;

			break;
		case 7:
			i+=12;

			break;
		default:

			break;
		}

		i=colorCheck(p,i);
		if(!p.isFaceUp()) {
			rotX=180;
		}
		if(models[i]==null) {System.out.println("model is null");}

		return new Entity(models[i], getBoardPosition(position), rotX, 0, 0, 1f);






	}

	public int colorCheck(Piece p,int index) {

		if(p.isBlack()) {
			index++;
		}
		return index;
	}







}
