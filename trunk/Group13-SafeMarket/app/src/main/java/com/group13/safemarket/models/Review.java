package com.group13.safemarket.models;

/**
 * Created by Khanh Tran-Cong on 5/11/2018.
 * Email: congkhanh197@gmail.com
 */
public class Review {
	private String reviewerID;
//	private String productID;
	// TODO add review for product
	private String userID;
	private String point;

	public Review() {
	}

	public Review(String reviewerID, String userID, String reviewPoint) {
		this.reviewerID = reviewerID;
		this.userID = userID;
		this.point = reviewPoint;
	}

	public String getReviewerID() {
		return reviewerID;
	}

	public void setReviewerID(String reviewerID) {
		this.reviewerID = reviewerID;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getPoint() {
		return point;
	}

	public void setPoint(String point) {
		this.point = point;
	}
}
