/**
 * Core Hedrea demand estimate algorithm
 * @author Xueshen Liu
 *
 */
public class Scheduler {
	public int hostNum;
	public Flow[][] matrix;
	public Flow[] flowList;
	
	public Scheduler(int hostNum, Flow[] flowList) {
		this.hostNum=hostNum;
		this.flowList=flowList;
	}
	
	public Flow[][] estimate_demand(Host[] hosts) {
		matrix=new Flow[10][10];
		for(int i=0;i<matrix.length;i++) {
			for(int j=0;j<matrix[i].length;j++) {
				matrix[i][j] = new Flow(i,j,0);
			}
		}
		
		while(true) {
			// deepcopy matrix for following comparison
			boolean flag=false;
			Flow[][] copy= new Flow[10][10];
			for(int i=0;i<copy.length;i++) {
				for(int j=0;j<copy[i].length;j++) {
					copy[i][j] = new Flow(i,j,matrix[i][j].demand);
				}
			}
			
			for(Host host:hosts) {
				estimate_src(host);
				estimate_dst(host);
			}

			// comparison
			for(int i=0;i<matrix.length;i++) {
				for(int j=0;j<matrix[i].length;j++) {
					if(copy[i][j].demand != matrix[i][j].demand)
						flag=true;
				}
			}
			if(!flag)
				break;
		}
		
		return matrix;
	}
	
	/**
	 * format output
	 * @param matrix - matrix correspond to matrix in paper
	 * @param num - actual matrix size (num*num), max 10(only for test)
	 */
	public void printList(Flow[][] matrix, int num) {
		for(int i=0;i<num;i++) {
			for(int j=0;j<num;j++) {
				if(i == j)
					System.out.printf("\t\t");
				else if(matrix[i][j].demand == 0)
					System.out.printf("00\t\t");
				else {
					matrix[i][j].dupNum=0;
					for(Flow f:flowList)
						if(matrix[i][j].equals(f))
							matrix[i][j].dupNum++;
					if(matrix[i][j].converged)
						System.out.printf("[%.2f]%d\t\t",matrix[i][j].demand,matrix[i][j].dupNum);
					else
						System.out.printf("(%.2f)%d\t\t",matrix[i][j].demand,matrix[i][j].dupNum);
				}
					
			}
			System.out.println();
		}
		System.out.println();
	}
	
	/**
	 * For src, collect resource from unconverged flow and evenly distributed to them
	 * @param host - src host to be estimated
	 */
	public void	estimate_src(Host host) {
		double dF=0;
		int nU=0;
		if(host.getSrc(flowList).length==0)
			return;
		for(Flow f:host.getSrc(flowList)) {
			if(f.converged)
				dF+=f.demand;
			else
				nU++;
		}
		double eS=(1.0-dF)/nU;
		for(Flow f:host.getSrc(flowList)) {
			if(!(f.converged)) {
				matrix[f.src][f.dst].demand=eS;
				f.demand=eS;
			}
		}
	}
	
	/**
	 * For dst, limit throughput (to 1), then:
	 * - Evenly distributed to remaining flow
	 * - For satisfied flow, recycle excessive resource back
	 * - For others, evenly distributed remaining sources (and repetitively test for convergence)
	 * Theoretically, every next step allocates more resource than previous one.
	 * @param host - dst host to be estimated
	 */
	public void estimate_dst(Host host) {
		double dT = 0;
		double dS = 0;
		int nR = 0;
		for(Flow f:host.getDst(flowList)) {
			f.rl=true;
			dT+=f.demand;
			nR++;
		}
		if(dT<=1.0)
			return;
		double eS=1.0/nR;
		
		boolean flag;
		do {
			flag=false;
			nR=0;
			for(Flow f:host.getDst(flowList)) {
				if(f.rl) {
					if(f.demand<eS) {
						dS+=f.demand;
						f.rl=false;
						flag = true;
					}
					else
						nR++;
				}
			}
			eS=(1.0-dS)/nR;
		} while(flag);
			
		for(Flow f:host.getDst(flowList)) {
			if(f.rl) {
				matrix[f.src][f.dst].demand=eS;
				matrix[f.src][f.dst].converged=true;
				f.demand=eS;
				f.converged=true;
			}
		}
	}
}
