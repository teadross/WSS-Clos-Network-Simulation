package Networks;

import java.util.ArrayList;
import java.util.HashMap;

import general.CommonObject;
import general.Constant;

public class Link extends CommonObject{
	
	private Network associatedNetwork = null;					// network that the link belongs to
	private Node nodeA = null;									// node A
	private Node nodeB = null;									// node B
	private HashMap<String, Wavelength> wavList = null;
	private ArrayList<Wavelength> wavList2 = null;
	
	public Link(String name, int index, Node nodeA, Node nodeB, Network associatedNetwork) {
		super(name, index);
		this.nodeA = nodeA;
		this.nodeB = nodeB;
		this.associatedNetwork = associatedNetwork;
		this.wavList = new HashMap<>();
		this.wavList2 = new ArrayList<>();
		for(int i = 0; i < Constant.W; i++) {
			Wavelength wav = new Wavelength("¦Ë"+i,i);
			this.wavList.put("¦Ë"+i,wav);
			this.wavList2.add(wav);
		}
	}
	
	public boolean isLinkEmpty(String wav, double Cap) {
		if(this.wavList.get(wav).canUseCapacity(Cap))
			return true;
		else
			return false;
	}
	
	public void useLink(String wav, double Cap) {
		this.wavList.get(wav).useCapacity(Cap);
	}
	
	public void releaseLink(String wav, double Cap) {
		this.wavList.get(wav).releaseCapacity(Cap);
	}

	public HashMap<String, Wavelength> getWavList() {
		return wavList;
	}

	public void setWavList(HashMap<String, Wavelength> wavList) {
		this.wavList = wavList;
	}

	public Network getAssociatedNetwork() {
		return associatedNetwork;
	}

	public void setAssociatedNetwork(Network associatedNetwork) {
		this.associatedNetwork = associatedNetwork;
	}

	public Node getNodeA() {
		return nodeA;
	}

	public void setNodeA(Node nodeA) {
		this.nodeA = nodeA;
	}

	public Node getNodeB() {
		return nodeB;
	}

	public void setNodeB(Node nodeB) {
		this.nodeB = nodeB;
	}
}