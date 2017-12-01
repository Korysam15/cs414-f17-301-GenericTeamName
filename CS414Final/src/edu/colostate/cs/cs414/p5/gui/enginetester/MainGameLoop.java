package edu.colostate.cs.cs414.p5.gui.enginetester;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;


import edu.colostate.cs.cs414.p5.banqi.BanqiGame;
import edu.colostate.cs.cs414.p5.banqi.BanqiPlayer;
import edu.colostate.cs.cs414.p5.banqi.GameBoard;
import edu.colostate.cs.cs414.p5.banqi.Piece;
import edu.colostate.cs.cs414.p5.banqi.Square;
import edu.colostate.cs.cs414.p5.gui.entities.Camera;
import edu.colostate.cs.cs414.p5.gui.entities.Entity;
import edu.colostate.cs.cs414.p5.gui.entities.EntityInfo;
import edu.colostate.cs.cs414.p5.gui.entities.Light;
import edu.colostate.cs.cs414.p5.gui.fontmeshcreator.FontType;
import edu.colostate.cs.cs414.p5.gui.fontmeshcreator.GUIText;
import edu.colostate.cs.cs414.p5.gui.fontrendering.TextMaster;
import edu.colostate.cs.cs414.p5.gui.guis.GuiRenderer;
import edu.colostate.cs.cs414.p5.gui.guis.GuiTexture;
import edu.colostate.cs.cs414.p5.gui.models.RawModel;
import edu.colostate.cs.cs414.p5.gui.models.TexturedModel;
import edu.colostate.cs.cs414.p5.gui.normalmappingobjconverter.NormalMappedObjLoader;
import edu.colostate.cs.cs414.p5.gui.objconverter.OBJFileLoader;
import edu.colostate.cs.cs414.p5.gui.renderengine.DisplayManager;
import edu.colostate.cs.cs414.p5.gui.renderengine.Loader;
import edu.colostate.cs.cs414.p5.gui.renderengine.MasterRenderer;
import edu.colostate.cs.cs414.p5.gui.renderengine.OBJLoader;
import edu.colostate.cs.cs414.p5.gui.terrains.Terrain;
import edu.colostate.cs.cs414.p5.gui.textures.ModelTexture;
import edu.colostate.cs.cs414.p5.gui.textures.TerrainTexture;
import edu.colostate.cs.cs414.p5.gui.textures.TerrainTexturePack;
import edu.colostate.cs.cs414.p5.gui.toolbox.MousePicker;
import edu.colostate.cs.cs414.p5.gui.water.WaterFrameBuffers;
import edu.colostate.cs.cs414.p5.gui.water.WaterRenderer;
import edu.colostate.cs.cs414.p5.gui.water.WaterShader;
import edu.colostate.cs.cs414.p5.gui.water.WaterTile;

import model.game_modes.ModelGenerator;
/**
 * 
 * 
 * @author Sam
 *
 */
public class MainGameLoop {


	private static final  int  ENTITY_COUNT=33;



	/**
	 * A reference to the board
	 */
	private GameBoard board;

	/**
	 * The type of game played
	 */
	private ModelGenerator modelGen;

	/**
	 * The players that in this game
	 */
	private BanqiPlayer player1,player2;

	private BanqiPlayer currentPlayer;
	BanqiGame banqiGame;


	private int selectedIndex=-1;
	int toFlip=-1;
	private GameBoard oldBoard;

	



	private Piece[] pieces;



	private TexturedModel whiteSquare ;
	private TexturedModel blackSquare ;



	/**
	 * @param player1
	 * @param player2
	 * @param pieces 
	 * @param banqiGame 
	 * @throws InterruptedException
	 */
	public void startGame( BanqiGame banqiGame)  {
		this.banqiGame=banqiGame;
		this.board=banqiGame.getGameBoard();
		this.player1=banqiGame.getFirstPlayer();
		this.player2=banqiGame.getSecondPlayer();

		this.modelGen=new ModelGenerator();
		DisplayManager.createDisplay(); 
		Loader loader = new Loader();


		List<Entity> entities = new ArrayList<Entity>();
		List<Terrain> terrains = new ArrayList<Terrain>();
		List<Entity> normalMapEntities = new ArrayList<Entity>();
		Entity[] pieceModels= new Entity[32];


		//************************Font************************	
		TextMaster.init(loader);
		FontType font = new FontType(loader.loadTexture("arial"), new File("res/arial.fnt"));
		GUIText text = new GUIText(player1.nickName+"  vs  "+player2.nickName, 3f, font, new Vector2f(0f, 0f), 1f, true);
		text.setColour(1, 0, 0);	
		// *****************************************************





		//************************Terrain************************
		//		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grassy2"));
		//		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("mud"));
		//		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("grassFlowers"));
		//		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));
		//
		//		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
		//		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));
		//		TexturedModel rocks = new TexturedModel(OBJFileLoader.loadOBJ("rocks", loader),
		//				new ModelTexture(loader.loadTexture("rocks")));
		//
		//		ModelTexture fernTextureAtlas = new ModelTexture(loader.loadTexture("fern"));
		//		fernTextureAtlas.setNumberOfRows(2);
		//
		//		TexturedModel fern = new TexturedModel(OBJFileLoader.loadOBJ("fern", loader), fernTextureAtlas);
		//
		//		TexturedModel bobble = new TexturedModel(OBJFileLoader.loadOBJ("pine", loader),
		//				new ModelTexture(loader.loadTexture("pine")));
		//		bobble.getTexture().setHasTransparency(true);
		//
		//		fern.getTexture().setHasTransparency(true);
		//
		//		Terrain terrain = new Terrain(-20, -1, loader, texturePack, blendMap, "hi");
		//
		//		terrains.add(terrain);
		//
		//		TexturedModel lamp = new TexturedModel(OBJLoader.loadObjModel("lamp", loader),
		//				new ModelTexture(loader.loadTexture("lamp")));
		//		lamp.getTexture().setUseFakeLighting(true);

		// *****************************************************************
		//*






		//****************Entity for the title**********************
		TexturedModel title = new TexturedModel(OBJFileLoader.loadOBJ("Banqi_Title", loader), 
				new ModelTexture(loader.loadTexture("Title_color")));
		Entity titleEntity = new Entity(title, new Vector3f(4.5f, 50, -100), 90, 0, 0, 2f);
		entities.add(titleEntity);
		// *******************************************************************
		//		TexturedModel grassIsland = new TexturedModel(OBJFileLoader.loadOBJ("grass_island", loader), 
		//				new ModelTexture(loader.loadTexture("grass_island")));
		//		Entity grassIslandEntity = new Entity(grassIsland, new Vector3f(10, -50, -90), 0, 0, 0, 100f);
		//		entities.add(grassIslandEntity);







		// ******************************BoardTiles****************************
		List<Entity> boardTiles= new ArrayList<Entity>() ;  

		boardTiles.addAll(board.generateBoardModel(loader));

		//****************Entity for the selected piece
		TexturedModel selectedSquare = new TexturedModel(OBJFileLoader.loadOBJ("square", loader),
				new ModelTexture(loader.loadTexture("square_selected")));
		whiteSquare = new TexturedModel(OBJFileLoader.loadOBJ("square", loader),
				new ModelTexture(loader.loadTexture("square_white")));
		blackSquare = new TexturedModel(OBJFileLoader.loadOBJ("square", loader),
				new ModelTexture(loader.loadTexture("square_black")));

		// **********************************************************************



		// ******************************Pieces**********************************


		//pieceModels=modelGen.generatePieceModels(board,loader).toArray(new Entity[32]);
		
		
		entities.addAll(modelGen.generatePieceModels(board,loader));
		entities.addAll(boardTiles);

		// ***********************************************************************

		
		

		List<Light> lights = new ArrayList<Light>();
		Light sun = new Light(new Vector3f(10000, 10000, -10000), new Vector3f(1.3f, 1.3f, 1.3f));
		lights.add(sun);

		Light spotLight = new Light(new Vector3f(4.5f, 65, 0), new Vector3f(1.3f, 1.3f, 1.3f));
		lights.add(spotLight);
		MasterRenderer renderer = new MasterRenderer(loader);




		Camera camera = new Camera();
		List<GuiTexture> guiTextures = new ArrayList<GuiTexture>();
		GuiRenderer guiRenderer = new GuiRenderer(loader);
		MousePicker picker = new MousePicker(camera, renderer.getProjectionMatrix());

		//**********Water Renderer Set-up************************

		WaterFrameBuffers buffers = new WaterFrameBuffers();
		WaterShader waterShader = new WaterShader();
		WaterRenderer waterRenderer = new WaterRenderer(loader, waterShader, renderer.getProjectionMatrix(), buffers);
		List<WaterTile> waters = new ArrayList<WaterTile>();
		WaterTile water = new WaterTile(5, -75, -30);
		waters.add(water);

		//****************Game Loop Below*********************

		ArrayList<Integer> possibleMovesIndex= new ArrayList<Integer>();
		oldBoard=board;
		while (!Display.isCloseRequested()) {
			System.out.println(oldBoard.equals(board));
			oldBoard=board;
			//System.out.println(DisplayManager.delta);
			camera.move();

			camera.startGame();
			
			
			
			



			picker.update();
			if(Mouse.isButtonDown(0)) {


				int square=picker.getClickPosition();
				Piece  selectedPiece= board.getSquare(square).getOn();

				if(square !=-1) {
					if (selectedIndex!=-1&&selectedIndex!=square) {

						
						for(Integer i:possibleMovesIndex){
							entities.get(i+ENTITY_COUNT).setModel(getColor(i));
							
							}
//						if(possibleMovesIndex.contains(square)) {
//							banqiGame.makeMove(board.getSquare(selectedIndex), board.getSquare(square));
//							entities.get(selectedIndex+1).setPosition(entities.get(square+1).getPosition());
//							entities.get(square+1);
//						}
						
						possibleMovesIndex.clear();








						selectedIndex=-1;
					}
					else if(board.getSquare(square)!=null&&selectedPiece.isFaceUp()&&selectedIndex!=square) {

						selectedIndex=square;
						for(Square move:banqiGame.getValidMoves(board.getSquare(square))){
							entities.get(8*move.getY()+move.getX()+ENTITY_COUNT).setModel(selectedSquare);
							possibleMovesIndex.add(8*move.getY()+move.getX());


						}





					}
					else if(board.getSquare(square)!=null&&!selectedPiece.isFaceUp()&&selectedIndex!=square) {
						selectedIndex=square;
						toFlip=square+1;
						banqiGame.flipPiece(board.getSquare(square));
						
						Entity fDPiece=entities.get(toFlip);
						entities.get(toFlip).setY(fDPiece.getPosition().y+5);

					}




				}
			}
			if(toFlip!=-1) {

				if(entities.get(toFlip).getRotX()!=0) {
					entities.get(toFlip).increaseRotation(5, 0, 0);
				}
				else {
					entities.get(toFlip).increaseRotation(0, 0, 0);
					entities.get(toFlip).setY(entities.get(toFlip).getPosition().y-5);;
					toFlip=-1;
					selectedIndex=-1;
				}


			}



			GL11.glEnable(GL30.GL_CLIP_DISTANCE0);

			//render reflection texture
			buffers.bindReflectionFrameBuffer();
			float distance = 2 * (camera.getPosition().y - water.getHeight());
			camera.getPosition().y -= distance;
			camera.invertPitch();
			renderer.renderScene(entities,pieceModels, normalMapEntities,terrains,  lights, camera, new Vector4f(0, 1, 0, -water.getHeight()+1));

			camera.getPosition().y += distance;
			camera.invertPitch();

			//refraction texture
			buffers.bindRefractionFrameBuffer();
			renderer.renderScene(entities,pieceModels, normalMapEntities,terrains,  lights, camera, new Vector4f(0, -1, 0, water.getHeight()));

			//render to screen
			GL11.glDisable(GL30.GL_CLIP_DISTANCE0);
			buffers.unbindCurrentFrameBuffer();	
			renderer.renderScene(entities,pieceModels, normalMapEntities,terrains,  lights, camera, new Vector4f(0, -1, 0, 100000));	
			waterRenderer.render(waters, camera, sun);
			guiRenderer.render(guiTextures);
			TextMaster.render();

			DisplayManager.updateDisplay();
		}



		//*********Clean Up Below**************

		TextMaster.cleanUp();
		buffers.cleanUp();
		waterShader.cleanUp();
		guiRenderer.cleanUp();
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();

	}
	public TexturedModel getColor(int index) {
		if(index<8||(index<24&&index>15)){
			if(index%2!=0) {
				return whiteSquare;
			}
			return blackSquare;
		}
		else {
			if(index%2==0) {
				return whiteSquare;
			}
			return blackSquare;
			
		}
	}
	



}
