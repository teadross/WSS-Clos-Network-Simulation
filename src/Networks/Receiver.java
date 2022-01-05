package Networks;

import java.util.ArrayList;
import java.util.HashMap;

import general.Constant;

public class Receiver extends Node{
	private Network associatedNetwork = null;					// network that the node belongs to
	private HashMap<String, Wavelength> wavList = null;
	private ArrayList<Wavelength> wavList2 = null;
	private Node associateNode = null;
	String wav = "¦Ë-1";
	
	public Receiver(String name, int index, Node aNode,  Network associatedNetwork) {
		super(name, index, associatedNetwork);
		this.associatedNetwork = associatedNetwork;
		this.wavList = new HashMap<>();
		this.wavList2 = new ArrayList<>();
		this.associateNode = aNode;
		for(int i = 0; i < Constant.W; i++) {
			Wavelength wav = new Wavelength("¦Ë"+i,i);
			this.wavList.put("¦Ë"+i,wav);
			this.wavList2.add(wav);
		}
	}
	
	public boolean isNodeEmpty(String wavelength, double Cap) {
		if(this.wav == "¦Ë-1")
			return true;
		else if((this.wavList.get(wav).canUseCapacity(Cap))&&(this.wav.equals(wavelength)))
			return true;
		else
			return false;
	}
	
	public void useNode(String wav, double Cap) {
		this.wavList.get(wav).useCapacity(Cap);
		this.wav = wav;
	}
	
	public void releaseNode(String wav,double Cap) {
		this.wavList.get(wav).releaseCapacity(Cap);
		if(this.wavList.get(wav).getCapacity() == Constant.WaveCap)
			wav = "¦Ë-1";
	}

	public Network getAssociatedNetwork() {
		return associatedNetwork;
	}

	public HashMap<String, Wavelength> getWavList() {
		return wavList;
	}

	public ArrayList<Wavelength> getWavList2() {
		return wavList2;
	}

	public Node getAssociateNode() {
		return associateNode;
	}

	public String getWav() {
		return wav;
	}
	
}
