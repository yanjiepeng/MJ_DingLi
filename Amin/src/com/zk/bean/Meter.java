package com.zk.bean;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

@Table(name = "meter")
public class Meter {

	@Id()
	int _id;
	@Column()
	private int meterId;
	@Column()
	private float va;
	@Column()
	private float vb;
	@Column()
	private float vc;
	@Column()
	private float Ia;
	@Column()
	private float Ib;
	@Column()
	private float Ic;
	@Column()
	private String time;
	@Column()
	private float ene;
	@Column
	private int zhiliu1;
	@Column
	private int zhiliu2;
	@Column
	private float air_current;
	@Column
	private float air_total;
	@Column
	private float si_speed;

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public int getMeterId() {
		return meterId;
	}

	public void setMeterId(int meterId) {
		this.meterId = meterId;
	}

	public float getVa() {
		return va;
	}

	public void setVa(float va) {
		this.va = va;
	}

	public float getVb() {
		return vb;
	}

	public void setVb(float vb) {
		this.vb = vb;
	}

	public float getVc() {
		return vc;
	}

	public void setVc(float vc) {
		this.vc = vc;
	}

	public float getIa() {
		return Ia;
	}

	public void setIa(float ia) {
		Ia = ia;
	}

	public float getIb() {
		return Ib;
	}

	public void setIb(float ib) {
		Ib = ib;
	}

	public float getIc() {
		return Ic;
	}

	public void setIc(float ic) {
		Ic = ic;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public float getEne() {
		return ene;
	}

	public void setEne(float ene) {
		this.ene = ene;
	}


	public int getZhiliu1() {
		return zhiliu1;
	}

	public void setZhiliu1(int zhiliu1) {
		this.zhiliu1 = zhiliu1;
	}

	public int getZhiliu2() {
		return zhiliu2;
	}

	public void setZhiliu2(int zhiliu2) {
		this.zhiliu2 = zhiliu2;
	}
	
	

	public float getAir_current() {
		return air_current;
	}

	public void setAir_current(float air_current) {
		this.air_current = air_current;
	}

	public float getAir_total() {
		return air_total;
	}

	public void setAir_total(float air_total) {
		this.air_total = air_total;
	}

	public float getSi_speed() {
		return si_speed;
	}

	public void setSi_speed(float si_speed) {
		this.si_speed = si_speed;
	}

	public Meter() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Meter(float va, float vb, float vc, float ia, float ib, float ic,
			String time) {
		super();
		this.va = va;
		this.vb = vb;
		this.vc = vc;
		Ia = ia;
		Ib = ib;
		Ic = ic;
		this.time = time;
	}

	public Meter(int meterId, float va, float vb, float vc, float ia, float ib,
			float ic, String time, float ene, int zhiliu1, int zhiliu2) {
		super();
		this.meterId = meterId;
		this.va = va;
		this.vb = vb;
		this.vc = vc;
		Ia = ia;
		Ib = ib;
		Ic = ic;
		this.time = time;
		this.ene = ene;
		this.zhiliu1 = zhiliu1;
		this.zhiliu2 = zhiliu2;
	}

	public Meter(int meterId, float va, float vb, float vc, float ia, float ib,
			float ic, String time, float ene, int zhiliu1, int zhiliu2,
			float air_current, float air_total, float si_speed) {
		super();
		this.meterId = meterId;
		this.va = va;
		this.vb = vb;
		this.vc = vc;
		Ia = ia;
		Ib = ib;
		Ic = ic;
		this.time = time;
		this.ene = ene;
		this.zhiliu1 = zhiliu1;
		this.zhiliu2 = zhiliu2;
		this.air_current = air_current;
		this.air_total = air_total;
		this.si_speed = si_speed;
	}
	
	

}
