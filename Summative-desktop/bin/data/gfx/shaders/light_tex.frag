
varying vec2 texCoord;
varying float intensity;

uniform sampler2D u_texture;

void main()
{
	gl_FragColor = intensity * texture2D(u_texture, texCoord);
}
