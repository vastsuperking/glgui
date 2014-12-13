#version 110

uniform mat3 modelMat;
uniform mat3 projMat;

attribute vec2 vertPos;
attribute vec2 vertTexCoords;

varying vec2 fragTexCoords;

void main() {
	vec3 transformed = projMat * modelMat * vec3(vertPos, 1);
	
	fragTexCoords = vertTexCoords;
	
	gl_Position = vec4(transformed.xy, 0, 1);
}