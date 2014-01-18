package com.nnarain.fps.entities.weapon;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.nnarain.fps.entities.Entity3D;

/**
 * @author Natesh Narain
 * @version 0.0.1
 * @since 2013-05-28
 */
public class Gun extends Weapon {

	private int numBullets = 30;

	private final Array<Bullet> bulletPool = new Array<Bullet>(numBullets);
	private final Array<Bullet> activeBullets = new Array<Bullet>();
	private int bulletIndex = 0;

	/**
	 * @param position
	 * @param angle
	 * @param damage
	 */
	public Gun(Vector3 position, float damage, Entity3D parent) {

		super(position, damage, parent);

		loadBullets();

	}

	/**
	 * @see com.nnarain.fps.entities.weapon.Weapon#fire()
	 */
	@Override
	public void fire() {
		
		super.fire();

		final Bullet bullet = bulletPool.get(bulletIndex);
		
		final Vector3 pos = this.getPosition().cpy();
		final Vector3 dir = this.getDirection().cpy();
		
		bullet.setActive(true);
		bullet.setCollisionPoint(null);
		bullet.setPosition(pos);
		bullet.setDirection(dir);
		bullet.setOrigin(pos.cpy());
		
		bullet.setCollisionRay(pos, dir);
		
		activeBullets.add(bullet);

		bulletIndex++;

		if (bulletIndex == numBullets)
			bulletIndex = 0;

	}

	/**
	 * @see com.nnarain.fps.entities.weapon.Weapon#update()
	 */
	@Override
	public void update(float delta) {
	
		for(final Bullet bullet : activeBullets){
			
			bullet.update(delta);
			
		}
		
	}
	
	private void loadBullets() {

		for (int i = 0; i < numBullets; i++) {

			Bullet b = new Bullet();
			b.setParent(this.getParent());
			
			bulletPool.add(b);

		}

	}

	/**
	 * @return the activeBullets
	 */
	public Array<Bullet> getActiveBullets() {
		return activeBullets;
	}

	/**
	 * @return the bulletPool
	 */
	public Array<Bullet> getBulletPool() {
		return bulletPool;
	}

}
