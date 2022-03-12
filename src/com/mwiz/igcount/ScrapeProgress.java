package com.mwiz.igcount;

import java.awt.AWTException;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;

import com.mwiz.UI.ProgressCircleUI;
import com.mwiz.utils.IGUtils;

import java.awt.Font;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;

public class ScrapeProgress extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static ScrapeProgress frame;
	public static JProgressBar progress;
	public static JLabel lblState;
	private TrayIcon trayIcon;
    private SystemTray tray;
	
	public static void main(String[] args) {
		try {
		    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		    UIManager.put("ProgressBar.selectionBackground", Color.BLACK);
		} catch(Exception e){
			e.printStackTrace();
		}
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				frame = new ScrapeProgress(null);
			}
		});
	}

	public ScrapeProgress(Program program) {
		setTitle("IGCounterAlpha - ScrapeProgress");
		setSize(450, 200);
		setLocationRelativeTo(program);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setBackground(Color.WHITE);
	    getContentPane().setLayout(null);
	    addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
                Scrape.shutdownScheduler();
				if (SelectProfile.frame != null)
					SelectProfile.frame.dispose();
				if (Program.frame != null)
					Program.frame.dispose();
				if (IGConnection.frame != null)
					IGConnection.frame.dispose();
				System.exit(0);
			}
	    });
	    
	    progress = new JProgressBar();
	    progress.setBounds(10, 10, 162, 143);
	    progress.setBackground(Color.WHITE);
	    progress.setUI(new ProgressCircleUI());
	    progress.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
	    progress.setStringPainted(true);
	    progress.setFont(new Font("Yu Gothic UI", Font.PLAIN, 21));
	    progress.setString("Scrape");
	    progress.setForeground(Color.LIGHT_GRAY);
	    progress.setValue(100);
	    getContentPane().add(progress);
	    
	    JButton btnCancelScrape = new JButton("Cancel Scrape");
	    btnCancelScrape.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		//stop the timer of scrape that use igutils and stop progressbar
	    		if (IGUtils.isLoggedIn()) {
		    		Scrape.cancelScraping();
	    		}
	    	}
	    });
	    btnCancelScrape.setBackground(Color.WHITE);
	    btnCancelScrape.setFont(new Font("Yu Gothic UI", Font.PLAIN, 13));
	    btnCancelScrape.setBounds(182, 100, 244, 35);
	    getContentPane().add(btnCancelScrape);
	    
	    JButton btnParameters = new JButton("Parameters");
	    btnParameters.setBackground(Color.WHITE);
	    btnParameters.setFont(new Font("Yu Gothic UI", Font.PLAIN, 13));
	    btnParameters.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		if (Program.frame == null) {
		    		SwingUtilities.invokeLater(new Runnable() {
		    			public void run() {
		    				if (!IGUtils.isLoggedIn()) {
		    					IGConnection.frame = new IGConnection(null);
		    				} else {
		    					Program.frame = new Program();
		    					Program.frame.setVisible(true);
		    				}
		    			}
		    		});
	    		} else if (!Program.frame.isFocused()){
	    			Program.frame.requestFocus();
	    		} else if (!Program.frame.isVisible()) {
	    			Program.frame.setVisible(true);
	    		}
	    	}
	    });
	    btnParameters.setBounds(182, 55, 244, 35);
	    getContentPane().add(btnParameters);
	    
	    lblState = new JLabel("Click on Parameters to start");
	    lblState.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
	    lblState.setBounds(182, 29, 244, 16);
	    getContentPane().add(lblState);
	    
	    if(SystemTray.isSupported()){
            tray = SystemTray.getSystemTray();

            Image image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/igclogo.png"));
            
            ActionListener exitListener=new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Scrape.shutdownScheduler();
    				if (SelectProfile.frame != null)
    					SelectProfile.frame.dispose();
    				if (Program.frame != null)
    					Program.frame.dispose();
                    System.exit(0);
                }
            };
            
            MenuItem defaultItem=new MenuItem("Show");
            PopupMenu popup=new PopupMenu();
            defaultItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    setVisible(true);
                    setExtendedState(JFrame.NORMAL);
                }
            });
            popup.add(defaultItem);
            
            defaultItem=new MenuItem("Exit");
            defaultItem.addActionListener(exitListener);
            popup.add(defaultItem);
            
            trayIcon = new TrayIcon(image, "IGCounterAlpha", popup);
            trayIcon.setImageAutoSize(true);
        }
	    
        addWindowStateListener(new WindowStateListener() {
            public void windowStateChanged(WindowEvent e) {
                if(e.getNewState()==ICONIFIED){
                    try {
                        tray.add(trayIcon);
                        setVisible(false);
                    } catch (AWTException ex) {
                    	ex.printStackTrace();
                    }
                }
                if(e.getNewState()==7){
	                try{
	                	tray.add(trayIcon);
	                	setVisible(false);
		            }catch(AWTException ex){
		            	ex.printStackTrace();
		            }
            	}
	        	if(e.getNewState()==MAXIMIZED_BOTH){
                    tray.remove(trayIcon);
                    setVisible(true);
                }
                if(e.getNewState()==NORMAL){
                    tray.remove(trayIcon);
                    setVisible(true);
                }
            }
        });
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/igclogo.png")));
		setVisible(true);
	}
	
	private static Timer timer = null;
	
	public static void cancelScraping() {
		if (timer != null)
			timer.stop();
		progress.setForeground(Color.LIGHT_GRAY);
		progress.setValue(100);
		progress.setString("Cancelled");
		lblState.setText("Scrape cancelled");
		frame.requestFocus();
	}
	
	public static void doneScraping() {
		if (timer != null)
			timer.stop();
		progress.setForeground(Color.ORANGE);
		progress.setValue(100);
		lblState.setText("Scraping done");
		progress.setString("Done");
		frame.setExtendedState(JFrame.NORMAL);
		frame.requestFocus();
	}
	
	public static void startScraping() {
		if (frame == null) return;
		progress.setForeground(Color.GREEN);
		if (Scrape.getProgress() == 101) {//infinite state
			progress.setValue(100);
			progress.setString("No time limit"); //no time limit
		} else if (Scrape.getProgress() == -1) {
			progress.setValue(0);
			progress.setString("Not scraping / error");
		} else {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					timer = new Timer(2000, new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							int value = Scrape.getProgress();
							if (value == -1) {//not scraping / error
								progress.setValue(0);
								progress.setString("Not scraping / error");
								timer.stop();
								return;
							} else {
								if (value >= 100) {
									progress.setValue(100);
									progress.setString("Scraping done");
									timer.stop();
									return;
								} else {
									progress.setValue(Scrape.getProgress());
									int min = Scrape.getMinLeft();
									if (min == -1) {
										timer.stop();
										return;
									}
									progress.setString(min + " min left");
								}
							}
						}
					});
					timer.start();
				}
			});
		}
	}
}
