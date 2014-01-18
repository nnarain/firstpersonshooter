package com.nnarain.fps.entities.weapon;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector3;
import com.nnarain.fps.entities.Entity3D;

/**
 * @author Natesh Narain
 * @version 0.0.1
 * @since 2013-05-27
 */
public class Weapon extends Entity3D {

	private Vector3 direction;

	private Sound sound = null;
	
	private float damage;
	private boolean firing = false;
	
	private Entity3D parent;

	/**
	 * 
	 */
	public Weapon(Vector3 position, float damage, Sound sound) {
		
		// TODO : not right now
		
	}
	
	/**
	 * 
	 */
	public Weapon(Vector3 position, float damage, Entity3D parent) {

		super(position);

		this.damage = damage;
		
		this.direction = new Vector3().set(Vector3.Z);
		
		this.parent = parent;

	}

	public void fire() {
		
		if(sound != null)
			sound.play();
		
	}
	
	public void update(float delta){}

	public void setSound(Sound sound){
		
		this.sound = sound;
		
	}
	
	/**
	 * @return the direction
	 */
	public Vector3 getDirection() {
		return direction;
	}

	/**
	 * @param direction
	 *            the direction to set
	 */
	public void setDirection(Vector3 direction) {
		this.direction = direction;
	}

	/**
	 * @return the damage
	 */
	public float getDamage() {
		return damage;
	}

	/**
	 * @param damage
	 *            the damage to set
	 */
	public void setDamage(float damage) {
		this.damage = damage;
	}

	/**
	 * @return the firing
	 */
	public boolean isFiring() {
		return firing;
	}

	/**
	 * @param firing
	 *            the firing to set
	 */
	public void setFiring(boolean firing) {
		this.firing = firing;
	}

	/**
	 * @return the parent
	 */
	public Entity3D getParent() {
		return parent;
	}

	/**
	 * @param parent the parent to set
	 */
	public void setParent(Entity3D parent) {
		this.parent = parent;
	}

}
