package Networks;

import general.CommonObject;
import general.Constant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

import TrafficPattern.Connections;
import TrafficPattern.Event;

public class Network extends CommonObject {
	private HashMap<String, Node> nodeList = null; 			// list of nodes within the network
	private ArrayList<Node> nodeList2 = null;
	private HashMap<String, ToR> ToRList = null; 			// list of nodes within the network
	private ArrayList<ToR> ToRList2 = null;
	private HashMap<String, Link> linkList =null;			// list of links within the network
	private ArrayList<Link> linkList2 = null;
	private ArrayList<String> wavList = null;
	
	public Network(String name, int index) {
		super(name, index);
		
		this.nodeList = new HashMap<String,Node>();
		this.nodeList2 = new ArrayList<Node>();
		
		this.ToRList = new HashMap<String,ToR>();
		this.ToRList2 = new ArrayList<ToR>();
		
		this.linkList = new HashMap<String,Link>();
		this.linkList2 = new ArrayList<Link>();
		
		this.wavList = new ArrayList<>();

		for(int i = 0; i < Constant.W; i++) {
			String wav = "¦Ë"+i;
			this.wavList.add(wav);
		}
	}

	public void addToR(ToR tor){
		this.ToRList.put(tor.getName(), tor);
		this.ToRList2.add(tor);
	}
	

	public void addNode(Node node){
		this.nodeList.put(node.getName(), node);
		this.nodeList2.add(node);
	}
	
	public void addLink(Link link){
		this.linkList.put(link.getName(), link);
		this.linkList2.add(link);
	}
	
	public void generateLink(String nodeAName, String nodeBName) {
		Node nodeA = this.getNodeList().get(nodeAName);
		Node nodeB = this.getNodeList().get(nodeBName);
		
		String name = nodeA.getName()+"-"+nodeB.getName();
		int index = this.getLinkList2().size();
		
		Link link = new Link(name, index, nodeA, nodeB, this);
		this.addLink(link);
	}
	
	
	public Link findLink(Node nodeA, Node nodeB){
		return this.getLinkList().get(nodeA.getName()+"-"+nodeB.getName());
	}
	
	public void generateTopology() {
		for(int i = 0; i < Constant.M; i++) {
			Node nod = new Node("Middle_"+i, this.nodeList2.size(), this);
			this.addNode(nod);
		}

		for(int i = 0; i < Constant.M_TWC; i++) {
			Node nod = new Node("Middle_TWC_"+i, this.nodeList2.size(), this);
			this.addNode(nod);
		}
		
		for(int i = 0; i < Constant.R; i++) {
			Node nod = new Node("Ingress_"+i, this.nodeList2.size(), this);
			this.addNode(nod);
		}
		
		for(int i = 0; i < Constant.R; i++) {
			Node nod = new Node("Egress_"+i, this.nodeList2.size(), this);
			this.addNode(nod);
		}
		
		for(int i = 0; i < Constant.R; i++) {
			for(int k = 0; k < Constant.N; k++) {
				ArrayList<Transmitter> IngressList = new ArrayList<>();
				ArrayList<Receiver> EgressList = new ArrayList<>();
				for(int j = 0; j < Constant.x; j++) {
					Transmitter nodi = new Transmitter("Ingress_"+i+"_Port_"+k+"_Tx_"+j, this.nodeList2.size(), this.nodeList.get("Ingress_"+i),this);
					Receiver nodo = new Receiver("Egress_"+i+"_Port_"+k+"_Rx_"+j, this.nodeList2.size(), this.nodeList.get("Egress_"+i),this);
					IngressList.add(nodi);
					EgressList.add(nodo);
					this.addNode(nodi);
					this.addNode(nodo);

					this.generateLink(nodi.getName(), this.nodeList.get("Ingress_"+i).getName());
					this.generateLink(this.nodeList.get("Egress_"+i).getName(), nodo.getName());
				}
				ToR tor = new ToR("In/Out_"+i+"_Port_"+k, this.ToRList2.size(), IngressList, EgressList, this);
				this.addToR(tor);
			}
		}

		for(int i = 0; i < Constant.M; i++) {
			Node nodm = this.nodeList.get("Middle_"+i);
			for(int j = 0; j < Constant.R; j++) {
				Node nodi = this.nodeList.get("Ingress_"+j);
				Node nodo = this.nodeList.get("Egress_"+j);
				this.generateLink(nodi.getName(), nodm.getName());
				this.generateLink(nodm.getName(), nodo.getName());
			}
		}

		for(int i = 0; i < Constant.M_TWC; i++) {
			Node nodm = this.nodeList.get("Middle_TWC_"+i);
			for(int j = 0; j < Constant.R; j++) {
				Node nodi = this.nodeList.get("Ingress_"+j);
				Node nodo = this.nodeList.get("Egress_"+j);
				this.generateLink(nodi.getName(), nodm.getName());
				this.generateLink(nodm.getName(), nodo.getName());
			}
		}
	}
	
	public boolean canGenerateLink(ToR torA, ToR torB, double cap) {
		for(int i = 0; i < Constant.W; i++) {
			String wav = "¦Ë"+i;
			for(Transmitter tx : torA.getInputList()) {
				if(tx.isNodeEmpty(wav, cap)) {
					Node input = tx.getAssociateNode();
					Link link1 = this.findLink(tx, input);
					if(link1.isLinkEmpty(wav, cap)) {
						for(Receiver rx : torB.getOutputList()) {
							if(rx.isNodeEmpty(wav, cap)) {
								Node output = rx.getAssociateNode();
								Link link4 = this.findLink(output, rx);
								if(link4.isLinkEmpty(wav, cap)) {
									ArrayList<Node> availList = new ArrayList<>();
									for(int j = 0; j < Constant.M; j++) {
										Node nodm = this.nodeList.get("Middle_"+j);
										Link link2 = this.findLink(input, nodm);
										Link link3 = this.findLink(nodm, output);
										if((link2.isLinkEmpty(wav, cap))&&(link3.isLinkEmpty(wav, cap))) {
											availList.add(nodm);
											return true;
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return false;
	}
	
	public boolean canGenerateLink(Connections con) {
		ToR torA = con.getStartToR();
		ToR torB = con.getEndToR();
		double cap = con.getCap();
		Collections.shuffle(wavList);
		for(String wav : this.wavList) {
			//Transmitter is empty?
			for(Transmitter tx : torA.getInputList()) {
				if(tx.isNodeEmpty(wav, cap)) {
//				if(true) {
					Node input = tx.getAssociateNode();
					Link link1 = this.findLink(tx, input);
					if(link1.isLinkEmpty(wav, cap)) {
//					if(true) {
						//receiver is empty
						for(Receiver rx : torB.getOutputList()) {
							if(rx.isNodeEmpty(wav, cap)) {
//							if(true) {
								Node output = rx.getAssociateNode();
								Link link4 = this.findLink(output, rx);
								if(link4.isLinkEmpty(wav, cap)) {
//								if(true) {
									//is link empty
									ArrayList<Node> availList = new ArrayList<>();
									for(int j = 0; j < Constant.M; j++) {
										Node nodm = this.nodeList.get("Middle_"+j);
//									if (Constant.M > 0){
//										Node nodm = this.nodeList.get("Middle_"+(new Random()).nextInt(Constant.M));
										Link link2 = this.findLink(input, nodm);
										Link link3 = this.findLink(nodm, output);
										if((link2.isLinkEmpty(wav, cap))&&(link3.isLinkEmpty(wav, cap))) {
											availList.add(nodm);
										}
									}
									if(availList.size()>=1) {
										con.setMiddle(Constant.MPass);
//										Node nodm = availList.get((new Random()).nextInt(availList.size()));
										Node nodm = availList.get(0);
										Link link2 = this.findLink(input, nodm);
										Link link3 = this.findLink(nodm, output);
										con.getAnswerNodeList().add(tx);
										con.getAnswerNodeList().add(rx);
										con.getAnswerSwitchList().add(input);
										con.getAnswerSwitchList().add(nodm);
										con.getAnswerSwitchList().add(output);
										con.setWav1(wav);
										con.setWav2(wav);
										con.getAnswerLinkList().add(link1);
										con.getAnswerLinkList().add(link2);
										con.getAnswerLinkList().add(link3);
										con.getAnswerLinkList().add(link4);
										return true;
									}
//									else
//										con.setMiddle(Constant.MBlock);
								}
							}
						}
					}
				}
			}
		}
		Collections.shuffle(wavList);
		for(String wav1 : this.wavList) {
			//Transmitter is empty?
			for(Transmitter tx : torA.getInputList()) {
				if(tx.isNodeEmpty(wav1, cap)) {
//				if(true) {
					Node input = tx.getAssociateNode();
					Link link1 = this.findLink(tx, input);
					if(link1.isLinkEmpty(wav1, cap)) {
//					if(true) {
						//receiver is empty
						Collections.shuffle(wavList);
						for(String wav2 : this.wavList) {
							//Transmitter is empty?
							for(Receiver rx : torB.getOutputList()) {
								if(rx.isNodeEmpty(wav2, cap)) {
//								if(true) {
									Node output = rx.getAssociateNode();
									Link link4 = this.findLink(output, rx);
									if(link4.isLinkEmpty(wav2, cap)) {
//									if(true) {
										//is link empty
										ArrayList<Node> availList = new ArrayList<>();
										for(int j = 0; j < Constant.M_TWC; j++) {
											Node nodm = this.nodeList.get("Middle_TWC_"+j);
//										if (Constant.M_TWC > 0){
//											Node nodm = this.nodeList.get("Middle_TWC_"+(new Random()).nextInt(Constant.M_TWC));
											Link link2 = this.findLink(input, nodm);
											Link link3 = this.findLink(nodm, output);
											if((link2.isLinkEmpty(wav1, cap))&&(link3.isLinkEmpty(wav2, cap))) {
												availList.add(nodm);
//												System.out.println("TWC Used");
											}
										}
										if(availList.size() >= 1) {
											con.setMiddle(Constant.MPass);
											Node nodm = availList.get((new Random()).nextInt(availList.size()));
//											Node nodm = availList.get(0);
											Link link2 = this.findLink(input, nodm);
											Link link3 = this.findLink(nodm, output);
											con.getAnswerNodeList().add(tx);
											con.getAnswerNodeList().add(rx);
											con.getAnswerSwitchList().add(input);
											con.getAnswerSwitchList().add(nodm);
											con.getAnswerSwitchList().add(output);
											con.setWav1(wav1);
											con.setWav2(wav2);
											con.getAnswerLinkList().add(link1);
											con.getAnswerLinkList().add(link2);
											con.getAnswerLinkList().add(link3);
											con.getAnswerLinkList().add(link4);
											return true;
										}
										else
											con.setMiddle(Constant.MBlock);
									}
								}
							}
						}
					}
				}
			}
		}
		return false;
	}
	
	public void useConnections(Connections con) {
		String wav1 = con.getWav1();
		String wav2 = con.getWav2();
		double cap = con.getCap();
		((Transmitter)(con.getAnswerNodeList().get(0))).useNode(wav1, cap);
		((Receiver)(con.getAnswerNodeList().get(1))).useNode(wav2, cap);
		con.getAnswerLinkList().get(0).useLink(wav1, cap);
		con.getAnswerLinkList().get(1).useLink(wav1, cap);
		con.getAnswerLinkList().get(2).useLink(wav2, cap);
		con.getAnswerLinkList().get(3).useLink(wav2, cap);
	}
	
	public void releaseConnections(Connections con) {
		String wav1 = con.getWav1();
		String wav2 = con.getWav2();
		double cap = con.getCap();
		((Transmitter)(con.getAnswerNodeList().get(0))).releaseNode(wav1, cap);
		((Receiver)(con.getAnswerNodeList().get(1))).releaseNode(wav2, cap);
		con.getAnswerLinkList().get(0).releaseLink(wav1, cap);
		con.getAnswerLinkList().get(1).releaseLink(wav1, cap);
		con.getAnswerLinkList().get(2).releaseLink(wav2, cap);
		con.getAnswerLinkList().get(3).releaseLink(wav2, cap);
	}
	
	public void doEventList(ArrayList<Event> eveList) {
		for(Event eve : eveList)
			this.doEvent(eve);
	}
	
	public void doEvent(Event eve) {
		Connections con = eve.getCon();
		
		if(eve.getType() == Constant.Arrival) {
			if(this.canGenerateLink(con)) {
				this.useConnections(con);
				con.setState(Constant.Doing);
			}
			else
				con.setState(Constant.Blocked);
		}
		else if(eve.getType() == Constant.Leave) {
			if(con.getState() == Constant.Doing) {
				this.releaseConnections(con);
				con.setState(Constant.Done);
			}
		}
	}
	
	public HashMap<String, ToR> getToRList() {
		return ToRList;
	}

	public ArrayList<ToR> getToRList2() {
		return ToRList2;
	}

	public HashMap<String, Node> getNodeList() {
		return nodeList;
	}

	public ArrayList<Node> getNodeList2() {
		return nodeList2;
	}
	public HashMap<String, Link> getLinkList() {
		return linkList;
	}

	public ArrayList<Link> getLinkList2() {
		return linkList2;
	}

	public void printNetwork() {
		for(ToR t: this.ToRList2) {
			System.out.println(t.getName());
			for(Transmitter tx: t.getInputList())
				System.out.println(tx.getName());
			for(Receiver rx: t.getOutputList())
				System.out.println(rx.getName());
		}
		for(Link t: this.linkList2)
			System.out.println(t.getName());
	}
}