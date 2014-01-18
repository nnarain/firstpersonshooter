package com.nnarain.fps.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.nnarain.fps.entities.Player;

/**
 * @author Natesh Narain
 * @version 0.0.1
 * @since 2013-05-20
 */
public class BaseWorldRenderer {

	protected final World world;
	
	protected PerspectiveCamera camera3D;
	private final SpriteBatch batch = new SpriteBatch();
//	protected OrthographicCamera camera2D;
	
	protected final Texture cubeTexture;
	protected final Texture assaultRifleTexture;
	
	private final BitmapFont status = new BitmapFont();
	
	private final Matrix4 viewMatrix = new Matrix4();
	
	private final static String TEX_PATH = "data/gfx/textures/";
	
	protected BaseWorldRenderer(World world){
		
		this.world = world;
		
		camera3D = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera3D.near = 0.01f;
		
		cubeTexture = new Texture(Gdx.files.internal(TEX_PATH + "brick.png"));
		assaultRifleTexture = new Texture(Gdx.files.internal(TEX_PATH + "t_autorifle_d.tga"));
		
	}
	
	public void render(){
		
		updateCamera(world.player);
		
		// TODO : Font not rendering
		viewMatrix.setToOrtho2D(0, 0, 5, 5);
		batch.setProjectionMatrix(camera3D.combined);
		batch.begin();
		{
			batch.setColor(Color.RED);
			batch.enableBlending();
			status.draw(batch, "Hello First Person Shooter", 250, 250);
		}
		batch.end();
		
	}
	
	private void updateCamera(Player player){
		
		camera3D.position.set(player.getPosition());
		camera3D.direction.set(player.getDirection());
		camera3D.update();
		
	}
	
	public void dipose(){
		
		ModelInfo.dispose();
		
		cubeTexture.dispose();
		assaultRifleTexture.dispose();
		
		status.dispose();
		
		batch.dispose();
		
	}
	
}
