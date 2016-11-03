package logic.engine;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import sim.engine.Engine;

public class NetEngine extends Engine{
	private double[][][] w;
	private double[][]x;
	private double[] trailViewX, trailViewY;
	private boolean drawLines = false, drawSideLines = true, trailView = true;
	private int layerDistance = 100;
	private String cost = "", grads  = "";
	public NetEngine(double[][][] w, double[][] x) {
		super(2);
		this.x = x;
		this.w = w;
	}
	public void onStart() {
	}
	public void tick() {
	}
	public void onRender(Graphics g) {
		double amplitude = 5;
		int zOffset = 100;
		int trailViewOffset = 450;
		int nodeSize = 20;
		int trailViewSpace = 30;
		this.renderGrid(g, 2);
		Point[][] xy = this.getGrid().getXy();
		g.setColor(Color.black);
		for(int i = 0; i<w.length; i++)
			for(int j = 0; j<w[i].length; j++)
				for(int k =0; k<w[i][j].length;k++){
					int weightDistance = 8;
					int unitDistance  = 40;//(500/((w[i][j].length+weightDistance/2)));
					//System.out.println(unitDistance);
				Point p = xy[j*unitDistance +k*weightDistance ][ i*layerDistance ];
				if(drawLines)
				if(i>0){
					for(int l = 0; l<w[i-1].length; l++)//{
						for(int m = 0; m<w[i-1][l].length; m++){
							Point p2 = xy[l*unitDistance +m*weightDistance][(i-1)*layerDistance];
							g.drawLine(p.x, (int)(p.y- w[i][j][k]*amplitude- zOffset), p2.x,(int)( p2.y - w[i-1][l][m]*amplitude- zOffset));
						}
				}
				if(k>0)
				if(drawSideLines){
					Point p2 = xy[j*unitDistance +(k-1)*weightDistance ][ i*layerDistance ];
					g.drawLine(p.x, (int) (p.y - w[i][j][k]*amplitude) - zOffset, p2.x, (int) (p2.y - w[i][j][k-1]*amplitude) - zOffset);
				}
				
				g.setColor(getColorForWeight(w[i][j][k]));
				g.fillOval(p.x-5, (int) (p.y - w[i][j][k]*amplitude) - zOffset,10, 10);
				g.drawLine(p.x, (int) (p.y - w[i][j][k]*amplitude) - zOffset ,p.x, p.y);
				g.setColor(Color.BLACK);
				}
			
		
		if(trailView){
			for(int f = 1; f<x[0].length;f++){
				//g.drawOval(, nodeSize, nodeSize);
				g.fillRect(trailViewOffset, f*trailViewSpace, nodeSize, nodeSize);
				for(int h = 0; h<w[0].length; h++)
			g.drawLine(trailViewOffset, f*trailViewSpace + nodeSize/2, trailViewOffset + (trailViewSpace*2 + nodeSize), h*trailViewSpace + nodeSize/2);
				if(trailViewX!=null &&  trailViewY!=null){
					g.setColor(Color.WHITE);
					g.drawString(Double.toString(trailViewX[f]), trailViewOffset, f*trailViewSpace + nodeSize/2);
					g.setColor(Color.BLACK);
				}
			}
			for(int i = 0; i<w.length; i++)
				for(int j = 0; j<w[i].length; j++){
					if(i==w.length-1){
						g.fillRect((i+1)*trailViewSpace*2 + trailViewOffset, trailViewSpace*j, nodeSize, nodeSize);
						if(trailViewX!=null &&  trailViewY!=null){
							g.setColor(Color.WHITE);//Double.toString(Math.round((10.0 * trailViewY[j]))/10
							g.drawString(Double.toString( (double)Math.round(trailViewY[j]*10)/10), (i+1)*trailViewSpace*2 + trailViewOffset, trailViewSpace*j +  nodeSize/2);
							g.setColor(Color.BLACK);
						}
					}
					else
					g.drawOval((i+1)*trailViewSpace*2 + trailViewOffset, trailViewSpace*j, nodeSize, nodeSize);
					if(i<w.length-1)
						for(int k = 0; k<w[i+1].length; k++)
						g.drawLine((i+1)*trailViewSpace*2 + trailViewOffset, j*trailViewSpace + nodeSize/2,
								trailViewOffset + (trailViewSpace*2 * (i+2) + nodeSize), k*trailViewSpace + nodeSize/2);
				}
		}
		g.setColor(Color.blue);
		g.drawString(cost,50, 50);
		//g.setColor(Color.red);
	//	g.drawString(grads,50, 30);
	}
	public Color getColorForWeight(double ww){
		double w = ww;
		if(Math.abs(w*10)>255)
			w = 25;
		Color color;
		if(w<0)
			color = new Color(Math.abs((int)w * 10), 0, 0);
		else if(w>0)
			color = new Color(0, Math.abs((int)w* 10), 0);
		else
			color = Color.BLACK;
		return color;
	}
	public void setTrailView(double[]x, double[] y){
		this.trailViewX = x;
		this.trailViewY = y;
	}
	public void updateCost(double cost){
		this.cost = Double.toString(cost);
	}
	public void updateAverage(double grads){
		this.grads = Double.toString(grads);
	}

}
