#version 330

uniform sampler2D u_texture;
uniform float u_blurRadius;

in vec2 v_texCoord;

out vec4 outColor;

void main() {
    // Gaussian kernel weights for a 5x5 kernel
    const float weights[5] = float[](0.06136, 0.24477, 0.38774, 0.24477, 0.06136);

    // Compute the texel size based on the blur radius
    vec2 texelSize = vec2(1.0 / textureSize(u_texture, 0)) * u_blurRadius;

    // Horizontal blur pass
    vec4 color = vec4(0.0);
    for (int i = -2; i <= 2; i++) {
        vec2 offset = vec2(float(i), 0.0) * texelSize;
        color += texture(u_texture, v_texCoord + offset) * weights[i + 2];
    }

    // Vertical blur pass
    outColor = vec4(0.0);
    for (int i = -2; i <= 2; i++) {
        vec2 offset = vec2(0.0, float(i)) * texelSize;
        outColor += color * weights[i + 2];
    }
}
