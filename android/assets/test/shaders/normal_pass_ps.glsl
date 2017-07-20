#ifdef GL_ES
    precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;

//varying mat2 v_normalMatrix;
varying vec2 v_vecX, v_vecY;

vec3 decodeNormal(vec4 color) {
    return 2.0 * (color.rgb - vec3(0.5));
}

vec3 encodeNormal(vec3 normal) {
    return 0.5 * (normal + vec3(1));
}

void main() {
    vec4 textureSample = texture2D(u_texture, v_texCoords);
    vec3 normal = decodeNormal(textureSample);
//    normal = vec3(v_normalMatrix * normal.xy, normal.z);
    normal = vec3(v_vecX * normal.x + v_vecY * normal.y, normal.z);
    normal = normalize(normal);
//    normal *= vec3(-1,1,1);
    gl_FragColor = vec4(encodeNormal(normal), textureSample.a);
}
