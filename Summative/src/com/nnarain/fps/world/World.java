package com.nnarain.fps.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.LifecycleListener;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.utils.Array;
import com.nnarain.fps.entities.City;
import com.nnarain.fps.entities.Entity3D;
import com.nnarain.fps.entities.Player;
import com.nnarain.fps.entities.enemies.Drone;
import com.nnarain.fps.entities.enemies.Enemy;
import com.nnarain.fps.entities.weapon.Bullet;
import com.nnarain.fps.entities.weapon.Gun;
import com.nnarain.fps.entities.weapon.Weapon;
import com.nnarain.fps.utils.Utils;

/**
 * Contains all game logic. All entities interact here
 * 
 * @author Natesh Narain
 * @version 0.0.1
 * @since 2013-05-20
 */
public class World implements LifecycleListener {

	private static final String TAG = "World";

	/** Controlled by user */
	public Player player;
	public City city;
	public Array<Enemy> enemies = new Array<Enemy>();

	private final Sound[] bulletImpacts = new Sound[3];
	private static final float soundFactor = 1f / 100f;

	/**
	 * 
	 */
	public World() {

		player = new Player(0, 5);

		city = new City(ModelInfo.cityModel);

		// load enemies
		for (int i = 0; i < 1; i++) {

			enemies.add(new Drone(new Vector3(i * 2, 5, 0)));

		}

		// load sound
		int size = bulletImpacts.length;
		final String IMPACT_SOUND_PATH = "data/sfx/impact_";

		for (int i = 0; i < size; i++) {

			bulletImpacts[i] = Gdx.audio.newSound(Gdx.files.internal(IMPACT_SOUND_PATH + i + ".ogg"));

		}

	}

	/**
	 * Update the world
	 * 
	 * @param delta
	 *            Games delta time
	 */
	public void update(float delta) {

		player.update(delta);
		city.checkElevation(player);

		Weapon playerWeapon = player.getWeapon();

		// playerWeapon.update(delta);
		updateWeapon(playerWeapon);

		updateEnemies(delta);

	}

	private void updateEnemies(float delta) {

		// final Enemy[] list = new Enemy[enemies.size];
		// System.arraycopy(enemies.items, 0, list, 0, list.length);

		int size = enemies.size;

		for (int i = 0; i < size; i++) {

			final Enemy enemy = enemies.get(i);

			Weapon enemyWeapon = enemy.getWeapon();
			updateWeapon(enemyWeapon);

			Vector3 ePos = enemy.getPosition().cpy();
			Vector3 pPos = player.getPosition().cpy();

			Vector3 dir = pPos.sub(ePos).nor().cpy();

			enemy.setDirection(dir);
			enemyWeapon.setDirection(dir);

			enemy.update(delta);

			checkCollisionBetweenEnemies(enemy, enemies);
			
			if(enemy.getHealth() <= 0){
				
				enemies.removeValue(enemy, false);
				
			}

		}

	}

	/**
	 * Check collision between the given enemy & the list of enemies
	 * 
	 * @param enemy
	 * 
	 * @param enemies
	 *            Enemies to check the given enemy against
	 * 
	 * */
	// TODO : make enemy collision work
	private void checkCollisionBetweenEnemies(Enemy enemy, Array<Enemy> list) {

		BoundingBox bbox = new BoundingBox();
		ModelInfo.enemyModel.getBoundingBox(bbox);
		enemy.translateBounds(bbox);

		Vector3 dim = bbox.getDimensions();
		Vector3 pos = enemy.getPosition();
		Vector3 center = bbox.getCenter();
		Vector3 crn = bbox.getCorners()[0];

		// dim.scl(1f/2f);

		float eRadius = crn.cpy().sub(pos).len();

		for (Enemy e : list) {

			if (e == enemy)
				continue;

			ModelInfo.enemyModel.getBoundingBox(bbox);
			e.translateBounds(bbox);

			Vector3 dir = pos.cpy().sub(e.getPosition());

			Ray collisionRay = new Ray(pos.cpy(), dir);
			Vector3 intersection = new Vector3();

			Intersector.intersectRayBounds(collisionRay, bbox, intersection);

			float enemyDistance = pos.cpy().sub(intersection).len();

			boolean colliding = (enemyDistance <= eRadius);
			
			// DEBUG
			Gdx.app.log(TAG, String.format("enemy radius : %f, enemy distance : %f ", eRadius, enemyDistance));

			enemy.setColliding(colliding);

		}

	}

	/**
	 * Updates specific weapon types
	 * 
	 * @param weapon
	 *            Weapon to be updated
	 */
	private void updateWeapon(Weapon weapon) {

		// specific update if weapon if a gun
		if (weapon instanceof Gun) {

			Gun gun = (Gun) weapon;

			Array<Bullet> activeBullets = gun.getActiveBullets();

			checkBulletCollisionWithWorld(activeBullets);
			checkBulletCollisionWithEnemies(activeBullets);

		}

	}

	/**
	 * Calculates intersection points of the bullets an the world mesh
	 * 
	 * @param bullets
	 *            List of bullets to check
	 */
	private void checkBulletCollisionWithWorld(Array<Bullet> bullets) {

		for (final Bullet bullet : bullets) {

			if (bullet.getCollisionPoint() == null) {

				Vector3 intersection = new Vector3();
				Array<Vector3> vecList = new Array<Vector3>();

				for (float[] verticies : city.getAllMeshVerticies()) {

					// populate list of intersections
					if (Intersector.intersectRayTriangles(bullet.getCollisionRay(), verticies, intersection)) {

						vecList.add(intersection);

					}

				}

				if (vecList.size > 0) {
					Vector3 closestCollision = Utils.ExtraMath.smallestVectorMagintude(vecList);
					bullet.setCollisionPoint(closestCollision); // set to the
																// closest
																// intersection
				} else {
				}

			}

			// Remove from list when past collision point
			if (bullet.isPastCollisionPoint()) {

				if (bullet.getCollisionPoint() != null) {

					float distance = bullet.getCollisionPoint().sub(this.player.getPosition()).len();

					playImpactSound(Utils.PhysicUtils.soundIntensity(distance * soundFactor));
				}

				bullets.removeValue(bullet, false);

			}

		}

	}

	/**
	 * Checks the collision of the bullets
	 * 
	 * @param bullets
	 *            The list of active bullets
	 */
	private void checkBulletCollisionWithEnemies(Array<Bullet> bullets) {
		
		BoundingBox bbox = new BoundingBox();
		
		// Cycle through all bullets and enemies
		for (final Bullet bullet : bullets) {

			for (final Enemy enemy : enemies) {

				if(bullet.getParent() == enemy) continue;
				
				ModelInfo.enemyModel.getBoundingBox(bbox); // reset bounding box
				enemy.translateBounds(bbox); // translate the bounding box to correct point

				Vector3 dim = bbox.getDimensions().cpy();// .scl(1f/2f);

				// calculate bullets & enemy distance from the bullet's origin
				// point
				Vector3 bulletOrigin = bullet.getOrigin();
				float bulletDistance = bullet.getPosition().cpy().sub(bulletOrigin).len();
				float enemyDistance = enemy.getPosition().cpy().sub(bulletOrigin).len();

				// bullet has not reached the enemy
				if (bulletDistance < enemyDistance)
					return;

				// bullet is the right distance away from the enemies
				// Note : dim.x is used because I know the enemies are a sphere
				if (bulletDistance >= enemyDistance && bulletDistance < (enemyDistance + dim.x)) {

					// bullet collides with enemies
					if (Intersector.intersectRayBoundsFast(bullet.getCollisionRay(), bbox)) {

						bullets.removeValue(bullet, false);
						enemy.removeHealth(player.getWeapon().getDamage());

						playImpactSound(Utils.PhysicUtils.soundIntensity(enemyDistance * soundFactor));

					}

				}

			}

		}

	}

	/**
	 * Randomly plays an impact sound
	 * */
	private void playImpactSound() {

		bulletImpacts[(int) (Math.random() * bulletImpacts.length)].play();

	}

	/**
	 * Randomly plays an impact sound with the specified volume
	 * */
	private void playImpactSound(float volume) {

		bulletImpacts[(int) (Math.random() * bulletImpacts.length)].play(volume);

	}

	/**
	 * @see com.badlogic.gdx.LifecycleListener#pause()
	 */
	@Override
	public void pause() {
	}

	/**
	 * @see com.badlogic.gdx.LifecycleListener#resume()
	 */
	@Override
	public void resume() {
	}

	/**
	 * @see com.badlogic.gdx.LifecycleListener#dispose()
	 */
	@Override
	public void dispose() {

		// Dispose sound
		for (Sound sound : bulletImpacts)
			sound.dispose();

	}

}
