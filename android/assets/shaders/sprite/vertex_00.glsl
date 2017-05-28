attribute vec4 a_position;
attribute vec4 a_color;
attribute vec2 a_texCoord0;

uniform mat4 u_projTrans;

varying vec4 v_color;
varying vec2 v_texCoord0;
varying vec2 v_position;

void main() {
    v_color = a_color;
    v_texCoord0 = a_texCoord0;

    vec4 pos = u_projTrans * a_position;
    v_position = pos.xy;
    gl_Position = pos;
}
