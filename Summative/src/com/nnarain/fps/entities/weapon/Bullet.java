package com.nnarain.fps.entities.weapon;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.nnarain.fps.entities.Entity3D;

/**
 * @author Natesh Narain
 * @version 0.0.1
 * @since 2013-05-27
 */
public class Bullet extends Entity3D {

	private static final String TAG = "Bullet";

	private Vector3 direction;
	private Vector3 origin;
	private Vector3 collisionPoint;
	private boolean active = false;

	private Ray collisionRay;

	private Entity3D parent;
	
	private static final float SPEED = 25f;

	/**
	 * 
	 */
	public Bullet() {
	}

	public void update(float delta) {

		float f = SPEED * delta;

		this.direction.scl(f);
		this.getPosition().add(this.direction);
		this.direction.scl(1 / f);

	}

	public boolean isPastCollisionPoint(){
		
		Vector3 bulletDistance = this.getPosition().cpy().sub(this.origin);
		Vector3 collisionDistance = null;
		
		if(this.collisionPoint != null){
			
			collisionDistance = this.collisionPoint.cpy().sub(this.origin);
		
			return bulletDistance.len() > collisionDistance.len();
			
		}else{
			
			return bulletDistance.len() > 300;
			
		}
		
	}
	
	public void setCollisionRay(Vector3 origin, Vector3 direction) {

		collisionRay = new Ray(origin, direction);

	}

	/**
	 * @return the collisionPoint
	 */
	public Vector3 getCollisionPoint() {
		return collisionPoint;
	}

	/**
	 * @param collisionPoint
	 *            the collisionPoint to set
	 */
	public void setCollisionPoint(Vector3 collisionPoint) {
		this.collisionPoint = collisionPoint;
	}

	/**
	 * @return the origin
	 */
	public Vector3 getOrigin() {
		return origin;
	}

	/**
	 * @param origin
	 *            the origin to set
	 */
	public void setOrigin(Vector3 origin) {
		this.origin = origin;
	}

	/**
	 * @return the collisionRay
	 */
	public Ray getCollisionRay() {
		return collisionRay;
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
	 * @return the active
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * @param active
	 *            the active to set
	 */
	public void setActive(boolean active) {
		this.active = active;
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
