package com.nnarain.fps.entities;

import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

/**
 * 
 * @author Natesh Narain
 * @version 3.0.2
 * @since April 29, 2013
 * 
 * */
public class Entity3D {

	protected Vector3 position;
	private Vector3 angle;

	private Matrix4 transform = new Matrix4();
	private Matrix4 normal = new Matrix4();

	/**
	 * Creates a default entity at (x = 0, y = 0, z = 0) with no rotation
	 */
	public Entity3D() {

		this(new Vector3(0, 0, 0), new Vector3(0, 0, 0));

	}

	/**
	 * @param position
	 * @param angle
	 */
	public Entity3D(Vector3 position, Vector3 angle) {

		this.position = position;
		this.angle = angle;

	}

	/**
	 * 
	 */
	public Entity3D(Vector3 position) {

		this(position, new Vector3(0, 0, 0));

	}

	/**
	 * Render with transformations
	 * 
	 * @since 3.0.0
	 */
	public void applyTransforms(GL10 gl) {

		gl.glTranslatef(position.x, position.y, position.z);

		gl.glRotatef(angle.x, 1, 0, 0);
		gl.glRotatef(angle.y, 0, 1, 0);
		gl.glRotatef(angle.z, 0, 0, 1);

	}

	public void applyTransforms(Matrix4 matrix) {

		matrix.translate(position);

		matrix.rotate(1, 0, 0, angle.x);
		matrix.rotate(0, 1, 0, angle.y);
		matrix.rotate(0, 0, 1, angle.z);

	}

	public Matrix4 getTransformMatrix() {

		transform.translate(position);

		transform.rotate(1, 0, 0, angle.x);
		transform.rotate(0, 1, 0, angle.y);
		transform.rotate(0, 0, 1, angle.z);

		return transform;

	}

	public Matrix3 getNormalMatrix() {

		normal.idt();

		normal.rotate(1, 0, 0, angle.x);
		normal.rotate(0, 1, 0, angle.y);
		normal.rotate(0, 0, 1, angle.z);

		return new Matrix3().set(normal.toNormalMatrix());

	}

	public void translateBounds(BoundingBox bbox){
		
		bbox.max.add(this.position);
		bbox.min.add(this.position);
		
	}
	
	/**
	 * @return the angle
	 */
	public Vector3 getAngle() {
		return angle;
	}

	/**
	 * @param angle
	 *            the angle to set
	 */
	public void setAngle(Vector3 angle) {
		this.angle = angle;
	}

	/**
	 * @return the position
	 */
	public Vector3 getPosition() {
		return position;
	}

	/**
	 * @param position
	 *            the position to set
	 */
	public void setPosition(Vector3 position) {
		this.position = position;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return position.toString();
	}

}