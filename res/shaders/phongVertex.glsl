#version 450 core

uniform mat4 projection;
uniform mat4 view;
uniform mat4 model;

in vec4 position;
in vec4 color;

out vec4 pass_Color;

void main(void) {
    gl_Position = projection * view * model * position;
    pass_Color = color;
}