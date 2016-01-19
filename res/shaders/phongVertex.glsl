#version 450 core

uniform mat4 projection;
uniform mat4 view;
uniform mat4 model;

in vec4 position;
in vec4 normal;
in vec4 color;

out vec4 pass_Position;
out vec4 pass_Normal;
out vec4 pass_Color;

void main(void) {
    //transform position and normal into worldSpace before passing to fragShader
    pass_Position = transpose(inverse(model)) * position;
    pass_Normal = transpose(inverse(model)) * normal;
    pass_Color = color;

    gl_Position = projection * view * model * position;
}