package main;

import java.util.ArrayList;
import java.util.List;

public class NewBottomUp {
	private List<Table> table = new ArrayList<>();
	private List<Double> pb = new ArrayList<>();
	private List<Double> lc = new ArrayList<>();
	private List<Double> cc = new ArrayList<>();
	private List<Double> r = new ArrayList<>();
	private List<Double> bw = new ArrayList<>();
	private List<Double> com = new ArrayList<>();
	private List<Double> ls = new ArrayList<>();
	private List<Double> cs = new ArrayList<>();
	private List<Double> sp = new ArrayList<>();
	private double f; 
	private double threshold;
	
	public NewBottomUp(List<Table> table, List<Double> pb, List<Double> lc, List<Double> cc, List<Double> r, List<Double> bw, List<Double> com, Double f, int layer, int server, int cp, List<Double> ls, List<Double> cs, List<Double> sp, double threshold) {
		this.table = table;
		this.pb =pb;
		this.lc = lc;
		this.cc =cc;
		this.r = r;
		this.bw = bw;
		this.com = com;
		this.f = f;
		this.ls = ls;
		this.cs = cs;
		this.sp = sp;
		this.threshold = threshold;
	}
	
	public void compute() {
		int cnt = -1;
		// check all R(i,j,k)
		for(Table t : this.table) {
			cnt ++;
			//check every R(i,j,k) which is need to check
			if(t.getcheck()) {
				List<Double> ans_tmp = new ArrayList<>(); 
				double ctime = 0.0;  // computing time
				double ttime = 0.0;  // transmission time
				double ratio = 1.0;
				double ans = 0.0;
				double cost = 0.0;
				double pb = 1.0;
				
				if(t.getS()==1) {
					if(check_capacity(t) && check_pipeline(t)) {
//						System.out.print("Compute " + t.toString());
						
						for(int i=1; i<=t.getL(); i++) {
							ctime = ctime + lc.get(i);
							ratio = ratio * r.get(i); 
						}
						ttime = f*ratio / bw.get(1);
						
						if(t.getC() == 0) {
							ctime = ctime / com.get(1);
							
							ans = 0.0;
							cost = ctime + ttime;
							pb = 1.0;
						}
						else if(t.getC() == 1) {
							ctime = (ctime+cc.get(t.getL())) / com.get(1);
							
							ans = this.pb.get(t.getL())*ctime;
							cost = ctime + ttime;
							pb = (1-this.pb.get(t.getL()));
						}
						
						ans_tmp = new ArrayList<>();
						ans_tmp.add(ans);
						ans_tmp.add(cost);
						ans_tmp.add(pb);
						ans_tmp.add(-1.0);
						
						fix_pb(t, pb, cost, ans_tmp);
					} // end check capacity
				} // end if
				else {
//					System.out.print("Compute " + t.toString());
					for(int i=0; i<cnt; i++) {
						Table tmp = this.table.get(i);
						if(DP.check(t.getL(), t.getS(), t.getC(), tmp.getL(), tmp.getS(), tmp.getC())) {
							if(t.getS()==tmp.getS()+1 && tmp.getC()+1>=t.getC()) {
								if(tmp.get_capa_check() && check_capacity(t, tmp) && check_pipeline(t, tmp)) {
									// init
									//******************
//									System.out.print("==> " + tmp.toString());
									ctime = 0.0;  // computing time
									ttime = 0.0;  // transmission time
									ratio = 1.0;
									ans = 0.0;
									cost = 0.0;
									pb = 1.0;
									//******************
									// compute cost
									for(int j=tmp.getL()+1; j<=t.getL(); j++) ctime = ctime+lc.get(j);
									// data transmit cost
									for(int j=0; j<=t.getL(); j++) ratio = ratio * r.get(j);
									ttime = f*ratio / bw.get(t.getS());
									// add check point or not
									if(t.getC()-tmp.getC() == 0) ctime = ctime / com.get(t.getS());
									else ctime = (ctime+cc.get(t.getL())) / com.get(t.getS());
									/*New Cost*/
									cost = ctime + ttime;
									
									for(List<Double> ls :tmp.get_ans_tmp()) {
										if(t.getC()-tmp.getC() == 0) {
											ans = ls.get(0);
											cost = cost + ls.get(1);
											pb = ls.get(2);
										}
										else {
											ans = (this.pb.get(t.getL())*ls.get(2)*(ls.get(1)+cost-ttime)) + ls.get(0);
											cost = cost + ls.get(1);
											pb = (1-this.pb.get(t.getL()))*ls.get(2);
										}
										ans_tmp = new ArrayList<>();
										ans_tmp.add(ans);
										ans_tmp.add(cost);
										ans_tmp.add(pb);
										ans_tmp.add((double) tmp.getID());
										ans_tmp.add(ls.get(2));
										
										t.set_ans_tmp(ans_tmp);
										
										ans = 0.0;
										cost = ctime + ttime;
										pb = 1.0;
									}
									
									// heuristic version 1
									for(double d: tmp.get_heuPb().keySet()) {
										List<Double> ls = tmp.get_heuPb().get(d);
										if(t.getC()-tmp.getC() == 0) {
											ans = ls.get(0);
											cost = cost + ls.get(1);
											pb = ls.get(2);
										}
										else {
											ans = (this.pb.get(t.getL())*ls.get(2)*(ls.get(1)+cost-ttime)) + ls.get(0);
											cost = cost + ls.get(1);
											pb = (1-this.pb.get(t.getL()))*ls.get(2);
										}
										ans_tmp = new ArrayList<>();
										ans_tmp.add(ans);
										ans_tmp.add(cost);
										ans_tmp.add(pb);
										ans_tmp.add((double)tmp.getID());
										ans_tmp.add(ls.get(2));
										
										t.set_heu_ans_tmp(ans_tmp);
										
										ans = 0.0;
										cost = ctime + ttime;
										pb = 1.0;
									}
									// heuristic version 2
									for(double d: tmp.get_heuPb2().keySet()) {
										List<Double> ls = tmp.get_heuPb2().get(d);
										if(t.getC()-tmp.getC() == 0) {
											ans = ls.get(0);
											cost = cost + ls.get(1);
											pb = ls.get(2);
										}
										else {
											ans = (this.pb.get(t.getL())*ls.get(2)*(ls.get(1)+cost-ttime)) + ls.get(0);
											cost = cost + ls.get(1);
											pb = (1-this.pb.get(t.getL()))*ls.get(2);
										}
										ans_tmp = new ArrayList<>();
										ans_tmp.add(ans);
										ans_tmp.add(cost);
										ans_tmp.add(pb);
										ans_tmp.add((double)tmp.getID());
										ans_tmp.add(ls.get(2));
										
										t.set_heu_ans_tmp2(ans_tmp);
										
										ans = 0.0;
										cost = ctime + ttime;
										pb = 1.0;
									}
								} // end capacity check								
							} //end check
						} // end DP check
					}
					/*add */
					fix_pb(t);
				}// end else
//				System.out.println();
			} // end if(t.getcheck())
		} //end for(Table t: this.table)
	}
	
	public void fix_pb(Table t) {
		List<Double> ans_tmp;
		for(double d : t.getPb().keySet()) {
			double min = Double.MAX_VALUE;
			ans_tmp = new ArrayList<>();
			int flag = 0;
			for(List<Double> ls : t.get_ans_tmp()) {
				if(ls.get(2) <= d) {
					if(ls.get(0)+(ls.get(1)*ls.get(2)) < min) {
						flag = 1;
						min = ls.get(0)+(ls.get(1)*ls.get(2));
						ans_tmp.clear();
						ans_tmp.add(ls.get(0));
						ans_tmp.add(ls.get(1));
						ans_tmp.add(ls.get(2));
						ans_tmp.add(ls.get(3));
					}
				}
			}
			if(flag==1) {
				t.setPb(d, ans_tmp);
				flag = 0;
			}
		}
		// heuristic version 1
		for(double d : PbCombin.heu_pb_1) {
			double min = Double.MAX_VALUE;
			ans_tmp = new ArrayList<>();
			int flag = 0;
			for(List<Double> ls : t.get_heu_ans_tmp()) {
				if(ls.get(2) <= d) {
					if(ls.get(0)+(ls.get(1)*ls.get(2)) < min) {
						flag = 1;
						min = ls.get(0)+(ls.get(1)*ls.get(2));
						ans_tmp.clear();
						ans_tmp.add(ls.get(0));
						ans_tmp.add(ls.get(1));
						ans_tmp.add(ls.get(2));
						ans_tmp.add(ls.get(3));
					}
				}
			}
			if(flag==1) {
				t.set_heuPb(d, ans_tmp);
				flag = 0;
			}
		}
		
		// heuristic version 2
		for(double d : PbCombin.heu_pb_2) {
			double min = Double.MAX_VALUE;
			ans_tmp = new ArrayList<>();
			int flag = 0;
			for(List<Double> ls : t.get_heu_ans_tmp2()) {
				if(ls.get(2) <= d) {
					if(ls.get(0)+(ls.get(1)*ls.get(2)) < min) {
						flag = 1;
						min = ls.get(0)+(ls.get(1)*ls.get(2));
						ans_tmp.clear();
						ans_tmp.add(ls.get(0));
						ans_tmp.add(ls.get(1));
						ans_tmp.add(ls.get(2));
						ans_tmp.add(ls.get(3));
					}
				}
			}
			if(flag==1) {
				t.set_heuPb2(d, ans_tmp);
				flag = 0;
			}
		}
	}
	
	public void fix_pb(Table t, double pb, double cost, List<Double> ls) {
		for(double d : t.getPb().keySet()) {
			if(pb <= d) {
				if(cost < t.getPb().get(d).get(1)) {
					t.setPb(d, ls);
					if(t.get_ans_tmp().size()==0) t.set_ans_tmp(ls);
					else {
						for(List<Double> tmp : t.get_ans_tmp()) {
							if(tmp.get(0)!=ls.get(0) && tmp.get(1)!=ls.get(1) && tmp.get(2)!=ls.get(2)) {
								t.set_ans_tmp(ls);
							}
						}
					}
					// heuristic version 1
					if(t.get_heu_ans_tmp().size()==0) t.set_heu_ans_tmp(ls);
					else {
						for(List<Double> tmp : t.get_heu_ans_tmp()) {
							if(tmp.get(0)!=ls.get(0) && tmp.get(1)!=ls.get(1) && tmp.get(2)!=ls.get(2)) {
								t.set_heu_ans_tmp(ls);
							}
						}
					}
					// heuristic version 2
					if(t.get_heu_ans_tmp2().size()==0) t.set_heu_ans_tmp2(ls);
					else {
						for(List<Double> tmp : t.get_heu_ans_tmp2()) {
							if(tmp.get(0)!=ls.get(0) && tmp.get(1)!=ls.get(1) && tmp.get(2)!=ls.get(2)) {
								t.set_heu_ans_tmp2(ls);
							}
						}
					}
				}
			}
		}
		
		// heuristic version 1
		for(double d : PbCombin.heu_pb_1) {
			if(pb <= d) {
				if(cost < t.get_heuPb().get(d).get(1)) {
					t.set_heuPb(d, ls);
				}
			}
		}
		
		// heuristic version 2
		for(double d : PbCombin.heu_pb_2) {
			if(pb <= d) {
				if(cost < t.get_heuPb2().get(d).get(1)) {
					t.set_heuPb2(d, ls);
				}
			}
		}
		
		
	}
	
	
	
	// initial pb map
	public void init_pb(int cp) {
		List<Double> tmp = new ArrayList<>();
		tmp.add(0.0);
		tmp.add(Double.MAX_VALUE);
		tmp.add(1.0);
		tmp.add(-2.0);
		
		for(Table t : this.table) {
			for(int i=0; i<=cp; i++) {
				for(double d : PbCombin.pb_combin.get(i)) {
					if(!t.getPb().containsKey(d)) {
						t.setPb(d, tmp);
					}
				}
			}
			
			// heuristic probability
			for(double d : PbCombin.heu_pb_1) t.set_heuPb(d, tmp);
			for(double d : PbCombin.heu_pb_2) t.set_heuPb2(d, tmp);
		}
		
	}

	// only one server
	public boolean check_capacity(Table t) {
		double lsize = 0.0; // total layer size
		
		// compute total layer size
		for(int i=0; i<=t.getL(); i++ ) {
			lsize = lsize + ls.get(i);
		}
		
		if(t.getC() == 1)lsize = lsize + cs.get(t.getL());
		
		if(lsize > sp.get(1)) {
			t.set_capa_check(false);
			return false;
		}
		
		else return true;
	}
	
	// more than one server
	public boolean check_capacity(Table t, Table tmp) {
		double lsize = 0.0;
		
		for(int i=tmp.getL()+1; i<=t.getL(); i++) {
			lsize = lsize + ls.get(i);
		}
		
		if(t.getC() - tmp.getC() == 1)lsize = lsize + cs.get(t.getL());
		
		if(lsize > sp.get(t.getS()))return false;
		
		else return true;
		
	}
	
	public boolean check_pipeline(Table t) {
		double ctime = 0;
		double ratio = 1;
		
		for(int i=0; i<=t.getL(); i++) {
			ctime = ctime + lc.get(i);
			ratio = ratio * r.get(i);
		}
		ctime = ctime / com.get(t.getS());
		ctime = ctime + ratio/bw.get(t.getS());
		
		if(ctime <= threshold) return true;
		else return false;
		
	}
	
	public boolean check_pipeline(Table t, Table tmp) {
		double ctime = 0;
		double ratio = 1;
		
		if(t.getS() - tmp.getS() == 1) {
			for(int i=tmp.getL()+1; i<=t.getL(); i++) ctime = ctime + lc.get(i);
			if(t.getC() - tmp.getC() == 1) ctime = ctime + cc.get(t.getL());
			ctime = ctime / com.get(t.getS());
			for(int i=0; i<=t.getL(); i++) ratio = ratio * r.get(i);
			ctime  = ctime + ratio/bw.get(t.getS());
			
			if(ctime <= threshold) return true;
			else return false;
		}
		
		else {
			for(int i=0; i<=t.getL(); i++) ctime = ctime + lc.get(i);
			if(t.getC() - tmp.getC() == 1) ctime = ctime + cc.get(t.getL());
			ctime = ctime / com.get(t.getS());
			for(int i=0; i<=t.getL(); i++) ratio = ratio * r.get(i);
			ctime  = ctime + ratio/bw.get(t.getS());
			
			if(ctime <= threshold) return true;
			else return false;
		}
		
	}
}
