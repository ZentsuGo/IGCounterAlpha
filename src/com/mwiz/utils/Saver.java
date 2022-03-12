package com.mwiz.utils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.mwiz.igcount.Listener;
import com.mwiz.igcount.ScrapeInfo;
import com.mwiz.igcount.ScrapeUserInfo;
import com.mwiz.igcount.UserProfile;

public class Saver {
	private String s = "		";
	
	public static void loadAccountFile() throws IOException {
		String path = Listener.SAVE_BASE_PATH;
		String file_name = "account.txt";
		
		
		File dir = new File(path);
		
		if (!dir.exists()) {
			FileUtils.forceMkdir(dir);
		}
		
		//saving data into file part
		File file = new File(path + file_name);
		
		
		if (!file.exists()) {
			FileUtils.touch(file);
			return;
		}
		
		String content = FileUtils.readFileToString(file, StandardCharsets.UTF_8.name());
		
		if (content != null) {
			if (!content.isEmpty()) {
				String[] credentials = content.split(", ");
				Listener.USERNAME = credentials[0];
				Listener.PASSWORD = credentials[1];
			}
		}
	}
	
	public static void saveAccountFile() throws IOException {
		String path = Listener.SAVE_BASE_PATH;
		String file_name = "account.txt";
		
		
		File dir = new File(path);
		
		if (!dir.exists()) {
			FileUtils.forceMkdir(dir);
		}
		
		//saving data into file part
		File file = new File(path + file_name);
		
		
		if (!file.exists()) {
			FileUtils.touch(file);
		}
		
		String content = FileUtils.readFileToString(file, StandardCharsets.UTF_8.name());

		if (!content.equals(Listener.USERNAME + ", " + Listener.PASSWORD)) {
			FileUtils.writeStringToFile(file, Listener.USERNAME + ", " + Listener.PASSWORD, StandardCharsets.UTF_8.name());
		} else return;
	}
	public static void loadProfilesFile() throws IOException {
		//file to arraylist from listener
		String path = Listener.SAVE_BASE_PATH;
		String file_name = "saved_users.txt";
		
		
		File dir = new File(path);
		
		if (!dir.exists()) {
			FileUtils.forceMkdir(dir);
		}
		
		//saving data into file part
		File file = new File(path + file_name);
		
		
		if (!file.exists()) {
			FileUtils.touch(file);
			return;
		}
		
		List<String> list = FileUtils.readLines(file, StandardCharsets.UTF_8.name());
		
		if (list != null) {
			if (!list.isEmpty()) {
				for (String user : list) {
					new ScrapeUserInfo(user, IGUtils.getUserId(user), 0, 5).save();
					Listener.users_pic.put(user, IGUtils.getProfilePicture(user));
				}
			}
		}
	}
	
	public static void saveProfilesFile() throws IOException {
		//arraylist from listener to file
		String path = Listener.SAVE_BASE_PATH;
		String file_name = "saved_users.txt";
		
		
		File dir = new File(path);
		
		if (!dir.exists()) {
			FileUtils.forceMkdir(dir);
		}
		
		//saving data into file part
		File file = new File(path + file_name);
		
		
		if (!file.exists()) {
			FileUtils.touch(file);
		}
		
		List<String> list = FileUtils.readLines(file, StandardCharsets.UTF_8.name());

		if (!list.equals(Utils.hashToArray(Listener.saved_users))) {
			FileUtils.writeLines(file, StandardCharsets.UTF_8.name(), Utils.hashToArray(Listener.saved_users), false);
		} else return;
	}
	
	public static void loadLocationFile() throws IOException {
		String path = Listener.SAVE_BASE_PATH;
		File dir = new File(path);
		
		if (!dir.exists()) {
			FileUtils.forceMkdir(dir);
		}
		
		//saving data into file part
		File file = new File(path + "save_location_path.txt");
		
		if (!file.exists()) {
			FileUtils.touch(file);
			return;
		}
		
		String content = FileUtils.readFileToString(file, StandardCharsets.UTF_8.name());
		if (content != null) {
			if (!content.isEmpty())
				Listener.SAVE_LOCATION_PATH = content;
			else
				Listener.SAVE_LOCATION_PATH = Listener.SAVE_BASE_PATH + "ScrapedUsers" + File.separator;
		} else {
			Listener.SAVE_LOCATION_PATH = Listener.SAVE_BASE_PATH + "ScrapedUsers" + File.separator;
		}
	}
	
	public static void saveLocationFile() throws IOException {
		String path = Listener.SAVE_BASE_PATH;
		File dir = new File(path);
		
		if (!dir.exists()) {
			FileUtils.forceMkdir(dir);
		}
		
		//saving data into file part
		File file = new File(path + "save_location_path.txt");
		
		if (!file.exists()) {
			FileUtils.touch(file);
		}
		
		FileUtils.writeStringToFile(file, Listener.SAVE_LOCATION_PATH, StandardCharsets.UTF_8.name());
	}
	
	public static void saveScrapingFile(String username, ScrapeInfo si, int min_count) throws IOException {
		//get data into variables part
		int likes = si.getLikes();
		ArrayList<UserProfile> likers = si.getUsers_profiles();
		String post_link = si.getPost_link();
		String data = ("Likers for post " + si.getPost_code() + " that have more than " + min_count + " likes\n"
				+ "Post link : " + post_link + "\n"
				+ si.getLikes() + " likes :\n\n"
				+ Utils.arrayToUsernames(likers)).replaceAll("\n", System.getProperty("line.separator"));
		//new file with post id
		String path = Listener.SAVE_LOCATION_PATH + username + File.separator;
		String file_name = si.getPost_code() + ".txt";
		
		File dir = new File(path);
		
		if (!dir.exists()) {
			FileUtils.forceMkdir(dir);
		}
		
		//saving data into file part
		File file = new File(path + file_name);
		
		if (!file.exists()) {
			FileUtils.touch(file);
		}
		
		String content = FileUtils.readFileToString(file, StandardCharsets.UTF_8.name());

		if (!content.equals(data)) {
			FileUtils.writeStringToFile(file, data, StandardCharsets.UTF_8.name());
		} else return;
	}
}
