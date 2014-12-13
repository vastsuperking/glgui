#version 110

uniform vec4 color;
uniform sampler2D glyphSampler;

varying vec2 fragTexCoords;

void main() {
	gl_FragColor = color * texture2D(glyphSampler, fragTexCoords);
}