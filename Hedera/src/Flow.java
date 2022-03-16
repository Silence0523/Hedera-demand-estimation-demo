
public class Flow {
	public int src;
	public int dst;
	public double demand;
	public boolean converged=false;
	public boolean rl;
	
	public int dupNum;  // # of repetitive(same src+dst) flows, only for print
	
	public Flow(int src,int dst,double demand) {
		this.src=src;
		this.dst=dst;
		this.demand=demand;
	}
	
	/**
	 * designed for formatted print
	 * @param f - flow to be check (if duplicated)
	 * @return true if duplicated, false otherwise
	 */
	public boolean equals(Flow f) {
		return f.src==src && f.dst==dst;
	}
}
