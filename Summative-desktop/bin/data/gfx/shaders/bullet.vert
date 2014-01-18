/*
	Vertex shader for bullets
	@author Natesh Narain
*/

attribute vec4 a_position;
attribute vec3 a_normal;

uniform mat4 u_projView;
uniform mat3 u_normal;

varying float intensity;
varying vec3 color;

void main(void){

	vec3 n = normalize(u_normal * a_normal);
	vec3 l = normalize(vec3(1, 1, -1));
	intensity = max(dot(n, l), 0.0);
	
	color = a_position.xyz;
	
	gl_Position = u_projView * a_position;

}