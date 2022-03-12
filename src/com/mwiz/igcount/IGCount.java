package com.mwiz.igcount;

import java.awt.Color;
import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.brunocvcunha.instagram4j.Instagram4j;

import com.mwiz.utils.IGUtils;
import com.mwiz.utils.Saver;

public class IGCount {
	private static Instagram4j instagram = null;
	public IGCount() {
		//bot account
		System.out.println("Trying to connect");
		
		instagram = Instagram4j.builder().username(Listener.USERNAME).password(Listener.PASSWORD).build();
		instagram.setup();
		try {
			instagram.login();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("Logged in");
		
		int CONNECTION_TIMEOUT = 30 * 1000;
		int SO_TIMEOUT = 30 * 1000;
		boolean STALE_CONNECTION_CHECK = true;
		boolean SO_KEEPALIVE = true;
		int RETRY_COUNT = 5;
		
		URIBuilder builder = null;
		try {
			builder = new URIBuilder("https://www.instagram.com/");
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
			Program.lblLoggedIn.setText("Warning : URIBuilder error");
		}
		
		
		if(builder != null) {
			builder.setParameter("http.socket.timeout", String.valueOf(SO_TIMEOUT)).setParameter("http.connection.timeout", String.valueOf(CONNECTION_TIMEOUT))
			.setParameter("http.connection.stalecheck", String.valueOf(STALE_CONNECTION_CHECK)).setParameter("ttp.socket.keepalive", String.valueOf(SO_KEEPALIVE))
			.setParameter("http.protocol.content-charset", "UTF-8");
			
			HttpGet request = null;
			try {
				request = new HttpGet(builder.build());
			} catch (URISyntaxException e1) {
				e1.printStackTrace();
			}
			
			if (request != null) {
				Builder builder2 = RequestConfig.custom();
				builder2.setConnectionRequestTimeout(1000).setMaxRedirects(RETRY_COUNT);
				request.setConfig(builder2.build());
			}
		}
		
		System.out.println("Builders set !");
		
		IGUtils.set(instagram);
		
		if (IGUtils.isLoggedIn()) {
			try {
				Saver.loadAccountFile();
				Saver.loadLocationFile();
				Saver.loadProfilesFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if (IGUtils.isLoggedIn()) {
			Program.lblLoggedIn.setText("Logged In");
			Program.lblLoggedIn.setForeground(Color.GREEN);
		} else {
			Program.lblLoggedIn.setText("Not logged in, reopen parameters to retry or change account.");
			Program.lblLoggedIn.setForeground(Color.RED);
		}
		
		if (Program.txtFileName != null)
			Program.txtFileName.setText(Listener.SAVE_LOCATION_PATH);
	}
}
