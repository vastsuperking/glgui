#version 110

uniform sampler2D diffuseSampler;

varying vec2 fragTexCoords;

void main() {
	gl_FragColor = texture2D(diffuseSampler, fragTexCoords);
}