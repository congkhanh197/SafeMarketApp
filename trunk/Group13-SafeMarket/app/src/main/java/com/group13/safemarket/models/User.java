package com.group13.safemarket.models;

/**
 * Created by Khanh Tran-Cong on 5/3/2018.
 * Email: congkhanh197@gmail.com
 */
public class User {
    private String name;
    private String mail;
    private String photoUrl;
    private String phone;
    private String identifyCard;
    private String address;
    private String reviewStar;

    public User() {
    }

	public User(String name, String mail, String photoUrl, String phone, String identifyCard, String address, String reviewStar) {
		this.name = name;
		this.mail = mail;
		this.photoUrl = photoUrl;
		this.phone = phone;
		this.identifyCard = identifyCard;
		this.address = address;
		this.reviewStar = reviewStar;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getIdentifyCard() {
		return identifyCard;
	}

	public void setIdentifyCard(String identifyCard) {
		this.identifyCard = identifyCard;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getReviewStar() {
		return reviewStar;
	}

	public void setReviewStar(String reviewStar) {
		this.reviewStar = reviewStar;
	}
}

