attribute vec4 a_position;
attribute vec4 a_color;
attribute vec2 a_texCoord0;

uniform mat4 u_projTrans;

varying vec4 v_color;
varying vec2 v_texCoords;
varying vec2 v_screenCoords;
//varying mat2 v_normalMatrix;
varying vec2 v_vecX, v_vecY;

void main() {
    vec4 pos = u_projTrans * a_position;
    gl_Position = pos;
    v_screenCoords = 0.5 * (pos.xy + vec2(1));
//    v_normalMatrix = mat2(/*transpose(inverse(*/u_projTrans/*))*/);
    mat4 normalMat = transpose(inverse(u_projTrans));
//    mat4 normalMat = u_projTrans;
    v_vecX = normalize((normalMat * vec4(1,0,0,0)).xy);
    v_vecY = normalize((normalMat * vec4(0,1,0,0)).xy);

    v_color = a_color;
    v_texCoords = a_texCoord0;
}
