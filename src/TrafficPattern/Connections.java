package TrafficPattern;

import java.util.ArrayList;

import Networks.*;
import general.Constant;

public class Connections {

	private int state = Constant.Undo;

	private int Middle = Constant.NOT;
	
	private double startTime = -1;
	private double keepingTime = -1;
	private double endTime = -1;
	
	private ToR startToR = null;
	private ToR endToR = null;
	
	private String wav1 = null;
	private String wav2 = null;
	
	private double cap = -1.0;
	
	private ArrayList<Node> AnswerNodeList = null;
	private ArrayList<Node> AnswerSwitchList = null;
	private ArrayList<Link> AnswerLinkList = null;
	
	public Connections(ToR startToR, ToR endToR, double startTime, double keepingTime, Network net) {
		this.AnswerNodeList = new ArrayList<>();
		this.AnswerSwitchList = new ArrayList<>();
		this.AnswerLinkList = new ArrayList<>();
		this.startToR = startToR;
		this.endToR = endToR;
		this.cap = Constant.TrafficCap;
		this.startTime = startTime;
		this.keepingTime = keepingTime;
		this.endTime = startTime + keepingTime;
	}
	
	public void PrintConnections() {
		System.out.print("state = ");
		if(this.state == Constant.Blocked)
			System.out.print("Blocked");
		else if(this.state == Constant.Doing)
			System.out.print("Doing");
		else if(this.state == Constant.Done)
			System.out.print("Done");
		else if(this.state == Constant.Undo)
			System.out.print("Undo:Error");
		System.out.println();

		System.out.println("Start ToR = " + this.startToR.getName());
		System.out.println("End ToR = " + this.endToR.getName());
		System.out.println("Start Time = " + this.startTime);
		System.out.println("End Time = " + this.endTime);
		System.out.println("Wavelength = " + this.wav1+", "+this.wav2);
		System.out.print("Route = ");
		for(Node o : this.AnswerSwitchList)
			System.out.print(o.getName()+" ");
		System.out.println(";");
//		for(Link o : this.AnswerLinkList)
//			System.out.print(o.getName()+" ");
//		System.out.println(";");
		
		System.out.println();
	}
	
	public int getMiddle() {
		return Middle;
	}

	public void setMiddle(int middle) {
		Middle = middle;
	}

	public double getCap() {
		return cap;
	}

	public void setCap(double cap) {
		this.cap = cap;
	}

	public ToR getStartToR() {
		return startToR;
	}

	public ToR getEndToR() {
		return endToR;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public double getStartTime() {
		return startTime;
	}

	public double getKeepingTime() {
		return keepingTime;
	}

	public double getEndTime() {
		return endTime;
	}
	
	public String getWav1() {
		return wav1;
	}

	public void setWav1(String wav) {
		this.wav1 = wav;
	}
	
	public String getWav2() {
		return wav2;
	}

	public void setWav2(String wav) {
		this.wav2 = wav;
	}

	public ArrayList<Node> getAnswerNodeList() {
		return AnswerNodeList;
	}

	public ArrayList<Node> getAnswerSwitchList() {
		return AnswerSwitchList;
	}

	public ArrayList<Link> getAnswerLinkList() {
		return AnswerLinkList;
	}
}
