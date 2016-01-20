#version 150 core

uniform vec3 lightPosition;
uniform vec3 lightColor;
uniform vec3 cameraPosition;

in vec4 pass_Position;
in vec4 pass_Normal;
in vec4 pass_Color;

out vec4 out_Color;

void main(void) {
    float materialShininess = 1000;
    float ambientCoefficient = 0.1;
    float lightAttenuation = 0.01;

    vec3 normal = normalize(vec3(pass_Normal.xyz));
    vec3 surfacePos = vec3(pass_Position.xyz);
    vec3 surfaceColor = pass_Color.rgb;
    float surfaceAlpha = pass_Color.a;

    //ambient
    vec3 ambient = ambientCoefficient * lightColor * surfaceColor;

    //diffuse
    vec3 surfaceToLight = normalize(lightPosition - surfacePos);
    float diffuseCoefficient = max(0.0, dot(normal, surfaceToLight));
    vec3 diffuse = diffuseCoefficient * lightColor * surfaceColor;

    //specular
    float specularCoefficient = 0.0;
    if (diffuseCoefficient > 0.0) {
        vec3 reflectionVector = reflect(-surfaceToLight, normal);
        vec3 surfaceToCamera = normalize(cameraPosition - surfacePos);
        float cosAngle = max(0.0, dot(surfaceToCamera, reflectionVector));
        specularCoefficient = pow(cosAngle, materialShininess);
    }
    vec3 specular = specularCoefficient * surfaceColor * lightColor;

    //attenuation
    float distanceToLight = length(lightPosition - surfacePos);
    float attenuation = 1.0 / (1.0 + lightAttenuation * pow(distanceToLight, 2));


    out_Color = vec4(ambient, surfaceAlpha) + (attenuation * vec4(diffuse + specular, surfaceAlpha));
}