package com.nnarain.fps.utils;

/**
 * @author Natesh Narain
 * @version 1.0.2
 * @since 2013-04-10
 */
public interface Renderer {

	public static final String U_PROJVIEW = "u_projView";
	public static final String U_NORMAL = "u_normal";
	public static final String U_TEXTURE = "u_texture";
	
	public void render();
	public void dispose();

}
