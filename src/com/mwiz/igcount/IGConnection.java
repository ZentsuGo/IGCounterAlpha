package com.mwiz.igcount;

import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.Timer;

import com.mwiz.UI.ProgressCircleUI;
import com.mwiz.utils.IGUtils;

public class IGConnection extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static IGConnection frame = null;

	public IGConnection(JFrame frame) {
		setTitle("Connecting to Instagram...");
		setSize(200, 160);
		setUndecorated(true);
		setLocationRelativeTo(frame);
		getContentPane().setBackground(new Color(1f, 1f, 1f, 0.3f));
		setBackground(new Color(1f, 1f, 1f, 0.3f));
		setResizable(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		getContentPane().setLayout(null);
		
	    JProgressBar progress = new JProgressBar();
	    progress.setBounds(10, 10, 176, 143);
	    progress.setBackground(new Color(1f, 1f, 1f, 0.3f));
	    progress.setUI(new ProgressCircleUI());
	    progress.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
	    progress.setStringPainted(true);
	    progress.setFont(new Font("Yu Gothic UI", Font.PLAIN, 21));
	    progress.setString("Connecting");
	    progress.setForeground(Color.GREEN);
	    getContentPane().add(progress);
	    
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/igclogo.png")));
	    
	    (new Timer(100, e -> {
	      int iv = Math.min(100, progress.getValue() + 1);
	      progress.setValue(iv);
	    })).start();
		
		setVisible(true);
		Program.frame = new Program();
		
		startConnection();
	}
	
	private void startConnection() {
		System.out.println("Connection started");
		Thread connection = new Thread(new Runnable() {
			@Override
			public void run() {
				System.out.println("In connection");
				new IGCount();
				Program.frame.setVisible(true);
				frame.dispose();
			}
		});
		connection.start();
	}
}
