package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Table {
	private int L; // layer
	private int S; // server
	private int C; // check point
	private boolean check;
	private boolean capacity_check;
	private double ans;
	private double cost;
	private double remain;
	private Map<Double, List<Double>> pb; // <probability, [ans, cost, remain data]>
	private Map<Double, List<Double>> heu_pb; // <probability, [ans, cost, remain data]>
	private Map<Double, List<Double>> heu_pb_2; // <probability, [ans, cost, remain data]>
	private List<List<Double>> ans_tmp; // { [E, cost, remain data, base on which ID, base ID remain data], [], ...} 
	private List<List<Double>> heu_ans_tmp; // { [E, cost, remain data], [], ...} 
	private List<List<Double>> heu_ans_tmp_2; // { [E, cost, remain data], [], ...} 
	private int id;
	private static int cnt = 0;
	
	public Table() {
		this.id = ++cnt;
		this.ans = -1.0;
		this.cost = -1.0;
		this.check = false;
		this.capacity_check = true;
		this.pb = new HashMap<>();
		this.heu_pb = new HashMap<>();
		this.heu_pb_2 = new HashMap<>();
		this.ans_tmp = new ArrayList<>();
		this.heu_ans_tmp = new ArrayList<>();
		this.heu_ans_tmp_2 = new ArrayList<>();
	}
	
	public void setL(int layer) {
		this.L = layer;
	}
	
	public void setS(int server) {
		this.S = server;
	}
	
	public void setC(int cp) {
		this.C = cp;
	}
	
	public void setAns(double ans) {
		this.ans = ans;
	}
	
	public void setCost(double cost) {
		this.cost = cost;
	}
	
	public void setPb(double pb, List<Double> cost) {
		this.pb.put(pb, cost);
	}
	
	public void set_heuPb(double pb, List<Double> cost) {
		this.heu_pb.put(pb, cost);
	}
	
	public void set_heuPb2(double pb, List<Double> cost) {
		this.heu_pb_2.put(pb, cost);
	}
	
	public void setcheck(boolean b) {
		this.check = b;
	}
	
	// set capacity check
	public void set_capa_check(boolean b) { 
		this.capacity_check = b;
	}
	
	public void setRemain(double d) {
		this.remain = d;
	}
	
	public void set_ans_tmp(List<Double> ls) {
		this.ans_tmp.add(ls);
	}
	
	public void set_heu_ans_tmp(List<Double> ls) {
		this.heu_ans_tmp.add(ls);
	}
	
	public void set_heu_ans_tmp2(List<Double> ls) {
		this.heu_ans_tmp_2.add(ls);
	}
	
	public int getID() {
		return this.id;
	}
	
	public int getL() {
		return this.L;
	}
	
	public int getS() {
		return this.S;
	}
	
	public int getC() {
		return this.C;
	}
	
	public double getAns() {
		return this.ans;
	}
	
	public double getCost() {
		return this.cost;
	}
	
	public Map<Double, List<Double>> getPb() {
		return this.pb;
	}
	
	public Map<Double, List<Double>> get_heuPb() {
		return this.heu_pb;
	}
	
	public Map<Double, List<Double>> get_heuPb2() {
		return this.heu_pb_2;
	}
	
	public boolean getcheck() {
		return this.check;
	}
	public boolean get_capa_check() {
		return this.capacity_check;
	}
	
	public double getRemain() {
		return this.remain;
	}
	
	public List<List<Double>> get_ans_tmp(){
		return this.ans_tmp;
	}
	
	public List<List<Double>> get_heu_ans_tmp(){
		return this.heu_ans_tmp;
	}
	
	public List<List<Double>> get_heu_ans_tmp2(){
		return this.heu_ans_tmp_2;
	}
	
	@Override
	public String toString() {
		return String.format("R(%d, %d, %d)", L, S, C);
	}
	
}
