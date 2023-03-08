package dev.andrew.maths;

public class Vector3f {

	public float x;
	public float y;
	public float z;
	
	public Vector3f(float x, float y, float z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public float length(){
		return (float) Math.sqrt(x * x + y * y + z * z);
	}
	
	public float max(){
		return Math.max(x, Math.max(y, z));
	}
	
	public float dot(Vector3f r){
		return x * r.x + y * r.y + z * r.z;
	}
	
	public Vector3f cross(Vector3f r){
		float x_ = y * r.z - z * r.y;
		float y_ = z * r.x - x * r.z;
		float z_ = x * r.y - y * r.x;
		
		return new Vector3f(x_, y_, z_);
	}
	
	public Vector3f normalize(){
		float length = length();
		
		return new Vector3f(x / length, y / length, z / length);
	}
	
	public Vector3f rotate(Vector3f axis, float angle){
		float sinAngle = (float) Math.sin(-angle);
		float cosAngle = (float) Math.cos(-angle);
		
		return this.cross(axis.mul(sinAngle)).add(
				(this.mul(cosAngle)).add(
						axis.mul(this.dot(axis.mul(1 - cosAngle)))));
	}
	
	public Vector3f lerp(Vector3f dest, float lerpFactor){
		return dest.sub(this).mul(lerpFactor).add(this);
	}
	
	public Vector3f add(Vector3f r){
		return new Vector3f(x + r.x, y + r.y, z + r.z);
	}
	
	public Vector3f add(float r){
		return new Vector3f(x + r, y + r, z + r);
	}
	
	public Vector3f sub(Vector3f r){
		return new Vector3f(x - r.x, y - r.y, z - r.z);
	}
	
	public Vector3f sub(float r){
		return new Vector3f(x - r, y - r, z - r);
	}
	
	public Vector3f mul(Vector3f r){
		return new Vector3f(x * r.x, y * r.y, z * r.z);
	}
	
	public Vector3f mul(float r){
		return new Vector3f(x * r, y * r, z * r);
	}
	
	public Vector3f div(Vector3f r){
		return new Vector3f(x / r.x, y / r.y, z / r.z);
	}
	
	public Vector3f div(float r){
		return new Vector3f(x / r, y / r, z / r);
	}
	
	public Vector3f abs(){
		return new Vector3f(Math.abs(x), Math.abs(y), Math.abs(z));
	}
	
	public String toString(){
		return "(" + x + ", " + y + ", " + z + " )";
	}
}
