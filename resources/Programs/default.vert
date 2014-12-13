#version 110

uniform mat3 modelMat;
uniform mat3 projMat;

uniform vec4 color;

attribute vec2 vertPos;

void main() {
	vec3 transformed = projMat * modelMat * vec3(vertPos, 1);
	gl_Position = vec4(transformed.xy, 0, 1);
}