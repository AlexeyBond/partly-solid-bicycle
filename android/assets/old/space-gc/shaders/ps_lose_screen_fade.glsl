#ifdef GL_ES
    precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform sampler2D u_distortionTexture;
uniform mat4 u_projTrans;
uniform float u_time;

void main() {
    float t = 0.5 * u_time;
    float a = float(int(t));
    float b = t - a;

    vec4 distortion = texture2D(u_distortionTexture, v_texCoords);
    vec4 shiftDistortion = texture2D(u_distortionTexture,
            v_texCoords + vec2(0.327, 0.542) * a + vec2(0.0132, -0.0143) * b * (1.0 - distortion.z));

    vec4 color = texture2D(u_texture, v_texCoords + vec2(0.054, 0.041) * shiftDistortion.z);
    vec4 grayscale = vec4(1,1,1,1) * dot(color, vec4(0.3, 0.3, 0.3, 0));
    float k = clamp(min(t * 0.5, 1.0) - shiftDistortion.x * 0.4, 0.0, 1.0);
    gl_FragColor = mix(color, grayscale, k)
            * vec4(1.0, 1.0 - distortion.y * k, 1.0 - distortion.y * k, 1.0);
}
