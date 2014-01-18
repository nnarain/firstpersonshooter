/*

	Bullet fragment shader
	@author Natesh Narain

*/

varying float intensity;
varying vec3 color;

void main()
{

	vec4 final_color = vec4(normalize(color), 1);

	gl_FragColor = intensity * final_color;
}