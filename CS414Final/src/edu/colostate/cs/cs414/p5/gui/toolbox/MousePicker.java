package edu.colostate.cs.cs414.p5.gui.toolbox;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import edu.colostate.cs.cs414.p5.gui.entities.Camera;
import edu.colostate.cs.cs414.p5.gui.terrains.Terrain;

public class MousePicker {

	private static final int RECURSION_COUNT = 200;
	private static final float RAY_RANGE = 600;

	private Vector3f currentRay = new Vector3f();

	private Matrix4f projectionMatrix;
	private Matrix4f viewMatrix;
	private Camera camera;


	private Vector3f currentTerrainPoint;
	private int square=-1;

	public MousePicker(Camera cam, Matrix4f projection) {
		camera = cam;
		projectionMatrix = projection;
		viewMatrix = Maths.createViewMatrix(camera);

	}


	static IntBuffer viewport = BufferUtils.createIntBuffer(16);
	static FloatBuffer modelview = BufferUtils.createFloatBuffer(16);
	static FloatBuffer projection = BufferUtils.createFloatBuffer(16);
	static FloatBuffer winZ = BufferUtils.createFloatBuffer(20);
	static FloatBuffer position = BufferUtils.createFloatBuffer(3);

	static public Vector3f getMousePositionIn3dCoords(int mouseX, int mouseY)
	{

		viewport.clear();
		modelview.clear();
		projection.clear();
		winZ.clear();;
		position.clear();
		float winX, winY;


		GL11.glGetFloat( GL11.GL_MODELVIEW_MATRIX, modelview );
		GL11.glGetFloat( GL11.GL_PROJECTION_MATRIX, projection );
		GL11.glGetInteger( GL11.GL_VIEWPORT, viewport );

		winX = (float)mouseX;
		winY = /* (float)viewport.get(3) -  */  //Uncomment this if you invert Y
				(float)mouseY;

		GL11.glReadPixels(mouseX, (int)winY, 1, 1, GL11.GL_DEPTH_COMPONENT, GL11.GL_FLOAT, winZ);

		float zz = winZ.get();

		GLU.gluUnProject(winX, winY, zz, modelview, projection, viewport, position);



		Vector3f v = new Vector3f (position.get(0),position.get(1),position.get(2));


		return v ;
	}
	public Vector3f getCurrentRay() {
		return currentRay;
	}

	public void update() {
		viewMatrix = Maths.createViewMatrix(camera);
		currentRay = calculateMouseRay();
		


	}
	public int getSquare() {
		return square;
	}
	public int getClickPosition() {
		int square=0;
		int mouseX=Mouse.getX();
		int mouseY=Mouse.getY();

		if(mouseX>145&&mouseX<1132&&mouseY>138 &&mouseY<632) {
			
			if (mouseY< 508 && mouseY> 384) {
				square+=8;
			}
			else if (mouseY< 384 && mouseY> 260) {
				square+=16;;
			}
			else if (mouseY< 260 && mouseY> 138) {
				square+=24;
			}



			
			if(mouseX> 268  && mouseX<   392) {
				square+=1;
			}
			else if(mouseX> 392  && mouseX<   516) {
				square+=2;
			}
			else if(mouseX> 516  && mouseX<   639) {
				square+=3;
			}
			else if(mouseX> 639  && mouseX<   763) {
				square+=4;
			}
			else if(mouseX> 763  && mouseX<   886) {
				square+=5;
			}
			else if(mouseX> 886  && mouseX<   1010) {
				square+=6;
			}
			else if(mouseX> 1010  && mouseX<   1132) {
				square+=7;
			}
		}
		else {
			return -1;
		}
		System.out.println(square);
		return square;

	}

	private Vector3f calculateMouseRay() {
		float mouseX = Mouse.getX();
		float mouseY = Mouse.getY();
		Vector2f normalizedCoords = getNormalisedDeviceCoordinates(mouseX, mouseY);
		Vector4f clipCoords = new Vector4f(normalizedCoords.x, normalizedCoords.y, -1.0f, 1.0f);
		Vector4f eyeCoords = toEyeCoords(clipCoords);
		Vector3f worldRay = toWorldCoords(eyeCoords);
		return worldRay;
	}

	private Vector3f toWorldCoords(Vector4f eyeCoords) {
		Matrix4f invertedView = Matrix4f.invert(viewMatrix, null);
		Vector4f rayWorld = Matrix4f.transform(invertedView, eyeCoords, null);
		Vector3f mouseRay = new Vector3f(rayWorld.x, rayWorld.y, rayWorld.z);
		mouseRay.normalise();
		return mouseRay;
	}

	private Vector4f toEyeCoords(Vector4f clipCoords) {
		Matrix4f invertedProjection = Matrix4f.invert(projectionMatrix, null);
		Vector4f eyeCoords = Matrix4f.transform(invertedProjection, clipCoords, null);
		return new Vector4f(eyeCoords.x, eyeCoords.y, -1f, 0f);
	}

	private Vector2f getNormalisedDeviceCoordinates(float mouseX, float mouseY) {
		float x = (2.0f * mouseX) / Display.getWidth() - 1f;
		float y = (2.0f * mouseY) / Display.getHeight() - 1f;
		return new Vector2f(x, y);
	}

	private Vector3f getPointOnRay(Vector3f ray, float distance) {
		Vector3f camPos = camera.getPosition();
		Vector3f start = new Vector3f(camPos.x, camPos.y, camPos.z);
		Vector3f scaledRay = new Vector3f(ray.x * distance, ray.y * distance, ray.z * distance);
		return Vector3f.add(start, scaledRay, null);
	}
	private boolean intersectionInRange(float start, float finish, Vector3f ray) {
		Vector3f startPoint = getPointOnRay(ray, start);
		Vector3f endPoint = getPointOnRay(ray, finish);
		if (!isUnderGround(startPoint) && isUnderGround(endPoint)) {
			return true;
		} else {
			return false;
		}
	}
	public Vector3f getCurrentTerrainPoint() {
		return currentTerrainPoint;
	}
	private boolean isUnderGround(Vector3f testPoint) {

		if (testPoint.y < -25) {
			return true;
		} else {
			return false;
		}
	}












}
