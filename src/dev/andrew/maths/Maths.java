package dev.andrew.maths;


public class Maths {

	public static Matrix4f lookAt(Vector3f eye, Vector3f center, Vector3f up) {
		Vector3f forward = new Vector3f(0, 0, 0);
		forward = center.sub(eye);
		forward.normalize();
		
		Vector3f side = new Vector3f(0, 0, 0);
		side = forward.cross(up);
		side.normalize();
		
		up = side.cross(forward);
		
		Matrix4f m = new Matrix4f();
		m.m[0][0] = side.x;
		m.m[1][0] = side.y;
		m.m[2][0] = side.z;
		m.m[0][1] = up.x;
		m.m[1][1] = up.y;
		m.m[2][1] = up.z;
		m.m[0][2] = -forward.x;
		m.m[1][2] = -forward.y;
		m.m[2][2] = -forward.z;
		m.m[0][3] = eye.x;
		m.m[1][3] = eye.y;
		m.m[2][3] = eye.z;
		
		return m;
	}
	
	public static Matrix4f ortho(float left, float right, float bottom, float top, float near, float far) {
		Matrix4f ortho = new Matrix4f();
		ortho.initIdentity();

        float tx = -(right + left) / (right - left);
        float ty = -(top + bottom) / (top - bottom);
        float tz = -(far + near) / (far - near);

        ortho.m[0][0] = 2f / (right - left);
        ortho.m[1][1] = 2f / (top - bottom);
        ortho.m[2][2] = -2f / (far - near);
        ortho.m[0][3] = tx;
        ortho.m[1][3] = ty;
        ortho.m[2][3] = tz;
		
		return ortho;
	}
}
