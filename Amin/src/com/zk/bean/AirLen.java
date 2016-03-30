package com.zk.bean;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

@Table(name = "airlen")
public class AirLen {

	@Id()
	int _id;
	@Column
	private String time;
	@Column
	private int air;
	@Column
	private float air_total;
	@Column
	private float si_speed;
	@Column
	private float si_len;

	
	
	

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getAir() {
		return air;
	}

	public void setAir(int air) {
		this.air = air;
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

	public float getSi_len() {
		return si_len;
	}

	public void setSi_len(float si_len) {
		this.si_len = si_len;
	}

	public AirLen(int air, float si_speed, String time) {
		super();
		this.air = air;
		this.si_speed = si_speed;
		this.time = time;
	}
	
	public AirLen(String time, int air, float air_total, float si_speed,
			float si_len) {
		super();
		this.time = time;
		this.air = air;
		this.air_total = air_total;
		this.si_speed = si_speed;
		this.si_len = si_len;
	}

	public AirLen() {
		super();
		// TODO Auto-generated constructor stub
	}
}
