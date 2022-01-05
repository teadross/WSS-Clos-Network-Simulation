package main;

import Networks.Network;
import TrafficPattern.RandomTraffic;
import general.Constant;

public class MainFunc{
	public static void main(String[] args) {
		for(int i = 10; i <= 20; i+=2) {
			Constant.M_TWC = 1;
			Constant.M = 9;
			Constant.lambda = 2.0;
			Network Clos = new Network("DCN", 0);
			RandomTraffic RT = new RandomTraffic();
			Clos.generateTopology();
			RT.GenerateTraffic_SingleNode(Clos);
//			RT.GenerateTraffic(Clos);
			Clos.doEventList(RT.getEventList());
			RT.printConnections();
		}
	}
}
