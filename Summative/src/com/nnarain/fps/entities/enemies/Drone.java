package com.nnarain.fps.entities.enemies;

import com.badlogic.gdx.math.Vector3;

import com.nnarain.fps.entities.weapon.AssaultRifle;

/**
 * @author Natesh Narain
 * @version 0.0.1
 * @since 2013-06-04
 */
public class Drone extends Enemy{
	
	/**
	 * @param position
	 */
	public Drone(Vector3 position) {
		
		super(position);
		
		this.setWeapon(new AssaultRifle(this.getPosition(), this));
		
	}

}
