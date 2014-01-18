package com.nnarain.fps.utils;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

/**
 * @author Natesh Narain
 * @version 0.0.1
 * @since 2013-06-01
 */
public class Utils {

	/**
	 * Extra mathy functions
	 * */
	public static class ExtraMath{
		
		/**
		 * A really fun method for determining the vector with the smallest magnitude in the list
		 * 
		 * @param list 
		 * 			List of vectors
		 * */
		public final static Vector3 smallestVectorMagintude(Array<Vector3> list){
			
			Vector3 v1 = list.get(0);
			
			if(list.size == 1) return v1;
			
			Vector3 v2 = list.get(1);
			
			Vector3 small = (v1.len() < v2.len()) ? v1 : v2; // initial small value
			
			int size = list.size;
			
			for(int i = 2; i < size; i++){
				
				Vector3 v = list.get(i);
				
				small = (small.len() < v.len()) ? small : v;
				
			}
			
			return small;
			
		}
		
		/**
		 * Uses parametric equation of circle to move the given vector around the center specified
		 * */
		public final static void moveInCircleXZ(Vector3 point, Vector3 center, float radius, float angle) {

			float x = (radius * MathUtils.sinDeg(angle)) + center.x;
			float z = (radius * MathUtils.cosDeg(angle)) + center.z;
			float y = point.y;

			point.set(x, y, z);

		}
		
		/**Calculates cos theta between the given vectors*/
		// TODO : Return theta?
		public final static float cos_theta(Vector3 a, Vector3 b){
			
			return (a.dot(b))/(a.len() * b.len());
			
		}
		
	}
	
	/**
	 * Utility class containing functions useful for physic calculations
	 * */
	public static class PhysicUtils{
		
		public static float soundIntensity(float radius){
			
			return MathUtils.clamp(1f/(float)(Math.pow(radius, 2)), 0, 1);
			
		}
		
	}
	
}
