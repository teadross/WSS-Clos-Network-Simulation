package TrafficPattern;

import general.Constant;

public class Event {
	private int type = Constant.Nothing;
	private double time = 0;
	private Connections con = null;
	
	public Event(int type, Connections con, double time) {
		this.type = type;
		this.con = con;
		this.time = time;
	}

	public int getType() {
		return type;
	}

	public double getTime() {
		return time;
	}

	public Connections getCon() {
		return con;
	}
}
