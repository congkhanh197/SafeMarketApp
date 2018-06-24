package com.group13.safemarket.models;

/**
 * Created by Khanh Tran-Cong on 5/3/2018.
 * Email: congkhanh197@gmail.com
 */
public class Product {
	private String id;
    private String pictureUrl;
    private String name;
    private String type;
    private String address;
    private String price;
    private String detail;
    private String userID;
    private String isApproved;
    private String isSold;

	public Product() {
	}

	public Product(String pictureUrl, String name, String type, String address,
				   String price, String detail, String userID, String isApproved, String isSold) {
		this.pictureUrl = pictureUrl;
		this.name = name;
		this.type = type;
		this.address = address;
		this.price = price;
		this.detail = detail;
		this.userID = userID;
		this.isApproved = isApproved;
		this.isSold = isSold;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPictureUrl() {
		return pictureUrl;
	}

	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
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

	public String getIsApproved() {
		return isApproved;
	}

	public void setIsApproved(String isApproved) {
		this.isApproved = isApproved;
	}

	public String getIsSold() {
		return isSold;
	}

	public void setIsSold(String isSold) {
		this.isSold = isSold;
	}
}

