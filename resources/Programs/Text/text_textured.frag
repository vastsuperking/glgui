#version 110

uniform sampler2D diffuseSampler;
uniform sampler2D glyphSampler;

varying vec2 fragTexCoords;

void main() {
	gl_FragColor = texture2D(diffuseSampler, fragTexCoords) * texture2D(glyphSampler, fragTexCoords);
}