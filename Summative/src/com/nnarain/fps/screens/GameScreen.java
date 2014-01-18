package com.nnarain.fps.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.nnarain.fps.world.BaseWorldRenderer;
import com.nnarain.fps.world.GL10WorldRenderer;
import com.nnarain.fps.world.GL20WorldRenderer;
import com.nnarain.fps.world.ModelInfo;
import com.nnarain.fps.world.World;

/**
 * @author Natesh Narain
 * @version 0.0.1
 * @since 2013-05-20
 */
public class GameScreen implements Screen{

	private World world;
	private BaseWorldRenderer renderer;
	
	/**
	 * @see com.badlogic.gdx.Screen#render(float)
	 */
	@Override
	public void render(float delta) {
		
		world.update(delta);
		renderer.render();
		
	}

	/**
	 * @see com.badlogic.gdx.Screen#resize(int, int)
	 */
	@Override
	public void resize(int width, int height) {
	}

	/**
	 * @see com.badlogic.gdx.Screen#show()
	 */
	@Override
	public void show() {
		
		world = new World();
		
		renderer = (Gdx.graphics.isGL20Available()) ? new GL20WorldRenderer(world) : new GL10WorldRenderer(world);
		
	}

	/**
	 * @see com.badlogic.gdx.Screen#hide()
	 */
	@Override
	public void hide() {
	}

	/**
	 * @see com.badlogic.gdx.Screen#pause()
	 */
	@Override
	public void pause() {
	
		world.pause();
		
	}

	/**
	 * @see com.badlogic.gdx.Screen#resume()
	 */
	@Override
	public void resume() {
		
		world.resume();
		
	}

	/**
	 * @see com.badlogic.gdx.Screen#dispose()
	 */
	@Override
	public void dispose() {
		
		world.dispose();
		renderer.dipose();
		
	}

}
