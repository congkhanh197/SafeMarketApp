package com.group13.safemarket.models;

import com.google.firebase.database.Query;

import java.util.ArrayList;

/**
 * Created by Khanh Tran-Cong on 5/11/2018.
 * Email: congkhanh197@gmail.com
 */
public class GroupProduct {
	private String title;
	private Query ref;

	public GroupProduct() {
	}

	public GroupProduct(String title, Query ref) {
		this.title = title;
		this.ref = ref;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Query getRef() {
		return ref;
	}

	public void setRef(Query ref) {
		this.ref = ref;
	}
}
