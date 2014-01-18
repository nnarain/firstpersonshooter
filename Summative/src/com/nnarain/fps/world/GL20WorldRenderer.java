package com.nnarain.fps.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.nnarain.fps.entities.City;
import com.nnarain.fps.entities.Player;
import com.nnarain.fps.entities.enemies.Enemy;
import com.nnarain.fps.entities.weapon.Bullet;
import com.nnarain.fps.entities.weapon.Gun;
import com.nnarain.fps.entities.weapon.Weapon;

/**
 * @author Natesh Narain
 * @version 0.0.1
 * @since 2013-05-20
 */
public class GL20WorldRenderer extends BaseWorldRenderer {

	private ShaderProgram lightTexShader;
	private ShaderProgram bulletShader;

	private static final String U_PROJVIEW = "u_projView";
	private static final String U_NORMAL = "u_normal";
	private static final String U_TEXTURE = "u_texture";

	private Matrix4 projView = new Matrix4();

	/**
	 * 
	 */
	public GL20WorldRenderer(World world) {

		super(world);

		lightTexShader = new ShaderProgram(
				Gdx.files.internal("data/gfx/shaders/light_tex.vert"),
				Gdx.files.internal("data/gfx/shaders/light_tex.frag"));
		bulletShader = new ShaderProgram(
				Gdx.files.internal("data/gfx/shaders/bullet.vert"), 
				Gdx.files.internal("data/gfx/shaders/bullet.frag"));

		if (!lightTexShader.isCompiled())
			throw new GdxRuntimeException(lightTexShader.getLog());

		if (!bulletShader.isCompiled())
			throw new GdxRuntimeException(bulletShader.getLog());

	}

	/**
	 * @see com.nnarian.fps.world.BaseWorldRenderer#render()
	 */
	@Override
	public void render() {

		super.render();

		GL20 gl = Gdx.graphics.getGL20();

		gl.glClearColor(1, 1, 1, 1);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glDepthMask(true);

		renderPlayer(world.player);
		renderCity(world.city);
		renderEnemies(world.enemies);

	}

	private void renderPlayer(Player player) {

		Weapon weapon = player.getWeapon();

		assaultRifleTexture.bind();

		lightTexShader.begin();
		{
			lightTexShader.setUniformi(U_TEXTURE, 0);

			projView.set(camera3D.combined);
			weapon.applyTransforms(projView);

			lightTexShader.setUniformMatrix(U_PROJVIEW, projView);
			lightTexShader.setUniformMatrix(U_NORMAL, weapon.getNormalMatrix());

			ModelInfo.assaultRifleModel.render(lightTexShader);
		}
		lightTexShader.end();
		
		// rendering bullets
		if(weapon instanceof Gun){
			
			Gun gun = (Gun)weapon;
			
			renderBullets(gun.getActiveBullets());
			
		}

	}

	private void renderEnemies(Array<Enemy> enemies) {

		for (Enemy enemy : enemies) {

			lightTexShader.begin();
			{
				lightTexShader.setUniformi(U_TEXTURE, 0);

				projView.set(camera3D.combined);
				enemy.applyTransforms(projView);

				lightTexShader.setUniformMatrix(U_PROJVIEW, projView);
				lightTexShader.setUniformMatrix(U_NORMAL, enemy.getNormalMatrix());

				ModelInfo.enemyModel.render(lightTexShader);
			}
			lightTexShader.end();
			
			// render enemy bullets
			if(enemy.getWeapon() instanceof Gun){
				
				renderBullets(((Gun)enemy.getWeapon()).getActiveBullets());
				
			}
			
		}

	}

	private void renderCity(City city){
		
		lightTexShader.begin();
		{
			lightTexShader.setUniformi(U_TEXTURE, 0);

			projView.set(camera3D.combined);
			city.applyTransforms(projView);
			
			lightTexShader.setUniformMatrix(U_PROJVIEW, projView);
			lightTexShader.setUniformMatrix(U_NORMAL, city.getNormalMatrix());
			
			ModelInfo.cityModel.render(lightTexShader);
		}
		lightTexShader.end();
		
	}
	
	private void renderBullets(Array<Bullet> bullets) {
		
		for(Bullet bullet : bullets){
			
			bulletShader.begin();
			{
				projView.set(camera3D.combined);
				bullet.applyTransforms(projView);
				
				bulletShader.setUniformMatrix(U_PROJVIEW, projView);
				bulletShader.setUniformMatrix(U_NORMAL, bullet.getNormalMatrix());
				
				ModelInfo.bulletModel.render(bulletShader);
			}
			bulletShader.end();
			
		}
		
	}

	/**
	 * @see com.nnarain.fps.world.BaseWorldRenderer#dipose()
	 */
	@Override
	public void dipose() {

		super.dipose();

		lightTexShader.dispose();
		bulletShader.dispose();

	}

}
