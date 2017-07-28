#ifdef GL_ES
    precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform mat4 u_projTrans;

uniform vec2 u_delta_1;
uniform vec2 u_delta_2;
uniform vec2 u_delta_3;

const vec3 c_cc_1 = vec3(0.8, 0.1, 0.1);
const vec3 c_cc_2 = vec3(0.1, 0.8, 0.1);
const vec3 c_cc_3 = vec3(0.1, 0.1, 0.8);

void main() {
        vec2 df = v_texCoords - vec2(0.5, 0.5);
        df = vec2(.25, .25) - df * df;
        float deltaFix = 4. * df.x * df.y;

        vec3 color =
            texture2D(u_texture, v_texCoords + u_delta_1 * deltaFix).rgb * c_cc_1 +
            texture2D(u_texture, v_texCoords + u_delta_2 * deltaFix).rgb * c_cc_2 +
            texture2D(u_texture, v_texCoords + u_delta_3 * deltaFix).rgb * c_cc_3;

        gl_FragColor = vec4(color, 1.0);
}
