import java.util.ArrayList;

public class Host {
	public int src;
	
	public Host(int src) {
		this.src=src;
	}
	
	public boolean isSrc(Flow f) {
		return f.src==this.src;
	}
	
	public boolean isDst(Flow f) {
		return f.dst==this.src;
	}
	
	public Flow[] getSrc(Flow[] flowList) {
		ArrayList <Flow> srcList = new ArrayList<Flow>();
		for(Flow f:flowList)
			if(isSrc(f))
				srcList.add(f);
		
		return srcList.toArray(new Flow[srcList.size()]);
	}
	
	public Flow[] getDst(Flow[] flowList) {
		ArrayList <Flow> dstList = new ArrayList<Flow>();
		for(Flow f:flowList)
			if(isDst(f))
				dstList.add(f);
		
		return dstList.toArray(new Flow[dstList.size()]);
	}
}
