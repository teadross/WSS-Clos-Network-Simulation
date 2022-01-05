package TrafficPattern;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

import Networks.*;
import general.Constant;

public class RandomTraffic {
	private ArrayList<Connections> conList = null;
	private ArrayList<Event> eventList = null;
	
	public RandomTraffic() {
		this.conList = new ArrayList<>();
		this.eventList = new ArrayList<>();
	}
	
	public void GenerateTraffic_test(Network net) {
		int i = 0;
		for(ToR torA : net.getToRList2()) {
			Connections con = new Connections(torA, torA, 1.0+0.001*i, 10.0+0.001*i, net);
			conList.add(con);
			//事件：开始、结束
			Event event1 = new Event(Constant.Arrival,con,con.getStartTime());
			Event event2 = new Event(Constant.Leave,con,con.getEndTime());
			this.eventList.add(event1);
			this.eventList.add(event2);
			i++;
		}
		//事件排序
		Collections.sort(eventList,new Comparator<Event>(){
			@Override
			public int compare(Event o1, Event o2) {
				return new Double(o1.getTime()).compareTo(new Double(o2.getTime()));
			}
		});
	}

	public void GenerateTraffic(Network net) {
		System.out.println("Init Connections...");
		for(ToR torA : net.getToRList2()) {
			for(ToR torB : net.getToRList2()) {
					double[] timeList = this.arrivalList();
					for(int i = 0; i < Constant.TrafficNum; i++) {
						Connections con = new Connections(torA, torB, timeList[i], this.keepingTime(), net);
						conList.add(con);
						//事件：开始、结束
						Event event1 = new Event(Constant.Arrival,con,con.getStartTime());
						Event event2 = new Event(Constant.Leave,con,con.getEndTime());
						this.eventList.add(event1);
						this.eventList.add(event2);
					}
//				}
			}
		}
		//事件排序
		Collections.sort(eventList,new Comparator<Event>(){
			@Override
			public int compare(Event o1, Event o2) {
				return new Double(o1.getTime()).compareTo(new Double(o2.getTime()));
			}
		});
		System.out.println("Init Done.");
	}

	public void GenerateTraffic_SingleNode(Network net) {
		System.out.println("Init Connections...");
		int size = net.getToRList2().size();
		int[] list = new int [size];
		for(ToR torA : net.getToRList2()) {
			double[] timeList = this.arrivalList();
			for(int i = 0; i < Constant.TrafficNum; i++) {
				int j = (new Random()).nextInt(size);
				Connections con = new Connections(torA, net.getToRList2().get(j), timeList[i], this.keepingTime(), net);
				list[j] = list[j]+1;
				conList.add(con);
				//事件：开始、结束
				Event event1 = new Event(Constant.Arrival,con,con.getStartTime());
				Event event2 = new Event(Constant.Leave,con,con.getEndTime());
				this.eventList.add(event1);
				this.eventList.add(event2);
			}
		}
		//事件排序
		Collections.sort(eventList,new Comparator<Event>(){
			@Override
			public int compare(Event o1, Event o2) {
				return new Double(o1.getTime()).compareTo(new Double(o2.getTime()));
			}
		});
		System.out.println("Init Done.");
	}
	
	public ArrayList<Event> getEventList() {
		return eventList;
	}
	
	public ArrayList<Connections> getConList() {
		return conList;
	}

	//到达时间的产生
	public double[] arrivalList() {
		double[] timeList = new double[Constant.TrafficNum];

		timeList[0] = this.nextArrival();
		for(int i = 1; i < Constant.TrafficNum; i++) {
			timeList[i] = timeList[i-1] + this.nextArrival();
		}
		return timeList;
	}
	
	//结束时间的产生
	public double nextArrival() {      
	    double P = (new Random()).nextDouble();
	    double t = (-1.0/Constant.lambda)*(Math.log(P));
	    return t;
	}
	
	public double keepingTime() {      
	    double P = (new Random()).nextDouble();
	    double t = (-1.0/Constant.mu)*(Math.log(P));
	    return t;
	}
	
	public void printEvent() {
		int bl = 0;
		int to = 0;
		int i = 0;
		for(Event e : this.eventList) {
			i++;
			System.out.println(i);
			Connections con = e.getCon();
			con.PrintConnections();
			if(con.getState() == Constant.Blocked)
				bl++;
			to++;
		}
		double ratio = ((double)bl/(double)to);
		System.out.print("Blocking Ratio : "+ratio+".");
	}
	
	public void printConnections() {
		int total = 0;
		int blocking_total = 0;
		int passing_link = 0;
		int blocking_link = 0;
		for(Connections con : this.conList) {
//			con.PrintConnections();
			if(con.getState() == Constant.Blocked)
				blocking_total++;
			if(con.getMiddle() == Constant.MPass)
				passing_link++;
			if(con.getMiddle() == Constant.MBlock)
				blocking_link++;
			total++;
		}
		double total_ratio = ((double)blocking_total/(double)total);
		double link_ratio = ((double)blocking_link/(double)(passing_link+blocking_link));
		System.out.println("Total Blocking : "+blocking_total+".");
		System.out.println("Overall Blocking Ratio : "+total_ratio+".");
		System.out.println("Passing Connection on Link: "+passing_link+", Blocking Connection on Link: "+blocking_link+".");
		System.out.println("Link Blocking Ratio : "+link_ratio+".");
		System.out.println("Port Blocking Ratio : "+(double)(total - (passing_link+blocking_link))/(double)total+".");
	}
}
