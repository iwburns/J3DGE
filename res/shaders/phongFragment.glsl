#version 150 core

in vec4 pass_Position;
in vec4 pass_Normal;
in vec4 pass_Color;

out vec4 out_Color;

void main(void) {
    vec4 someVector = pass_Position + pass_Normal;
    out_Color = pass_Color + someVector;
}