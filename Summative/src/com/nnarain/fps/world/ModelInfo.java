package com.nnarain.fps.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.loaders.ModelLoaderRegistry;
import com.badlogic.gdx.graphics.g3d.loaders.wavefront.ObjLoader;
import com.badlogic.gdx.graphics.g3d.model.still.StillModel;
import com.badlogic.gdx.utils.Disposable;

/**
 * <p1><b>Only 1 instance of this class is going</b></p1>
 * 
 * @author Natesh Narain
 * @version 0.0.1
 * @since 2013-05-23
 */
public class ModelInfo {

	private static final String PATH = "data/gfx/models/";
	
	public static final StillModel cityModel = ModelLoaderRegistry.loadStillModel(Gdx.files.internal(PATH + "testcity.obj"));
	public static final StillModel assaultRifleModel = ModelLoaderRegistry.loadStillModel(Gdx.files.internal(PATH + "rifle.obj"));
	public static final StillModel bulletModel = ModelLoaderRegistry.loadStillModel(Gdx.files.internal(PATH + "bullet.obj"));
	public static final StillModel pistolModel = ModelLoaderRegistry.loadStillModel(Gdx.files.internal(PATH + "pistol.obj"));
	public static final StillModel enemyModel = ModelLoaderRegistry.loadStillModel(Gdx.files.internal(PATH + "enemy.obj"));
	
	public static void dispose() {
		
		cityModel.dispose();
		assaultRifleModel.dispose();
		bulletModel.dispose();
		pistolModel.dispose();
		enemyModel.dispose();
		
	}

}
