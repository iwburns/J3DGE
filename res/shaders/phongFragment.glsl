#version 150 core

uniform vec3 lightPosition;
uniform vec3 lightColor;

in vec4 pass_Position;
in vec4 pass_Normal;
in vec4 pass_Color;

out vec4 out_Color;

void main(void) {
    vec3 normal = normalize(vec3(pass_Normal.xyz));
    vec3 surfacePos = vec3(pass_Position.xyz);
    vec3 surfaceColor = pass_Color.rgb;
    float surfaceAlpha = pass_Color.a;

    vec3 surfaceToLight = normalize(lightPosition - surfacePos);

    float diffuseCoefficient = max(0.0, dot(normal, surfaceToLight));

    out_Color = vec4(diffuseCoefficient * lightColor * surfaceColor, surfaceAlpha);
}