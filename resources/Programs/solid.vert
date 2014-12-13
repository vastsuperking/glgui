#version 110

uniform mat3 modelMat;
uniform mat3 projMat;

uniform vec4 color;

attribute vec2 vertPos;

varying vec4 fragColor;

void main() {
	vec3 transformed = projMat * modelMat * vec3(vertPos, 1);
	
	fragColor = color;
	
	gl_Position = vec4(transformed.xy, 0, 1);
}