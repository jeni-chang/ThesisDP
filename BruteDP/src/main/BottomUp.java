package main;

import java.util.ArrayList;
import java.util.List;

import javax.print.attribute.HashAttributeSet;

public class BottomUp {
	
	private List<Table> table = new ArrayList<>();
	
	private List<Double> pb = new ArrayList<>();
	private List<Double> lc = new ArrayList<>();
	private List<Double> cc = new ArrayList<>();
	private List<Double> r = new ArrayList<>();
	private List<Double> bw = new ArrayList<>();
	private List<Double> com = new ArrayList<>();
	private double f; 
	
	private int layer;
	private int server;
	private int cp;
	
	public BottomUp(List<Table> table, List<Double> pb, List<Double> lc, List<Double> cc, List<Double> r, List<Double> bw, List<Double> com, Double f, int layer, int server, int cp) {
		this.table = table;
		this.pb =pb;
		this.lc = lc;
		this.cc =cc;
		this.r = r;
		this.bw = bw;
		this.com = com;
		this.f = f;
		
		this.layer = layer;
		this.server = server;
		this.cp = cp;				
		
	}
	
	public void compute(double pb_threshold) {
		int cnt = 0;
		int num_cnt = 0;
		for(Table t: table) {
			cnt ++;
			if(t.getcheck()) {
				System.out.print("Compute " + t.toString());
				List<Double> ans_tmp = new ArrayList<>(); 
				double ctime = 0.0;  // computing time
				double ttime = 0.0;  // transmission time
				double ratio = 1.0;
				double ans = 0.0;
				double cost = 0.0;
				double pb = 1.0;
				
				num_cnt++;
				
				if(t.getS() == 1) {
					for(int i=1; i<=t.getL(); i++) {
						ctime = ctime + lc.get(i);
						ratio = ratio * r.get(i); 
					}
					
					if(t.getC() == 0) {
						ctime = ctime / com.get(1);
						ttime = f*ratio / bw.get(1);
						
						cost = ctime + ttime;
					}
					else if(t.getC() == 1) {
						ctime = (ctime+cc.get(t.getL())) / com.get(1);
						ttime = f*ratio / bw.get(1);
						
						ans = this.pb.get(t.getL())*ctime;
						cost = ctime + ttime;
						pb = (1-this.pb.get(t.getL()));
					}
					
					ans_tmp.clear();
					ans_tmp.add(ans);
					ans_tmp.add(cost);
					ans_tmp.add(pb);
					t.setPb(pb, ans_tmp);
					t.setRemain(pb);
					t.setAns(ans);
					t.setCost(cost);
					
				}
				// server != 1
				else {
					Table tmp = null;
					Table min_t = null;
					double min_ans = Double.MAX_VALUE;
					double min_cost = Double.MAX_VALUE;
					// find local minimum
					for(int i=0; i<cnt-1; i++) {
						tmp = this.table.get(i);
						if(DP.check(t.getL(), t.getS(), t.getC(), tmp.getL(), tmp.getS(), tmp.getC())) {
							if(t.getS() == tmp.getS()+1 && tmp.getC()+1 >= t.getC()) {
								ctime = 0.0;  // computing time
								ttime = 0.0;  // transmission time
								ratio = 1.0;
								ans = 0.0;
								cost = 0.0;
								pb = 1.0;
								
								System.out.print("==> " + tmp.toString());
								
								for(int j = tmp.getL()+1; j<=t.getL(); j++) {
									ctime = ctime + lc.get(j);
								}
								for(int j = 0; j<=t.getL(); j++) {
									ratio = ratio * r.get(j);
								}
								ttime = f*ratio / bw.get(t.getS());
								
								// test all probability 
								if(t.getC() == 0) {
									ctime = ctime / com.get(t.getS());
									cost = ctime + ttime;
									cost = cost + tmp.getPb().get(1.0).get(1);
									
									if(cost < min_cost) {
										min_cost = cost;										
									}
									ans_tmp.clear();
									ans_tmp.add(ans);
									ans_tmp.add(min_cost);
									ans_tmp.add(pb);
									t.setPb(pb, ans_tmp);
									t.setAns(ans);
									t.setCost(min_cost);
									t.setRemain(pb);
									
								}
								// check point != 0
								else {	
									if(t.getC() - tmp.getC() == 1) ctime = (ctime + cc.get(t.getL()))/com.get(t.getS());
									else ctime = ctime / com.get(t.getS());
									
									for(int j=0; j<=t.getC(); j++) {
										for(double d: PbCombin.pb_combin.get(j)) {
											double opt_ans = 0;
											double opt_cost = 0;
											double opt_remain = 0;
											double opt_predict = Double.MAX_VALUE;
											int flag = 0;
											for(double cand: tmp.getPb().keySet()) {
												double remain = 0;
												double predict = 0;
												cost = 0;
												if(cand <= d) {
													flag = 1;
													if(t.getC() - tmp.getC() == 1) {
														ans = this.pb.get(t.getL())*tmp.getPb().get(cand).get(2)*(ctime + tmp.getPb().get(cand).get(1)) + tmp.getPb().get(cand).get(0);
														remain = (1-this.pb.get(t.getL()))*tmp.getPb().get(cand).get(2);
													}
													else {
														ans = tmp.getPb().get(cand).get(0);
														remain = tmp.getPb().get(cand).get(2);
													}
													cost = tmp.getPb().get(cand).get(1) + ctime + ttime;
													predict = ans + cost*remain;
													
													if(predict < opt_predict) {
														opt_ans = ans;
														opt_cost = cost;
														opt_remain = remain;
														opt_predict = predict;
													}
												}
											}
											if(flag != 0) {
												ans_tmp = new ArrayList<>();
												ans_tmp.add(opt_ans);
												ans_tmp.add(opt_cost);
												ans_tmp.add(opt_remain);
												ans_tmp.add(opt_predict);
												
												if(t.getPb().containsKey(d)) {
//													System.out.println("---" + ans_tmp + "---");
//													System.out.println("***" + t.getPb() + "***");
													if(opt_predict < t.getPb().get(d).get(3)) t.setPb(d, ans_tmp);
												}
												else {
													
													t.setPb(d, ans_tmp);
//													System.out.println("***" + t.getPb() + "***");
												}
												
												flag = 0;
											}
											
										}
									}
								}
							}
							
						}
					}
					
				}
				
				System.out.println();
			}
		}
		System.out.println("TOTAL ======== " + cnt);
	}
}
