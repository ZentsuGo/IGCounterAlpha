package com.mwiz.igcount;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.mwiz.utils.IGUtils;
import com.mwiz.utils.Saver;

public class Scrape {
	private HashMap<String, ScrapeUserInfo> users;
	public static boolean isScraping = false;
    public static ScheduledExecutorService scheduler;
    public static HashMap<String, ScheduledFuture<?>> refscrapes;
    private static ScheduledFuture<?> refscrape2 = null;
	
	public Scrape() {
		this.users = new HashMap<String, ScrapeUserInfo>();
		//selected users hashmap with scrapeuserinfo
		scheduler = Executors.newScheduledThreadPool(1);
		refscrapes = new HashMap<String, ScheduledFuture<?>>();
		for (String user : Listener.saved_users.keySet()) {
			if (Listener.selected_users.contains(user)) {
				this.users.put(user, Listener.saved_users.get(user));
			}
		}
	}
	
	public void startScraping() {
		if (IGUtils.isLoggedIn()) {
			isScraping = true;
			scrape();
		} else {
			return;
		}
	}
	
	private void scrape() {
		long scrape_limit = (long) Program.getProgramLimit();
		for (String user : users.keySet()) {
			ScrapeUserInfo info = users.get(user);
			int min_count = info.getMin_count();
			int interval = info.getInterval();
			long id = info.getId();
			//new thread with the interval
			ScrapeProgress.lblState.setText("Starting scraping " + user + "...");
			final ScheduledFuture<?> scrapeHandle = scheduler.scheduleAtFixedRate(new Runnable() {
				public void run() {
					ArrayList<ScrapeInfo> scrapeinfos = IGUtils.getScrapeInfo(id, min_count);
					int count = 0;
					for (ScrapeInfo si : scrapeinfos) {
//						ScrapeProgress.lblState.setText(user + "'s scrape : " + count + "/" + scrapeinfos.size());
						++count;
						try {
							Saver.saveScrapingFile(user, si, min_count);
						} catch (IOException e) {
							e.printStackTrace();
						}
						if (scrapeinfos.size() - count == 0) {
							ScrapeProgress.lblState.setText(user + "'s scrape done - next in " + interval + " min(s)");
						}
					}
				}
			}, 0, interval, TimeUnit.MINUTES);
			
			refscrapes.put(user, scrapeHandle);
			
			if (scrape_limit > 0) {
				final ScheduledFuture<?> scrapeHandle2 = scheduler.schedule(new Runnable() {
					public void run() {
						if (isScraping)
							isScraping = false;
						refscrapes.get(user).cancel(true);
						refscrapes.remove(user);
						refscrape2.cancel(true);
						refscrape2 = null;
						ScrapeProgress.lblState.setText("Scrape limit reached - task done");
						ScrapeProgress.doneScraping();
					}
				}, scrape_limit, TimeUnit.MINUTES);
				refscrape2 = scrapeHandle2;
			}
		}
		ScrapeProgress.startScraping();
	}
	
	public static void shutdownScheduler() {
		if (scheduler != null) {
			if (!scheduler.isShutdown() || !scheduler.isTerminated()) {
				scheduler.shutdown();
				try {
				    if (!scheduler.awaitTermination(1000, TimeUnit.MILLISECONDS)) {
				    	scheduler.shutdownNow();
				    	System.out.println("Scheduler shutdown now");
				    } 
				} catch (InterruptedException e) {
					scheduler.shutdownNow();
					System.out.println("Scheduler shutdown now - interruptedexception");
				}
			}
		}
	}
	
	//return the interval time left
	public static int getProgress() { //101 = 0 infinite // -1 is not scraping / error // otherwise just getdelay
		return (int) (isScraping ? (refscrape2 == null ? (Program.getProgramLimit() == 0 ? 101 : -1) : 
			(refscrape2.getDelay(TimeUnit.SECONDS) * 100 / (Program.getProgramLimit() * 60)) //delay converted to percentage (cross product) delay * 100 / max
			) : -1);
	}
	
	public static int getMinLeft() {
		return (int) (isScraping ? (refscrape2 != null ? Math.max(1, refscrape2.getDelay(TimeUnit.MINUTES)) : 0) : -1);
	}
	
	public static void cancelScraping() {
		if (isScraping) {
			for (String scrape : refscrapes.keySet()) {
				ScheduledFuture<?> task = refscrapes.get(scrape);
				if (!task.isDone())
					task.cancel(true);
			}
			if (refscrape2 != null) {
				if (!refscrape2.isDone())
					refscrape2.cancel(true);
			}
			
			ScrapeProgress.cancelScraping();
			
			refscrapes.clear();
			refscrape2 = null;
			isScraping = false;
		}
	}
	
	public boolean isScraping() {
		return isScraping;
	}
}
