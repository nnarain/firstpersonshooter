package com.nnarain.fps.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.nnarain.fps.entities.weapon.AssaultRifle;
import com.nnarain.fps.entities.weapon.Weapon;
import com.nnarain.fps.utils.PS3Map;
import com.nnarain.fps.utils.Utils;

/**
 * @author Natesh Narain
 * @version 0.0.1
 * @since 2013-05-20
 */

// TODO : Controller config needed

public class Player /*extends ControllerAdapter*/extends Entity3D implements ControllerListener {

	private static final String TAG = "Player";

	/**This player's position*/
//	private Vector3 position;
	/**Direction of player*/
	private Vector3 direction;
	/**Vector perpendicular to direction*/
	private Vector3 strafeDirection;
	/**Where player is looking*/
	private Vector3 focus;
	/**Player's direction*/
	private Vector3 velocity;

	/**Player's weapon*/
	private Weapon weapon;

	private float theta = 180f;

	private boolean jumping = false;

	/**Player's state*/
	private State state;

	private Controller controller;
	
	private static final float FOCUS_RADIUS = 3f;
	private static final float WEAPON_RADIUS = .05f;

	private static final float CROUCH_SPEED = 3f;
	private static final float WALK_SPEED = 5f;
	private static final float RUN_SPEED = 8f;
	private static final float VERTICLE_SPEED = 0f;

	private static final float LOOK_SPEED = 6f;

	public static final float STAND_HEIGHT = 2f;
	public static final float CROUCH_HEIGHT = 1f;
	public static final float PRONE_HEIGHT = 0f;

	public enum State {
		WALK, CROUCH, PRONE, RUN;
	}

	/**
	 * 
	 */
	public Player(float x, float z) {

		this.position = new Vector3(x, STAND_HEIGHT, z);
		this.focus = new Vector3(position.x, position.y, position.z - FOCUS_RADIUS);

		this.direction = focus.cpy().sub(this.position).nor();

		this.strafeDirection = new Vector3();
		this.calculateStrafeDirection();

		this.velocity = new Vector3();
		this.velocity.y = VERTICLE_SPEED;

		this.setWeapon(new AssaultRifle(new Vector3(x, STAND_HEIGHT - .5f, z - WEAPON_RADIUS), this));
		this.weapon.setSound(Gdx.audio.newSound(Gdx.files.internal("data/sfx/shot.wav")));
		// this.setWeapon(new AssaultRifle(new Vector3(
		// MathUtils.cosDeg(45) + WEAPON_RADIUS,STAND_HEIGHT-.5f,
		// MathUtils.sinDeg(45) * WEAPON_RADIUS)));

		this.setState(State.WALK);
		
		Controllers.addListener(this);

		// DEBUG
		Gdx.app.log(TAG, "controllers : " + Controllers.getControllers().size);
		
	}

	public void update(float delta) {

		for (Controller controller : Controllers.getControllers()) {

			/* Polling the analog sticks works better than event handling */

			float value;

			value = controller.getAxis(PS3Map.STICK_LEFT_VERTICLE);
			advance(-(value * delta * velocity.x));

			value = controller.getAxis(PS3Map.STICK_LEFT_HORIZONTAL);
			strafe(value * delta * velocity.x);

			value = controller.getAxis(PS3Map.STICK_RIGHT_VERTICLE);
			lookVerticle(value * delta);

			value = controller.getAxis(PS3Map.STICK_RIGHT_HORIZONTAL);
			lookHorizontal(value * delta);

		}
		
		this.weapon.update(delta);
		
		this.keyBoardControl(delta);

	}

	private void keyBoardControl(float delta){
		
		if(Gdx.input.isTouched()){
			this.weapon.fire();
			try{Thread.sleep(10);}catch(InterruptedException e){}
		}
		
		if(Gdx.input.isKeyPressed(Input.Keys.W)){
			advance(5f * delta * velocity.x);
		}else if(Gdx.input.isKeyPressed(Input.Keys.S)){
			advance(-5f * delta * velocity.x);
		}else if(Gdx.input.isKeyPressed(Input.Keys.A)){
			strafe(-5f * delta * velocity.x);
		}else if(Gdx.input.isKeyPressed(Input.Keys.D)){
			strafe(5f * delta * velocity.x);
		}else if(Gdx.input.isKeyPressed(Input.Keys.UP)){
			lookHorizontal(delta + LOOK_SPEED);
		}
		
	}
	
	/**
	 * Moves the focal point in a circle around the player's position
	 * 
	 * @param value
	 *            Value from the analog stick
	 */
	private void lookHorizontal(float value) {

		theta += -(value * LOOK_SPEED * 15f);

		Utils.ExtraMath.moveInCircleXZ(this.focus, this.position, FOCUS_RADIUS, theta);
		Utils.ExtraMath.moveInCircleXZ(this.weapon.getPosition(), this.position, WEAPON_RADIUS, theta);

		this.direction = this.focus.cpy().sub(this.position).nor();

		calculateStrafeDirection();
		calculateWeaponDirection();

		// keep theta between 0 & 360
		if (theta < 0)
			theta += 360;
		
		if (theta >= 360)
			theta = 0;

	}

	/**Move the focus point up and down*/
	private void lookVerticle(float value) {

		this.focus.add(0, -(value * LOOK_SPEED), 0);
		this.direction = this.focus.cpy().sub(this.position).nor();
		
		if (this.focus.y <= 0)
			this.focus.y = 0;

	}

	/**Move player along its direction vector*/
	public void advance(float value) {

		this.direction.scl(value);
		this.translate(this.direction.x, 0, this.direction.z);
		this.direction.scl(1 / value);

	}

	/**Moves player along the strafe direction vector*/
	public void strafe(float value) {

		this.strafeDirection.scl(value);
		this.translate(this.strafeDirection);
		this.strafeDirection.scl(1 / value);

	}

	/**Calculates the vector perpendicular to the direction vector*/
	private void calculateStrafeDirection() {

		this.strafeDirection.set(this.direction.x, 0, this.direction.z);
		this.strafeDirection.crs(Vector3.Y);

	}

	/**Calculates the weapons direction and its new angle*/
	private void calculateWeaponDirection() {

		// Direction weapon is facing, important for projectile direction
		Vector3 direction = this.focus.cpy().sub(this.weapon.getPosition());
		this.weapon.getDirection().set(direction).nor();

		Vector3 angle = this.weapon.getAngle();
		
		// y-axis rotation
		angle.y = this.theta;
		
		//calculate up & down angle
		Vector3 dirNoY = direction.cpy();
		dirNoY.y = 0;
	
		float theta = (float) (Math.acos(Utils.ExtraMath.cos_theta(direction, dirNoY)) * MathUtils.radiansToDegrees);
		
		angle.x = theta * this.strafeDirection.x;
		angle.z = theta * this.strafeDirection.z;

	}

	/**
	 * @see com.badlogic.gdx.controllers.ControllerAdapter#buttonUp(com.badlogic.gdx.controllers.Controller,
	 *      int)
	 */
	@Override
	public boolean buttonUp(Controller controller, int buttonIndex) {

		switch (buttonIndex) {

		case PS3Map.BUTTON_L3:

			this.setState(State.WALK);

			break;

		}

		return true;
	}

	/**
	 * @see com.badlogic.gdx.controllers.ControllerAdapter#buttonDown(com.badlogic.gdx.controllers.Controller,
	 *      int)
	 */
	@Override
	public boolean buttonDown(Controller controller, int buttonIndex) {

		switch (buttonIndex) {

		case PS3Map.BUTTON_X:

			if (!jumping)
				jumping = true;

			break;

		case PS3Map.BUTTON_L3:

			this.setState(State.RUN);

			break;

		case PS3Map.TRIGGER_R1:

			this.weapon.fire();

			break;

		// DEBUG
		case PS3Map.TRIGGER_R2:

			break;

		}

		return true;
	}

	public void translate(Vector3 vector) {

		this.translate(vector.x, vector.y, vector.z);

	}

	public void translate(float x, float y, float z) {

		this.position.add(x, y, z);
		this.focus.add(x, y, z);
		this.weapon.getPosition().add(x, y, z);

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

	public void setState(State state) {

		this.state = state;

		if (state == State.WALK) {

			velocity.x = WALK_SPEED;

		} else if (state == State.CROUCH) {

			velocity.x = CROUCH_SPEED;

		} else if (state == State.RUN) {

			velocity.x = RUN_SPEED;

		}

	}

	/**
	 * @return the position
	 */
	public Vector3 getPosition() {
		return position;
	}

	/**
	 * @return the direction
	 */
	public Vector3 getDirection() {
		return direction;
	}

	/**
	 * @return
	 */
	public Vector3 getVelocity() {
		return this.velocity;
	}

	public State getState() {
		return this.state;
	}

	/**
	 * @return the focus
	 */
	public Vector3 getFocus() {
		return focus;
	}

	/**
	 * @see com.badlogic.gdx.controllers.ControllerListener#connected(com.badlogic.gdx.controllers.Controller)
	 */
	@Override
	public void connected(Controller controller) {
	}

	/**
	 * @see com.badlogic.gdx.controllers.ControllerListener#disconnected(com.badlogic.gdx.controllers.Controller)
	 */
	@Override
	public void disconnected(Controller controller) {
	}

	/**
	 * @see com.badlogic.gdx.controllers.ControllerListener#axisMoved(com.badlogic.gdx.controllers.Controller, int, float)
	 */
	@Override
	public boolean axisMoved(Controller controller, int axisCode, float value) {
		return false;
	}

	/**
	 * @see com.badlogic.gdx.controllers.ControllerListener#povMoved(com.badlogic.gdx.controllers.Controller, int, com.badlogic.gdx.controllers.PovDirection)
	 */
	@Override
	public boolean povMoved(Controller controller, int povCode, PovDirection value) {
		return false;
	}

	/**
	 * @see com.badlogic.gdx.controllers.ControllerListener#xSliderMoved(com.badlogic.gdx.controllers.Controller, int, boolean)
	 */
	@Override
	public boolean xSliderMoved(Controller controller, int sliderCode, boolean value) {
		return false;
	}

	/**
	 * @see com.badlogic.gdx.controllers.ControllerListener#ySliderMoved(com.badlogic.gdx.controllers.Controller, int, boolean)
	 */
	@Override
	public boolean ySliderMoved(Controller controller, int sliderCode, boolean value) {
		return false;
	}

	/**
	 * @see com.badlogic.gdx.controllers.ControllerListener#accelerometerMoved(com.badlogic.gdx.controllers.Controller, int, com.badlogic.gdx.math.Vector3)
	 */
	@Override
	public boolean accelerometerMoved(Controller controller, int accelerometerCode, Vector3 value) {
		return false;
	}

}
