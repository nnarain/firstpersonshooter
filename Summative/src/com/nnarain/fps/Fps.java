package com.nnarain.fps;

import com.badlogic.gdx.Game;
import com.nnarain.fps.screens.GameScreen;
import com.nnarain.fps.screens.SetupScreen;

/**
 * Game entry point
 * 
 * */
public class Fps extends Game {

	private GameScreen gameScreen = new GameScreen();
	private SetupScreen setupScreen = new SetupScreen();
	
	public static final boolean useGLES = true;
	
	/**
	 * @see com.badlogic.gdx.ApplicationListener#create()
	 */
	@Override
	public void create() {
		
		this.setScreen(gameScreen);
		
	}

	/**
	 * @see com.badlogic.gdx.Game#dispose()
	 */
	@Override
	public void dispose() {

		super.dispose();
		
		gameScreen.dispose();
		setupScreen.dispose();

	}

}
