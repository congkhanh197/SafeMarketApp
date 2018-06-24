package com.group13.safemarket.models;

/**
 * Created by Khanh Tran-Cong on 5/17/2018.
 * Email: congkhanh197@gmail.com
 */
public class Feedback {
	String name;
	String detail;
	String userID;

	public Feedback() {
	}

	public Feedback(String name, String detail, String userID) {
		this.name = name;
		this.detail = detail;
		this.userID = userID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}
}
