
/*

	@author Natesh Narain

*/
attribute vec4 a_position;
attribute vec3 a_normal;
attribute vec2 a_texCoord0;

uniform mat4 u_projView;
uniform mat3 u_normal;

varying float intensity;
varying vec2 texCoord;

void main()
{

	vec3 n = normalize(u_normal * a_normal);
	vec3 l = normalize(vec3(1, 1, -1));
	intensity = max(dot(n, l), 0.0);
	
	texCoord = a_texCoord0;
	
	gl_Position = u_projView * a_position;

} 
