#version 330 core

out vec4 FragColor;
in vec4 vertexColor;

void main() {
    vec3 color = vertexColor.xyz;
    color = fract(color * 2.0f);

    FragColor = vec4(color, 1.0f);
}