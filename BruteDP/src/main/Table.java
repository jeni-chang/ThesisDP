package main;

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
	private Map<Double, List<Double>> pb; // <probability, [ans, cost]>
	private int id;
	private String choose = "Begin!!!";
	private static int cnt = 0;
	
	public Table() {
		this.id = ++cnt;
		this.ans = -1.0;
		this.cost = -1.0;
		this.check = false;
		this.capacity_check = true;
		this.pb = new HashMap<>();
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
	
	public void setcheck(boolean b) {
		this.check = b;
	}
	
	// set capacity check
	public void set_capa_check(boolean b) { 
		this.capacity_check = b;
	}
	
	public void setChoose(String s) {
		this.choose = s;
	}
	
	public void setRemain(double d) {
		this.remain = d;
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
	
	public boolean getcheck() {
		return this.check;
	}
	public boolean get_capa_check() {
		return this.capacity_check;
	}
	
	public String getChoose() {
		return this.choose;
	}
	
	public double getRemain() {
		return this.remain;
	}
	
	@Override
	public String toString() {
		return String.format("R(%d, %d, %d)", L, S, C);
	}
	
}
