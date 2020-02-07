package com.yliu.netValue;

import java.util.Date;

public class NetValue {
	
	private Date time;
	private double value;
	private String id;
	
	public NetValue(Date time, double value, String id) {
		super();
		this.time = time;
		this.value = value;
		this.id = id;
	}

	public NetValue() {
		super();
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	
	
}
