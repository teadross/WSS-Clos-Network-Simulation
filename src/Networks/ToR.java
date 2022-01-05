package Networks;

import java.util.ArrayList;

import general.CommonObject;

public class ToR extends CommonObject {
	
	private Network associatedNetwork = null;					// network that the node belongs to
	
	private ArrayList<Transmitter> inputList = null;					// network that the node belongs to
	private ArrayList<Receiver> outputList = null;					// network that the node belongs to
	
	public ToR(String name, int index, ArrayList<Transmitter> inputList, ArrayList<Receiver> outputList, Network associatedNetwork) {
		super(name, index);
		this.inputList = inputList;
		this.outputList = outputList;
		this.associatedNetwork = associatedNetwork;
	}

	public Network getAssociatedNetwork() {
		return associatedNetwork;
	}

	public ArrayList<Transmitter> getInputList() {
		return inputList;
	}

	public ArrayList<Receiver> getOutputList() {
		return outputList;
	}
	
}
