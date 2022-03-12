package com.mwiz.igcount;

import java.util.ArrayList;

public class ScrapeInfo {
	private ArrayList<UserProfile> users_profiles = new ArrayList<UserProfile>();
	private int likes;
	private String post_code;
	private String post_link;
	
	public ScrapeInfo(ArrayList<UserProfile> users_profiles, int likes, String post_code, String post_link) {
		this.users_profiles = users_profiles;
		this.likes = likes;
		this.post_code = post_code;
		this.post_link = post_link;
	}
	
	public String getPost_code() {
		return post_code;
	}

	public void setPost_code(String post_code) {
		this.post_code = post_code;
	}

	public String getPost_link() {
		return post_link;
	}

	public void setPost_link(String post_link) {
		this.post_link = post_link;
	}

	public ArrayList<UserProfile> getUsers_profiles() {
		return users_profiles;
	}

	public void setUsers_profiles(ArrayList<UserProfile> users_profiles) {
		this.users_profiles = users_profiles;
	}

	public int getLikes() {
		return likes;
	}

	public void setLikes(int likes) {
		this.likes = likes;
	}
}
