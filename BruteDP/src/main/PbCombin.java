package main;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PbCombin {
	List<Double> pb;
	public static Map<Integer, Set<Double>> pb_combin = new HashMap<>();
	
	public PbCombin(List<Double> pb) {
		this.pb = pb;
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
	
	public void compute_pb(String s, int i) {
		// token => [123, 124]
		String[] token = s.split(":");
		// t => 123
		Set<Double> pbset = new HashSet<>();
		for(String t : token) {
			// p => [1,2,3]
			String[] p = t.split("");
			double remain = 1.0;
			for(String pb : p) {
				remain = remain * (1-this.pb.get(Integer.parseInt(pb)));
			}
			pbset.add(remain);
//			System.out.println(t + " ==> " + remain);
		}
		PbCombin.pb_combin.put(i, pbset);
	}
	
}
