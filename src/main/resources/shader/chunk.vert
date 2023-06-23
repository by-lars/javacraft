#version 330 core

layout(location = 0) in vec3 aPos;

uniform mat4 uView;
uniform mat4 uProj;

out vec4 vertexColor;

void main() {
    gl_Position = uProj * uView * vec4(aPos, 1.0f);
    vertexColor = vec4((aPos.yxz + 1.0f) / 2.0f, 1.0f);
}