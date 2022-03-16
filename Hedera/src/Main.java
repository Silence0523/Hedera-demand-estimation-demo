/**
 * Demonstrate Hedrea demand estimate algorithm with case provided in paper
 * @author Xueshen Liu
 *
 */
public class Main {

	public static void main(String[] args) {
		// flow in given values
		Flow[] f = {new Flow(0,1,1),new Flow(0,2,1),new Flow(0,3,1),
					new Flow(1,0,1),new Flow(1,0,1),new Flow(1,2,1),
					new Flow(2,0,1),new Flow(2,3,1),
					new Flow(3,1,1),new Flow(3,1,1)};
		
		Host h0 = new Host(0);
		Host h1 = new Host(1);
		Host h2 = new Host(2);
		Host h3 = new Host(3);
		
		Host[] hosts = {h0,h1,h2,h3};
		
		Scheduler sched = new Scheduler(4,f);
		sched.printList(sched.estimate_demand(hosts),4);
	}

}
