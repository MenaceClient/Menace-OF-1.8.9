#version 330

layout (location = 0) in vec3 position;
layout (location = 1) in vec2 texCoord;
layout (location = 2) in vec4 color;

out vec4 vertexColor;
out vec2 outTexCoord;

uniform mat4 projectionMatrix;

void main()
{
    vertexColor = color;
    outTexCoord = texCoord;

    gl_Position = projectionMatrix * vec4(position.x, position.y, position.z, 1.0);
}
