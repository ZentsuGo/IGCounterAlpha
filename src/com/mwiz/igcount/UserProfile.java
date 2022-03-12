package com.mwiz.igcount;

public class UserProfile {
	private String username, full_name, link;
	private long id;
	
	public UserProfile(String username, String full_name, long id) {
		this.username = username;
		this.full_name = full_name;
		this.id = id;
		this.link = "https://wwW.instagram.com/" + username;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFull_name() {
		return full_name;
	}

	public void setFull_name(String full_name) {
		this.full_name = full_name;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
}
