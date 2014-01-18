package com.nnarain.fps.entities;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.g3d.model.still.StillModel;
import com.badlogic.gdx.graphics.g3d.model.still.StillSubMesh;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.utils.Array;
import com.nnarain.fps.entities.Player.State;

/**
 * @author Natesh Narain
 * @version 0.0.1
 * @since 2013-05-23
 */
public class City extends Entity3D {

	private static final String TAG = "City";

	public static final String GROUND = "Plane_Plane.001";
	public static final String BOB = "Bob_Cube";
	public static final String STAIRS = "Mesh_Stais_Cube.002";

	public final StillModel cityModel;

	/**
	 * 
	 */
	public City(StillModel cityModel) {

		super();

		this.cityModel = cityModel;

	}

	public void checkElevation(Player player) {

		Mesh groundMesh = cityModel.getSubMesh(GROUND).mesh;
		Mesh stairsMesh = cityModel.getSubMesh(STAIRS).mesh;

		adjustHeightAboveMesh(player, groundMesh);
		adjustHeightAboveMesh(player, stairsMesh);

	}

	/**
	 * Corrects the players height above the given mesh
	 * 
	 * @param player
	 *            The Player
	 * @param mesh
	 * */
	public void adjustHeightAboveMesh(Player player, Mesh mesh) {

		float[] vertices = new float[mesh.getNumVertices() * 6];
		mesh.getVertices(vertices);

		Vector3 pos = player.getPosition();
		Vector3 focus = player.getFocus();

		Ray ray = new Ray(pos.cpy(), Vector3.Y.cpy().scl(-1));
		Vector3 intersection = new Vector3();

		if (Intersector.intersectRayTriangles(ray, vertices, intersection)) {

			float yDiff = pos.y - intersection.y;

			State playerState = player.getState();

			if ((playerState == State.WALK || playerState == State.RUN)) {

				float y = 0f;

				if (yDiff < Player.STAND_HEIGHT) {

					y = (intersection.y + Player.STAND_HEIGHT) - pos.y;

					// pos.y += y;
					// focus.y += y;
					player.translate(0, y, 0);

				} else if (yDiff > Player.STAND_HEIGHT) {

					y = pos.y - (intersection.y + Player.STAND_HEIGHT);

					// pos.y -= y;
					// focus.y -= y;
					player.translate(0, -y, 0);

				}

			} else if (playerState == State.CROUCH) {

				float y = 0f;

			}

		}

	}

	/** @return Bounds of all meshes in city model */
	public BoundingBox[] getAllMeshBounds() {

		StillSubMesh[] subMeshes = cityModel.subMeshes;

		BoundingBox[] bounds = new BoundingBox[subMeshes.length];

		int len = subMeshes.length;

		for (int i = 0; i < len; i++) {

			bounds[i] = new BoundingBox();

			subMeshes[i].getBoundingBox(bounds[i]);

		}

		return bounds;

	}

	/**
	 * @return Vertex coordinates of all vertices in the city model
	 * 
	 * */
	public Array<float[]> getAllMeshVerticies() {

		StillSubMesh[] subMeshes = cityModel.subMeshes;

		int numMeshes = subMeshes.length;

		Array<float[]> allVerticies = new Array<float[]>();

		for (int i = 0; i < numMeshes; i++) {

			Mesh mesh = subMeshes[i].mesh;
			float[] verticies = new float[mesh.getNumVertices() * 6];

			mesh.getVertices(verticies);

			allVerticies.add(verticies);

		}

		return allVerticies;

	}

	/**
	 * @return the cityModel
	 */
	public StillModel getCityModel() {
		return cityModel;
	}

	public Mesh getElementMesh(String name) {

		return cityModel.getSubMesh(name).mesh;

	}

	public BoundingBox getElementBounds(String name) {

		BoundingBox bbox = new BoundingBox();

		StillSubMesh subMesh = cityModel.getSubMesh(name);

		subMesh.getBoundingBox(bbox);

		return bbox;

	}

}
