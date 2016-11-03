package boids;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

import sim.engine.Grid;
import sim.entity.Entity;
public class Projectile extends Entity{
	private int size  = 3;
	private double dx,dy,dz;
//	double[] v1 = {1,1,1}, v2 = {1,1,-1}, v3 = {-1,1,-1}, v4 = {-1,1,1}, v5 = {-1,-1,1}, v6 = {1,-1,1}, v7 = {1,-1,-1}, v8 = {-1,-1,-1};
	public Projectile(double x, double y, double z, double dx, double dy, double dz, double velocity){
		this.velocity = velocity;
		this.x = x; this.y = y; this.z = z;
		this.dx = dx; this.dy = dy; this.dz = dz;
		collisionRadius = size;
	}
	public Projectile(double x, double y, double z, double dx, double dy, double dz){
		this(x, dy, z, dx, dy, dz, 5);
	}
	public void tick(ArrayList<Entity> entities) {
		x+= velocity*dx;
		y+= velocity*dy;
		z+=	velocity*dz;
	}
	public void render(Graphics g, Grid grid) {
		g.setColor(Color.RED);
		Point p = grid.getXy()[(int)x][(int) y];
		g.fillOval(p.x,(int)(p.y-z), size, size);
	}
}
