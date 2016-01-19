#version 150 core

uniform vec3 lightPosition;
uniform vec3 lightColor;

in vec4 pass_Position;
in vec4 pass_Normal;
in vec4 pass_Color;

out vec4 out_Color;

void main(void) {
    vec3 normal = normalize(vec3(pass_Normal.xyz));
    vec3 position = vec3(pass_Position.xyz);

    vec3 positionDiff = lightPosition - position;

    float brightness = dot(normal, positionDiff) / (length(positionDiff) * length(normal));
    brightness = clamp(brightness, 0, 1);

    vec3 reflectedColor = lightColor * pass_Color.rgb;

    out_Color = vec4(brightness * reflectedColor, pass_Color.a);
}