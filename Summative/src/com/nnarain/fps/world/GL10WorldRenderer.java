package com.nnarain.fps.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.utils.Array;
import com.nnarain.fps.entities.City;
import com.nnarain.fps.entities.Player;
import com.nnarain.fps.entities.enemies.Enemy;
import com.nnarain.fps.entities.weapon.AssaultRifle;
import com.nnarain.fps.entities.weapon.Bullet;
import com.nnarain.fps.entities.weapon.Gun;
import com.nnarain.fps.entities.weapon.Weapon;

/**
 * @author Natesh Narain
 * @version 0.0.1
 * @since 2013-05-20
 */
public class GL10WorldRenderer extends BaseWorldRenderer {
	
	private static final String TAG = "GL10WorldRenderer";
	
	private static final float[] RUBY_AMBIENT = { 0.1745f, 0.01175f, 0.01175f, 1.0f };
	private static final float[] RUBY_DIFFUSE = { 0.61424f, 0.04136f, 0.04136f, 1.0f };
	
	private static final float[] EMERALD_AMBIENT = { 0.0215f, 0.1745f, 0.0215f, 1.0f };
	private static final float[] EMERALD_DIFFUSE = { 0.07568f, 0.61424f, 0.07568f, 1.0f };
	
	private static final float[] CYAN_AMBIENT = {0.1f, 0.18725f, 0.1745f, 1.0f};
	private static final float[] CYAN_DIFFUSE = {0.396f, 0.74151f, 0.69102f, 1.0f};
	
	private static final float[] WHITE = {0, 0, 0, 1.0f};
	private static final float[] BLACK = {1f, 1f, 1f, 1.0f};
	
	/**
	 * 
	 */
	public GL10WorldRenderer(World world) {
	
		super(world);
		
	}
	
	/**
	 * @see com.nnarian.fps.world.BaseWorldRenderer#render()
	 */
	@Override
	public void render() {
		
		super.render();
		
		GL10 gl = Gdx.graphics.getGL10();
		
		gl.glClearColor(0, 0, 0, 1);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		
		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glDepthMask(true);
		
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glEnable(GL10.GL_LIGHTING);
		gl.glEnable(GL10.GL_LIGHT0);
		
		camera3D.apply(gl);
		
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, new float[]{0,0,1,1}, 0);
		
		renderCity(gl, world.city);
		renderWeapon(gl, world.player);
		renderEnemies(gl, world.enemies);
		
	}

	private void renderWeapon(GL10 gl, Player player){
		
		Weapon weapon = player.getWeapon();
		
		// Render Weapon
		gl.glPushMatrix();
		{
			gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT_AND_DIFFUSE, BLACK, 0);
			weapon.applyTransforms(gl);
			
			if(weapon instanceof AssaultRifle){
			
			//	assaultRifleTexture.bind();// TODO : makes everything dark
				ModelInfo.assaultRifleModel.render();
				
			}
			
		}
		gl.glPopMatrix();
		
		// if is a gun, render active bullets
		if(weapon instanceof Gun){
			
			Gun gun = (Gun)weapon;
			
			for(Bullet bullet : gun.getActiveBullets()){
				
				gl.glPushMatrix();
				{	
					gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT, RUBY_AMBIENT, 0);
					gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, RUBY_DIFFUSE, 0);
					bullet.applyTransforms(gl);
					ModelInfo.bulletModel.render();
				}
				gl.glPopMatrix();
				
			}
			
		}
		
	}
	
	private void renderEnemies(GL10 gl, Array<Enemy> enemies){
		
		for(Enemy enemy : enemies){
			
			gl.glPushMatrix();
			{
				gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT, EMERALD_AMBIENT, 0);
				gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, EMERALD_DIFFUSE, 0);
				enemy.applyTransforms(gl);
				ModelInfo.enemyModel.render();
			}
			gl.glPopMatrix();
			
		}
		
	}
	
	private void renderCity(GL10 gl, City city){
		
		gl.glPushMatrix();
		{
			gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT, CYAN_AMBIENT, 0);
			gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, CYAN_DIFFUSE, 0);
			city.applyTransforms(gl);
			ModelInfo.cityModel.render();
		}
		gl.glPopMatrix();
	
		/*
		StillSubMesh mesh = cityModel.getSubMesh("Monkey");
		
		monkeyTex.bind();
		mesh.mesh.render(mesh.primitiveType);
		*/
	}
	
}
