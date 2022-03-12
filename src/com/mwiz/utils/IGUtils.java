package com.mwiz.utils;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.http.client.ClientProtocolException;
import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.requests.InstagramGetMediaLikersRequest;
import org.brunocvcunha.instagram4j.requests.InstagramSearchUsernameRequest;
import org.brunocvcunha.instagram4j.requests.InstagramUserFeedRequest;
import org.brunocvcunha.instagram4j.requests.payload.InstagramFeedItem;
import org.brunocvcunha.instagram4j.requests.payload.InstagramFeedResult;
import org.brunocvcunha.instagram4j.requests.payload.InstagramGetMediaLikersResult;
import org.brunocvcunha.instagram4j.requests.payload.InstagramSearchUsernameResult;
import org.brunocvcunha.instagram4j.requests.payload.InstagramUserSummary;

import com.mwiz.igcount.ScrapeInfo;
import com.mwiz.igcount.ScrapeProgress;
import com.mwiz.igcount.UserProfile;

public class IGUtils {
	private static Instagram4j instagram;
	
	public static void set(Instagram4j instagram) {
		IGUtils.instagram = instagram;
	}
	
	public static Instagram4j getInstagram() {
		return instagram;
	}
	
	private static InstagramSearchUsernameResult getUser(String username) {
		InstagramSearchUsernameResult userResult = null;
		try {
			userResult = instagram.sendRequest(new InstagramSearchUsernameRequest(username));
		} catch (ClientProtocolException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return userResult;
	}
	
	public static boolean isUser(String username) {
		return getInstagram() != null ? (getUser(username).getMessage() == null ? true : false) : false;
	}
	
	public static long getUserId(String username) {
		InstagramSearchUsernameResult userResult = getUser(username);
		long id = userResult.getUser().getPk();
		
		return id;
	}
	
	public static boolean isLoggedIn() {
		return instagram == null ? false : (instagram.isLoggedIn() ? (instagram.getUserId() != 0 ? true : false) : false);
	}
	
	private static InstagramFeedResult getFeedResult(long id, String next_max_id) {
		//get posts
		
		InstagramFeedResult tagFeed = null;
		try {
			if (next_max_id != null)
				tagFeed = instagram.sendRequest(new InstagramUserFeedRequest(id, next_max_id, 0));
			else
				tagFeed = instagram.sendRequest(new InstagramUserFeedRequest(id));
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return tagFeed;
	}
	
	public static int getLikes(long userId, String mediaCode) {
		InstagramFeedResult tagFeed = getFeedResult(userId, null);
		for (InstagramFeedItem feedResult : tagFeed.getItems()) {
			if (feedResult.getCode().equals(mediaCode)) {
				int count = feedResult.getLike_count();
				return count <= 0 ? 0 : count;
			}
		}
		return -1;
	}
	
	public static BufferedImage getProfilePicture(String username) {
		BufferedImage image = null;
		InstagramSearchUsernameResult userResult = getUser(username);
		if (userResult.getUser() == null) return null;
		URL url = null;
		try {
			url = new URL(userResult.getUser().getProfile_pic_url());
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}
		try {
			image = ImageIO.read(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return image;
	}
	
	public static ArrayList<ScrapeInfo> getScrapeInfo(long userId, int min_count) {
			ArrayList<ScrapeInfo> scrapeinfos = new ArrayList<ScrapeInfo>();
			String next_max_id = null;
			InstagramFeedResult tagFeed = getFeedResult(userId, null);
			int counter = 0;
			while (true) {
				ScrapeProgress.lblState.setText("Processing..."); //around 3 seconds for each processing (each divided part)
				for (InstagramFeedItem feedResult : tagFeed.getItems()) {
					int like_count = feedResult.getLike_count();
					if (like_count >= min_count) {
						counter++;
						ScrapeProgress.lblState.setText("Scraping " + counter + " post(s) ...");
						InstagramGetMediaLikersRequest likeFeed = new InstagramGetMediaLikersRequest(feedResult.getPk());
						InstagramGetMediaLikersResult result = null;
						try {
							result = instagram.sendRequest(likeFeed);
						} catch (ClientProtocolException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
						String code = feedResult.getCode();
						String post_link = "https://www.instagram.com/p/" + code;
						if (like_count <= 0) {
							scrapeinfos.add(new ScrapeInfo(new ArrayList<UserProfile>(), 0, code, post_link));
							break;
						}
						List<InstagramUserSummary> users = result.getUsers();
						if (users != null) {
							if (!users.isEmpty()) {
								ArrayList<UserProfile> users_profiles = new ArrayList<UserProfile>();
								for (InstagramUserSummary user : users) {
									users_profiles.add(new UserProfile(user.getUsername(), user.getFull_name(), user.getPk()));
								}
								scrapeinfos.add(new ScrapeInfo(users_profiles, like_count, code, post_link));
							}
						}
					}
				}
				//tagFeed.isMore_available()
				next_max_id = tagFeed.getNext_max_id();
				if (next_max_id == null) {
					break;
				}
				tagFeed = getFeedResult(userId, next_max_id);
			}
			ScrapeProgress.lblState.setText(counter + " posts found");
			return scrapeinfos;
	}
	
	public static HashMap<Long, Integer> getLikesFromAllPosts(long userId) {
		HashMap<Long, Integer> likes = new HashMap<Long, Integer>();
		InstagramFeedResult tagFeed = getFeedResult(userId, null);
		for (InstagramFeedItem feedResult : tagFeed.getItems()) {
			int count = feedResult.getLike_count();
			likes.put(feedResult.getPk(), count);
		}
		return likes;
	}
	
	public static HashMap<Long, List<InstagramUserSummary>> getLikersFromAllPosts(long userId) {
		HashMap<Long, List<InstagramUserSummary>> likers = new HashMap<Long, List<InstagramUserSummary>>();
		InstagramFeedResult tagFeed = getFeedResult(userId, null);
		for (InstagramFeedItem feedResult : tagFeed.getItems()) {
			InstagramGetMediaLikersRequest likeFeed = new InstagramGetMediaLikersRequest(feedResult.getPk());
			InstagramGetMediaLikersResult result = null;
			try {
				result = instagram.sendRequest(likeFeed);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
//		    String url = "https://www.instagram.com/p/" + code; //url
			List<InstagramUserSummary> users = result.getUsers();
			if (users != null) {
				likers.put(feedResult.getPk(), users);
			}
		}
		return likers;
	}
	
	public static String getMediaIdFromCode(String mediaCode) {
		String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_";
		long id = 0;
		for (int i = 0; i < mediaCode.length(); i++) {
			char c = mediaCode.charAt(i);
			id = id * 64 + alphabet.indexOf(c);
		}
		return id + "";
	}
}
