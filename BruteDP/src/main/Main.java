package main;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub\
		List<Table> table = new ArrayList<>();
		
		int layer = 5;
		int server = 3;
		List<Double> pb = new ArrayList<>();
		List<Double> lc = new ArrayList<>();
		List<Double> cc = new ArrayList<>();
		List<Double> r = new ArrayList<>();
		List<Double> bw = new ArrayList<>();
		List<Double> com = new ArrayList<>();
		List<Double> ls = new ArrayList<>();
		List<Double> cs = new ArrayList<>();
		List<Double> sp = new ArrayList<>();
		double f = 100; 
		
		pb.add(0.0);
		pb.add(0.3);
		pb.add(0.5);
		pb.add(0.8);
		pb.add(0.1);
		pb.add(1.0);
		
		lc.add(0.0);
		lc.add(300.0);
		lc.add(240.0);
		lc.add(360.0);
		lc.add(120.0);
		lc.add(180.0);
		
		cc.add(0.0);
		cc.add(400.0);
		cc.add(350.0);
		cc.add(280.0);
		cc.add(160.0);
		cc.add(0.0);
		
		r.add(1.0);
		r.add(4.0);
		r.add(0.5);
		r.add(3.0);
		r.add(1.0);
		r.add(0.0);
		
		bw.add(0.0);
		bw.add(200.0);
		bw.add(200.0);
		bw.add(Double.MAX_VALUE);
		
		com.add(0.0);
		com.add(10.0);
		com.add(12.0);
		com.add(8.0);
		
		ls.add(0.0);
		ls.add(30.0);
		ls.add(24.0);
		ls.add(36.0);
		ls.add(12.0);
		ls.add(18.0);
		
		cs.add(0.0);
		cs.add(40.0);
		cs.add(35.0);
		cs.add(28.0);
		cs.add(16.0);
		cs.add(0.0);
		
		sp.add(0.0);
		sp.add(90.0);
		sp.add(100.0);
		sp.add(110.0);				
		
		
		// create table
		for (int c=0; c<=server; c++) {
			for (int s=1; s<=server; s++) {
				for(int l=0; l<=layer; l++) {
					Table t = new Table();
					t.setL(l);
					t.setS(s);
					t.setC(c);
					table.add(t);
				}
			}
		}
		
		// compute probability combination
		PbCombin cbin = new PbCombin(pb);
		String s = null;
		char[] from = new char[layer];
		for(int i=0; i<layer; i++) {
			char c = (char)(i+1+'0');
			from[i] = c;
		}
	    char to[] = new char[from.length];
	    
	    Set<Double> init = new HashSet<>();
	    init.add(1.0);
	    PbCombin.pb_combin.put(0, init);
	    for (int i = 1; i <= from.length; i++) {
	      s = cbin.comb(from, to, i, from.length, i);
	      cbin.compute_pb(s, i);
	    }
	    
	    // compute heuristic probability
	    cbin.create_heuPb(server);
//	    System.out.println(PbCombin.heu_pb);
	    
	    
//	    for(int i : PbCombin.pb_combin.keySet()) {
//	    	System.out.println(i + " ==> " + PbCombin.pb_combin.get(i));
//	    }
		
		// check 
		new DP(table, layer, server);
		for(int i=1; i<= server; i++) DP.recursive(layer, server, i);
//		table = r.get_table();
		
//		BottomUp btmup = new BottomUp(table, pb, lc, cc , r, bw , com, f, layer, server, 3, ls , cs, sp);
//		btmup.compute();
		
		NewBottomUp btmup = new NewBottomUp(table, pb, lc, cc , r, bw , com, f, layer, server, 3, ls , cs, sp);
		btmup.init_pb(server);
		btmup.compute();
		
		for(Table t: table) {
//			if(t.getID() == 23) {
			if(t.getL() == 5 && t.getS() == 3 && t.getC() == 2) {
				System.out.print(t.toString() + "===>");
				System.out.println(t.getPb());
				
				System.out.print(t.toString() + "===>");
				System.out.println(t.get_heuPb());
				
				System.out.println(t.get_ans_tmp());
			}
		}
		
		
//		int cnt = 0;
//		for(Table t : table) {
////			System.out.print(t.toString());
//			cnt ++;
//			if(t.getcheck()) {
//				System.out.print(t.toString());
//				System.out.printf("==>(%.2f, %.2f, %.4f) ", t.getAns(), t.getCost(), t.getRemain());
//			}
//			if(cnt%5 == 0)System.out.println();
//			if(cnt%15 == 0)System.out.println();
//			
//		}
		
		
		
	}

}
