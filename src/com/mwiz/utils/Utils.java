package com.mwiz.utils;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.DefaultListModel;

import com.mwiz.igcount.ScrapeUserInfo;
import com.mwiz.igcount.UserProfile;

public class Utils {
	public static DefaultListModel<String> arrayToModel(ArrayList<String> array) {
		DefaultListModel<String> model = new DefaultListModel<String>();
		for (String o : array) {
			model.addElement(o);
		}
		return model;
	}
	
	public static DefaultListModel<String> hashToList(HashMap<String, ScrapeUserInfo> hash) {
		DefaultListModel<String> model = new DefaultListModel<String>();
		for (String o : hash.keySet()) {
			model.addElement(o);
		}
		return model;
	}
	
	public static ArrayList<String> modelToArray(DefaultListModel<String> model) {
		ArrayList<String> array = new ArrayList<String>();
		for (int i = 0; i < model.size(); i++) {
			array.add(model.getElementAt(i));
		}
		return array;
	}
	
	public static ArrayList<String> hashToArray(HashMap<String, ScrapeUserInfo> hash) {
		ArrayList<String> list = new ArrayList<String>();
		for (String user : hash.keySet()) {
			list.add(user);
		}
		return list;
	}
	
	public static String arrayToUsernames(ArrayList<UserProfile> users_profiles) {
		String users = "";
		for (UserProfile user : users_profiles) {
			users += user.getUsername() + "\n";
		}
		return users.substring(0, users.length() - 1);
	}
	
	public static String arrayToString(ArrayList<UserProfile> users_profiles) {
		String users = "";
		for (UserProfile user : users_profiles) {
			users +=  "	- " + user.getUsername() + " (" + user.getFull_name() + ") : " + user.getId() + "\n";
		}
		return users;
	}
}
