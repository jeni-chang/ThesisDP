package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PbCombin {
	List<Double> pb;
	public static Map<Integer, Set<Double>> pb_combin = new HashMap<>();
	
	public static List<Double> heu_pb = new ArrayList<>();
	public static Map<Integer, Set<Double>> heu_pb_combin = new HashMap<>();
	public static List<Double> heu_pb_1 = new ArrayList<>();
	
	public static List<Double> heu_pb_init = new ArrayList<>();
	public static Map<Integer, Set<Double>> heu_pb_combin_2 = new HashMap<>();
	public static List<Double> heu_pb_2 = new ArrayList<>();
	
	
	
	public PbCombin(List<Double> pb, List<Double> heu_pb, List<Double> heu_pb2) {
		this.pb = pb;
		PbCombin.heu_pb = heu_pb; // initial random probability
		PbCombin.heu_pb_init = heu_pb2; // initial average probability
	}
	
	public String comb(char[] from, char[] to, int len, int m, int n) {
		String result = "";
		if(n==0) {
			for(int i=0; i<len; i++) {
				result += to[i];
			}
			result += ":";
		}
		else {
			to[n-1] = from[m-1];
			
			if(m > n-1) {
				result = comb(from, to, len, m-1, n-1);
			}
			if(m > n) {
				result = comb(from, to ,len, m-1, n) + result;
			}
		}
		
		return result;
	}
	
	public void compute_pb(String s, int i, int version) {
		// token => [123, 124]
		String[] token = s.split(":");
		// t => 123
		Set<Double> pbset = new HashSet<>();
		for(String t : token) {
			// p => [1,2,3]
			String[] p = t.split("");
			double remain = 1.0;
			for(String pb : p) {
				if(version==0) remain = remain * (1-this.pb.get(Integer.parseInt(pb)));
				else if(version==1)  remain = remain * (1-PbCombin.heu_pb.get(Integer.parseInt(pb)));
				else if(version==2)  remain = remain * (1-PbCombin.heu_pb_init.get(Integer.parseInt(pb)));
			}
			pbset.add(remain);
//			System.out.println(t + " ==> " + remain);
		}
		if(version==0) PbCombin.pb_combin.put(i, pbset);
		else if(version==1) PbCombin.heu_pb_combin.put(i, pbset);
		else if(version==2) PbCombin.heu_pb_combin_2.put(i, pbset);
	}
	
	public void map_to_list(Map<Integer, Set<Double>> map, List<Double> ls) {
		for(int i: map.keySet()) {
			for(double p: map.get(i)) {
				int flag=0;
				for(double j: ls) {
					if(j==p) {
						flag=1;
						break;
					}
				}
				if(flag!=1) {
					ls.add(p);
				}
			}
		}
	}
	
}
