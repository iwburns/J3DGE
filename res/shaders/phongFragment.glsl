#version 450 core

const int MAX_LIGHTS = 10;

struct Light {
    bool isActive;
    vec3 position;
    vec3 color;
    float attenuation;
    float ambient;
};

uniform Light lights[MAX_LIGHTS];

uniform float materialSpecularCoefficient;
uniform vec3 materialSpecularColor;

uniform vec3 cameraPosition;

in vec4 pass_Position;
in vec4 pass_Normal;
in vec4 pass_Color;

out vec4 out_Color;

vec3 calculateColor(Light light, vec3 normal, vec3 surfacePos, vec3 surfaceColor, vec3 surfaceToCameraDir ) {
    if (light.isActive) {
        vec3 lightPosition = light.position;
        vec3 lightColor = light.color;
        float lightAttenuation = light.attenuation;
        float lightAmbient = light.ambient;

        vec3 surfaceToLightDir = normalize(lightPosition - surfacePos);

        //ambient
        vec3 ambient = lightAmbient * surfaceColor * lightColor;

        //diffuse
        float diffuseCoefficient = max(0.0, dot(normal, surfaceToLightDir));
        vec3 diffuse = diffuseCoefficient * surfaceColor * lightColor;

        //specular
        float specularCoefficient = 0.0;
        if (diffuseCoefficient > 0.0) {
            vec3 reflectionVector = reflect(-surfaceToLightDir, normal);
            float cosAngle = max(0.0, dot(surfaceToCameraDir, reflectionVector));
            specularCoefficient = pow(cosAngle, materialSpecularCoefficient);
        }
        vec3 specular = specularCoefficient * materialSpecularColor * lightColor;

        //attenuation
        float distanceToLight = length(lightPosition - surfacePos);
        float attenuation = 1.0 / (1.0 + lightAttenuation * pow(distanceToLight, 2));

        return ambient + (attenuation * (diffuse + specular));
        //    return ambient;
        //    return diffuse;
        //    return specular;
        //    return ambient + diffuse;
    }
    return vec3(0);
}

void main(void) {

    vec3 normal = normalize(vec3(pass_Normal.xyz));
    vec3 surfacePos = vec3(pass_Position.xyz);
    vec3 surfaceColor = pass_Color.rgb;
    vec3 surfaceToCameraDir = normalize(cameraPosition - surfacePos);

    vec3 linearColor = vec3(0);

    for (int i = 0; i < MAX_LIGHTS; i++) {
        linearColor += calculateColor(lights[i], normal, surfacePos, surfaceColor, surfaceToCameraDir);
    }

    vec3 gamma = vec3(1.0/2.2);
    out_Color = vec4(pow(linearColor, gamma), pass_Color.a);
}