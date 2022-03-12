package com.mwiz.igcount;

public class ScrapeUserInfo {
	
	private String username;
	private int min_count = 0;
	private int interval = 5;
	private long id;
	
	/**
	 * 
	 * On new added user in SelectProfile.list save data
	 * On selected item in Program.list load data
	 * 
	 */
	
	public ScrapeUserInfo(String username, long id, int min_count, int interval) {
		this.username = username;
		this.min_count = min_count;
		this.interval = interval;
		this.id = id;
	}
	
	public void save() {
		//save data into array listener
		Listener.saved_users.put(username, this);
	}
	
	public static void remove(String username) {
		//remove data from array listener
		if (Listener.saved_users.containsKey(username))
			Listener.saved_users.remove(username);
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getMin_count() {
		return min_count;
	}

	public void setMin_count(int min_count) {
		this.min_count = min_count;
	}

	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
}
