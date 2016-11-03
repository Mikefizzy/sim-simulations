import java.awt.Graphics;
import java.util.ArrayList;
import boids.Boid;
import sim.engine.Engine;
import sim.entity.Entity;
public class EngineB extends Engine{
	private ArrayList<Entity> entities = new ArrayList<Entity>();
	public EngineB(int offset) {
		super(offset);
		this.setFps(60);
	}
	public void onStart() {
		double start = 6;
		for(double k = 0; k<52; k++)
		entities.add(new Boid(120, 120, 60,5,start-k/20,this.getGrid()));
	}
	public void tick() {

		for(Entity e: entities){
			e.tick(entities);
			e.incrementGrace();
		}
	}

	public void onRender(Graphics g) {
		this.renderGrid(g, 2);
		for(int k = 0; k<entities.size(); k++)
			try{
				Entity e = entities.get(k);
				if(e.z>500||e.z<0)
					entities.remove(k);
				else
					e.render(g, this.getGrid());
			}catch(ArrayIndexOutOfBoundsException e){
				Boid b  = (Boid) (entities.get(k));
				b.reset();
				continue;
			}

	}
	
	public static void main(String[] args) {
		new Thread(new EngineB(2)).start();
	}
}
