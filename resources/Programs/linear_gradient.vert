#version 110

uniform mat3 modelMat;
uniform mat3 projMat;

attribute vec2 vertPos;
attribute vec4 vertColor;

varying vec4 fragColor;

void main() {
	vec3 transformed = projMat * modelMat * vec3(vertPos, 1);
	
	fragColor = vertColor;
	
	gl_Position = vec4(transformed.xy, 0, 1);
}