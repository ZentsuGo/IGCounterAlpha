package com.mwiz.igcount;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class Listener {
	public static String USERNAME = "ikctp789";
	public static String PASSWORD = "toproduceanything";
	
	public static String SAVE_BASE_PATH = System.getProperty("user.home") + File.separator + ".IGCounterAlpha" + File.separator;
	public static String SAVE_LOCATION_PATH = SAVE_BASE_PATH + "ScrapedUsers" + File.separator;
	
	//save into file SelectProfile users (scrape user)
	public static HashMap<String, ScrapeUserInfo> saved_users = new HashMap<String, ScrapeUserInfo>();
	//selected profiles to scrape
	public static ArrayList<String> selected_users = new ArrayList<String>();
	public static HashMap<String, BufferedImage> users_pic = new HashMap<String, BufferedImage>();
	
	//time program limit saved from Program.frame before dispose
	public static float program_limit = 0;
}
