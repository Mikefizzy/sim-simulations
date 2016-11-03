package boids;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

import sim.engine.Grid;
import sim.entity.Entity;
import sim.tools.Axis;
import sim.tools.Calculations;
public class Boid extends Entity{
	private int size = 5;
	private double[] v2 = {0,1,0};
	private double[] v1 = {0,-1,0};
	private double[] v3 = {0,1,1};
	private double[] v4 = {-1, 2*(Math.sqrt(3)/2),0};
	private double[] v5 = {1, 2*(Math.sqrt(3)/2),0};
			double ax,ay,az,
			dx,dy,dz;
	private Grid grid;
	private int sx ,sy, sz;
	public Boid(int x, int y, int z, int size, double velocity, Grid grid){
		this.x = x;
		this.y = y;
		this.z = z;
		this.sx = x;
		this.sz = z;
		this.sy = y;
		this.size  = size;
		this.velocity = velocity;
		this.grid = grid;
		collisionRadius = 5;
	}
	public Boid(int x, int y, int z, int size, Grid grid){
		this(x, y, z, size, 1, grid);
	}
	private boolean isPercieved(double angle, Entity e){
		double[] v2 = {e.x - x, e.y - y, e.z - z};
		double[] unitVector = Calculations.unitVector(v2);
		double[] delta = {dx,dy,dz};
		double rads = Math.acos(Calculations.dotProduct(delta, unitVector));
		double theta = Math.abs(Calculations.fromRads(rads));
		return (theta<=angle);
	}
	public void tick(ArrayList<Entity> entities) {
		double[] v = v1;
		v = Calculations.rotate(v, Axis.X, Math.toRadians(ax));
		v = Calculations.rotate(v, Axis.Z, Math.toRadians(az));
		dx = v[0];
		dy = v[1];
		dz = v[2];
		double[] proximity = proximity();
		int index = Calculations.vectorMaxIndex(proximity);
		
		double turnRate = ((500*velocity)/proximity[0]);
		System.out.println(turnRate);
		switch(index){
		case 1:
			az += turnRate;
			break;
		case 2:
			az -=turnRate;
			break;
		case 3:
			ax +=turnRate;
			break;
		case 4:
			ax -=turnRate;
			break;
		}
		
		for(Entity e: entities){
			if(isPercieved(30, e)){
				double[] v2 = {e.x - x, e.y - y, e.z - z};
				double[] unitVector = Calculations.unitVector(v2);
				dx = unitVector[0];
				dy = unitVector[1];
				dz = unitVector[2];
				break;
			}
		}
		
		z+= velocity*dz;
		x+= velocity*dx;
		y+= velocity*dy;
		
	}
	public void render(Graphics g, Grid grid) throws ArrayIndexOutOfBoundsException{
		Point[][] xy = grid.getXy();
		double[][]V = {v1,v4,v2,v5,v1,v3,v2};
		for(int k = 0; k<V.length; k++)
			V[k] = Calculations.rotate(V[k], Axis.X, Math.toRadians(ax));
		for(int k = 0; k<V.length; k++)
			V[k] = Calculations.rotate(V[k], Axis.Y, Math.toRadians(ay));
		for(int k = 0; k<V.length; k++)
			V[k] = Calculations.rotate(V[k], Axis.Z, Math.toRadians(az));
		Point origin = xy[(int) x][(int) y];
		boolean showLines = false;
		if(showLines){
		double[] t = proximity();
		double[][] dMatrix = dMatrix(); 
		for(int k = 0;k<5; k++){ 
			if(k>0)
				g.setColor(Color.red);
			else
				g.setColor(Color.BLUE);
			Point q = xy[(int) (x + dMatrix[k][0]*t[k])][(int) (y + dMatrix[k][1]*t[k])];
			g.drawLine(origin.x, (int) (origin.y-z), q.x, q.y -(int) (z + dMatrix[k][2]*t[k]));
		}
		}
		g.setColor(Color.BLACK);
		for(int k = 0; k<V.length-1; k++){
			double[] v = V[k];
			double[] v2 =V[k+1];
			Point p = xy[(int) (v[0]*size+x)] [(int) (v[1]*size + y)];
			Point q = xy[(int) (v2[0]*size+x)] [(int) (v2[1]*size + y)];
			g.drawLine(p.x, p.y - (int) (v[2]*size + z), q.x, q.y -(int) (v2[2]*size + z)); 
		}
		
		
	}
	private double[][] dMatrix(){
		double[] dVector  = {dx,dy,dz};
		double fov = 60;
		double[][] dMatrix = {dVector,
				Calculations.rotate(Calculations.rotate(v1, Axis.X, Math.toRadians(ax)), Axis.Z,  Math.toRadians(az + fov)),
				Calculations.rotate(Calculations.rotate(v1, Axis.X, Math.toRadians(ax)), Axis.Z,  Math.toRadians(az - fov)),
				Calculations.rotate(Calculations.rotate(v1, Axis.X, Math.toRadians(ax + fov)), Axis.Z, Math.toRadians(az)),
				Calculations.rotate(Calculations.rotate(v1, Axis.X, Math.toRadians(ax - fov)), Axis.Z,  Math.toRadians(az))};
		return dMatrix;
	}
	private double[] proximity(){
		double[] proximities  = new double[5];
		double[][] dMatrix = dMatrix();
		for(int k = 0; k<proximities.length;k++){
		boolean oob = false;
		int t = 0, xMax  = grid.getXy().length-1, yMax  = grid.getXy()[0].length-1, zMax = 500;
		while(!oob){
			t++;
			int x2 = (int)(x  + dMatrix[k][0]*t);
			int y2 = (int)(y + dMatrix[k][1]*t);
			int z2 = (int)(z + dMatrix[k][2]*t);
			if(x2>xMax || x2<0 || y2>yMax || y2<0|| z2>zMax|| z2<0){
				t = t-1;
				break;
			}
		}
		proximities[k] = t;
		}
		return proximities;
	}
	public void reset(){
		this.x = sx;
		this.y = sy;
		this.z = sz;
	}
}