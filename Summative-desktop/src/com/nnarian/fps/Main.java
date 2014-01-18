package com.nnarian.fps;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.nnarain.fps.Fps;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Summative";
		cfg.useGL20 = Fps.useGLES;
		cfg.width = 1080;
		cfg.height = 720;
		
		new LwjglApplication(new Fps(), cfg);
	}
}
