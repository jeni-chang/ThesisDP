package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub\
		List<Table> table = new ArrayList<>();
		
		int layer = 5;
		int server = 3;
		double choose = 3;
		List<Double> pb = new ArrayList<>();
		List<Double> heu_pb_1 = new ArrayList<>();
		List<Double> heu_pb_2 = new ArrayList<>();
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
//		pb.add(0.4);
//		pb.add(0.95);
//		pb.add(0.5);
		pb.add(1.0);
		
		lc.add(0.0);
		lc.add(300.0);
		lc.add(240.0);
		lc.add(360.0);
		lc.add(120.0);
		lc.add(180.0);
//		lc.add(100.0);
//		lc.add(90.0);
//		lc.add(80.0);
//		lc.add(70.0);
		
		cc.add(0.0);
		cc.add(400.0);
		cc.add(350.0);
		cc.add(280.0);
		cc.add(160.0);
//		cc.add(85.0);
//		cc.add(75.0);
//		cc.add(65.0);
		cc.add(0.0);
		
		r.add(1.0);
		r.add(4.0);
		r.add(0.5);
		r.add(3.0);
		r.add(1.0);
//		r.add(2.0);
//		r.add(1.0);
//		r.add(0.5);
		r.add(0.0);
		
		bw.add(0.0);
		bw.add(200.0);
		bw.add(200.0);
		bw.add(Double.MAX_VALUE);
		
		com.add(0.0);
		com.add(10.0);
		com.add(12.0);
		com.add(8.0);
//		com.add(10.0);
//		com.add(10.0);
//		com.add(10.0);
		
		ls.add(0.0);
		ls.add(30.0);
		ls.add(24.0);
		ls.add(36.0);
		ls.add(12.0);
		ls.add(18.0);
//		ls.add(10.0);
//		ls.add(9.0);
//		ls.add(8.0);
//		ls.add(7.0);
		
		cs.add(0.0);
		cs.add(40.0);
		cs.add(35.0);
		cs.add(28.0);
		cs.add(16.0);
//		cs.add(8.5);
//		cs.add(7.5);
//		cs.add(6.5);
		cs.add(0.0);
		
		sp.add(0.0);
		sp.add(2000.0);
		sp.add(2500.0);
		sp.add(2000.0);				
		
		
		/* create table */
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
		/*---------------------------------*/
		/* randomly choose heuristic probability */
		heu_pb_1.add(0.0);
		List<Integer> tmp = new ArrayList<>();
		for(int i=1; i<layer; i++) tmp.add(i);
		Collections.shuffle(tmp);
		for(int i=0; i<choose-1; i++) heu_pb_1.add(pb.get(tmp.get(i)));
		heu_pb_1.add(1.0);

		/* average choose heuristic probability */
		List<Double> heu_tmp = new ArrayList<>();
		for(double d: pb) heu_tmp.add(d);
		Collections.sort(heu_tmp);
		int ceiling = (int) Math.ceil(layer/choose);
		heu_pb_2.add(0.0);
		for(int i=1; i<choose; i++) heu_pb_2.add(heu_tmp.get(i*ceiling));
		heu_pb_2.add(1.0);
		
		/* compute probability combination */
		PbCombin cbin = new PbCombin(pb, heu_pb_1, heu_pb_2);
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
	      cbin.compute_pb(s, i, 0);
	    }
	    
	    /* compute heuristic probability version 1 (random) */
	    
	    s = null;
		from = new char[(int)choose];
		for(int i=0; i<(int)choose; i++) {
			char c = (char)(i+1+'0');
			from[i] = c;
		}
	    to = new char[from.length];
	    
	    init = new HashSet<>();
	    init.add(1.0);
	    PbCombin.heu_pb_combin.put(0, init);
	    for (int i = 1; i <= from.length; i++) {
	      s = cbin.comb(from, to, i, from.length, i);
	      cbin.compute_pb(s, i, 1);
	    }
	    cbin.map_to_list(PbCombin.heu_pb_combin, PbCombin.heu_pb_1);
	    /* compute heuristic probability version 2 (average) */
	    
	    s = null;
		from = new char[(int)choose];
		for(int i=0; i<(int)choose; i++) {
			char c = (char)(i+1+'0');
			from[i] = c;
		}
	    to = new char[from.length];
	    
	    init = new HashSet<>();
	    init.add(1.0);
	    PbCombin.heu_pb_combin_2.put(0, init);
	    for (int i = 1; i <= from.length; i++) {
	      s = cbin.comb(from, to, i, from.length, i);
	      cbin.compute_pb(s, i, 2);
	    }
	    
	    cbin.map_to_list(PbCombin.heu_pb_combin_2, PbCombin.heu_pb_2);
	    
	    System.out.println(PbCombin.heu_pb_combin_2);
	    System.out.println("ggggggggg==>>> " + PbCombin.heu_pb_2);	    
	    
	    for(int i : PbCombin.pb_combin.keySet()) {
	    	System.out.println(i + " ==> " + PbCombin.pb_combin.get(i));
	    }
	    /*---------------------------------*/
		/* compute Brute DP and heuristic DP */ 
		new DP(table, layer, server);
		for(int i=1; i<= server; i++) DP.recursive(layer, server, i);
//		table = r.get_table();
		
		
		NewBottomUp btmup = new NewBottomUp(table, pb, lc, cc , r, bw, com, f, layer, server, 3, ls , cs, sp);
		btmup.init_pb(server);
		btmup.compute();
		
		Table opt = null;
		for(Table t: table) {
			if(t.getL() == 5 && t.getS() == 3 && t.getC() == 3) {
				System.out.print(t.toString() + "===>");
				System.out.println(t.getPb());
				
				System.out.print(t.toString() + "===>");
				System.out.println(t.get_heuPb());
				
				opt = t;
				System.out.println(t.get_heu_ans_tmp());
//				for(double d: t.get_heuPb().keySet()) {
//					for(Double l: t.get_heuPb().get(d)) {
//						System.out.println(l);
//					}
//				}
//				System.out.println(t.get_heuPb().get(1.0).get(1));
				
			}
		}
		/*------------------------------*/
		/* find the optimal and heuristic optimal*/
		double min = Double.MAX_VALUE;
		List<Double> opt_ls = new ArrayList<>(); //[answer, opt_id, probability]
		opt_ls.add(0.0);
		opt_ls.add(0.0);
		opt_ls.add(0.0);
		List<Double> heu_opt_ls = new ArrayList<>(); //[answer, opt_id, probability]
		heu_opt_ls.add(0.0);
		heu_opt_ls.add(0.0);
		heu_opt_ls.add(0.0);
		for(List<Double> l: opt.get_ans_tmp()) {
			if(l.get(0) <= min) {
				min = l.get(0);
				opt_ls.set(0, l.get(0));
				opt_ls.set(1, l.get(3));
				opt_ls.set(2, l.get(4));
			}
		}
		min = Double.MAX_VALUE;
		for(List<Double> l: opt.get_heu_ans_tmp()) {
			if(l.get(0) <= min) {
				min = l.get(0);
				heu_opt_ls.set(0, l.get(0));
				heu_opt_ls.set(1, l.get(3));
				heu_opt_ls.set(2, l.get(4));
			}
		}
		
		System.out.println("Optimal Solution ==> " + opt_ls);
		System.out.println("Heuristic Optimal Solution ==> " + heu_opt_ls);
		
		// get check point location
		List<Table> cp_loc = new ArrayList<>();
		cp_loc.add(opt);
		while(opt_ls.get(1) != -1.0){
			for(Table t: table) {
				if(t.getID() == opt_ls.get(1).intValue()) {
					min = Double.MAX_VALUE;
//					System.out.println(t.toString());
					cp_loc.add(t);
					for(List<Double> l: t.get_ans_tmp()) {
						if(l.get(0) <= min && l.get(2) == opt_ls.get(2)) {
							min = l.get(0);
							opt_ls.set(0, l.get(0));
							opt_ls.set(1, l.get(3));
							if(l.get(3) != -1.0)opt_ls.set(2, l.get(4));
						}
					}
					break;
				}
			}
		}
		List<Table> heu_cp_loc = new ArrayList<>();
		heu_cp_loc.add(opt);
		while(heu_opt_ls.get(1) != -1.0){
			for(Table t: table) {
				if(t.getID() == heu_opt_ls.get(1).intValue()) {
					min = Double.MAX_VALUE;
//					System.out.println(t.toString());
					heu_cp_loc.add(t);
					for(List<Double> l: t.get_heu_ans_tmp()) {
						if(l.get(0) <= min && l.get(2) == heu_opt_ls.get(2)) {
							min = l.get(0);
							heu_opt_ls.set(0, l.get(0));
							heu_opt_ls.set(1, l.get(3));
							if(l.get(3) != -1.0)heu_opt_ls.set(2, l.get(4));
						}
					}
					break;
				}
			}
		}
		// the check point's layer
		List<Integer> cp_layer = new ArrayList<>();
		for(int i=0; i<cp_loc.size(); i++) {
			if(i != cp_loc.size()-1) {
				if(cp_loc.get(i).getC() - cp_loc.get(i+1).getC() == 1) {
					cp_layer.add(cp_loc.get(i).getL());
				}
			}
			else {
				if(cp_loc.get(i).getC() ==1) {
					cp_layer.add(cp_loc.get(i).getL());
				}
			}
			
//			System.out.println("Ans ==> " + cp_loc.get(i).toString());
		}
		List<Integer> heu_cp_layer = new ArrayList<>();
		for(int i=0; i<heu_cp_loc.size(); i++) {
			if(i != heu_cp_loc.size()-1) {
				if(heu_cp_loc.get(i).getC() - heu_cp_loc.get(i+1).getC() == 1) {
					heu_cp_layer.add(heu_cp_loc.get(i).getL());
				}
			}
			else {
				if(heu_cp_loc.get(i).getC() ==1) {
					heu_cp_layer.add(heu_cp_loc.get(i).getL());
				}
			}
			
//			System.out.println("Ans ==> " + cp_loc.get(i).toString());
		}
		cp_layer.add(0);
		heu_cp_layer.add(0);
		Collections.reverse(cp_layer);
		Collections.reverse(heu_cp_layer);
		System.out.println("Opt Ans ==> " + cp_layer);
		System.out.println("Heuristic Ans ==> " + heu_cp_layer);
		
		/* Cloud only */
		Cloud cloud = new Cloud(pb, lc, cc, bw , com, f);
		cloud.compute(cp_layer);
		cloud.compute(heu_cp_layer);
		cloud.init_compute();
		
		/* Device only */
		Device device = new Device(pb, lc, cc, bw , com, f);
		device.compute(cp_layer);
		device.compute(heu_cp_layer);
		device.init_compute();
		
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
