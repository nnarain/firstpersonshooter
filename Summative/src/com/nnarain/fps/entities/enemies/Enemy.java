package com.nnarain.fps.entities.enemies;

import com.badlogic.gdx.math.Vector3;

import com.nnarain.fps.entities.Entity3D;
import com.nnarain.fps.entities.weapon.Weapon;

/**
 * @author Natesh Narain
 * @version 0.0.1
 * @since 2013-06-03
 */
public class Enemy extends Entity3D {

	private float health = 100f;

	private Vector3 direction;
	
	private Weapon weapon;
	
	private float shotTime = 0f;
	
	private boolean colliding = false;

	private float acummTime = 0f;
	
	private static final float SPEED = .05f;
	private static final float SHOT_DELAY = 3f;

	/**
	 * 
	 */
	public Enemy(Vector3 position) {
		super(position);
	}

	public void update(float delta){
		
		this.acummTime += delta;
		
		if(weapon != null)
			weapon.update(delta);
		
		this.advance(delta);
		
		updateShooting();
		
	}
	
	private void updateShooting(){
		
		if(shotTime + SHOT_DELAY <= acummTime){
			
			this.weapon.fire();
			shotTime = acummTime;
			
		}
		
	}
	
	/**
	 * Moves this enemy along it's direction vector
	 * */
	public void advance(float delta){
		
		float scale = (colliding) ? -SPEED : SPEED; // if colliding with something move the other way
		
		this.direction.scl(scale);
		this.getPosition().add(direction);
		this.direction.scl(1/scale);
		
	}
	
	public void removeHealth(float damage){
		
		this.health -= damage;
		
	}
	
	/**
	 * @return the direction
	 */
	public Vector3 getDirection() {
		return direction;
	}

	/**
	 * @param direction the direction to set
	 */
	public void setDirection(Vector3 direction) {
		this.direction = direction;
	}

	/**
	 * @return the health
	 */
	public float getHealth() {
		return health;
	}

	/**
	 * @param health
	 *            the health to set
	 */
	public void setHealth(float health) {
		this.health = health;
	}

	/**
	 * @return the weapon
	 */
	public Weapon getWeapon() {
		return weapon;
	}

	/**
	 * @param weapon
	 *            the weapon to set
	 */
	public void setWeapon(Weapon weapon) {
		this.weapon = weapon;
	}

	/**
	 * @return the colliding
	 */
	public boolean isColliding() {
		return colliding;
	}

	/**
	 * @param colliding the colliding to set
	 */
	public void setColliding(boolean colliding) {
		this.colliding = colliding;
	}

}
