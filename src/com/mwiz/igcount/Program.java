package com.mwiz.igcount;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.mwiz.utils.IGUtils;
import com.mwiz.utils.Saver;
import com.mwiz.utils.Utils;

public class Program extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static Program frame = null;
	private JTextField textField;
	private JTextField textField_1;
	public static JTextField txtFileName;
	public static JLabel lblLoggedIn;
	public static JList<String> list = null;
	private ImagePanel panel_1;
	private static JTextField textField_2;
	
	public static void main(String[] args) {
		try {
		    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch(Exception e){
			e.printStackTrace();
		}
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				frame = new Program();
				frame.setVisible(true);
			}
 		});
	}
	
	public Program() {
		setTitle("IGCounterAlpha");
		setSize(400, 500);
		setResizable(false);
		setLocationRelativeTo(null);
		getContentPane().setBackground(Color.WHITE);
		getContentPane().setLayout(null);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if (SelectProfile.frame != null) {
					SelectProfile.frame.dispose();
					SelectProfile.frame = null;
				}
				frame.dispose();
				frame = null;
			}
		});
		
		JButton btnManageProfile = new JButton("Manage profiles");
		btnManageProfile.setBackground(Color.WHITE);
		btnManageProfile.setFont(new Font("Yu Gothic UI", Font.PLAIN, 13));
		btnManageProfile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (SelectProfile.frame == null) {
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							SelectProfile.frame = new SelectProfile(frame);
						}
					});
				} else if (!SelectProfile.frame.isFocused()) {
	    			SelectProfile.frame.requestFocus();
	    		} else if (!SelectProfile.frame.isVisible()) {
	    			SelectProfile.frame.setVisible(true);
	    		}
			}
		});
		
		btnManageProfile.setBounds(10, 59, 150, 34);
		getContentPane().add(btnManageProfile);
		
		textField = new JTextField();
		textField.addFocusListener(new java.awt.event.FocusAdapter() {
		    public void focusGained(java.awt.event.FocusEvent evt) {
		        SwingUtilities.invokeLater(new Runnable() {
		            @Override
		            public void run() {
		            	textField.selectAll();
		            }
		        });
		    }
		});
		textField.setFont(new Font("Yu Gothic UI", Font.PLAIN, 12));
		textField.setText("0");
		textField.setBounds(170, 315, 206, 25);
		textField.setColumns(10);
		textField.getDocument().addDocumentListener(new DocumentListener() {
			private void correct() {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						float nb = -1;
						try {
							nb = Float.parseFloat(textField.getText());
						} catch (NumberFormatException exception) {
							textField.setText("0");
							return;
						}
						if (nb < 0) {
							textField.setText("0");
						}
					}
				});
			}
			@Override
			public void insertUpdate(DocumentEvent e) {
				correct();
				updateScrapeUser(textField.getText());
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				correct();
				updateScrapeUser(textField.getText());
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				correct();
				updateScrapeUser(textField.getText());
			}
		});
		getContentPane().add(textField);
		
		JLabel lblPostsOver = new JLabel("Min. like count");
		lblPostsOver.setFont(new Font("Yu Gothic UI", Font.PLAIN, 13));
		lblPostsOver.setBounds(170, 288, 216, 25);
		getContentPane().add(lblPostsOver);
		
		JLabel lblProfile = new JLabel("Profile :");
		lblProfile.setFont(new Font("Yu Gothic UI", Font.PLAIN, 13));
		lblProfile.setBounds(10, 98, 150, 25);
		getContentPane().add(lblProfile);
		
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel.setBackground(Color.WHITE);
		panel.setBounds(10, 10, 376, 39);
		getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel lblIgcounteralpha = new JLabel("IGCounterAlpha");
		lblIgcounteralpha.setHorizontalAlignment(SwingConstants.CENTER);
		lblIgcounteralpha.setBounds(0, 0, 376, 39);
		panel.add(lblIgcounteralpha);
		lblIgcounteralpha.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 16));
		
		JButton btnScrape = new JButton("Scrape");
		if (Scrape.isScraping) {
			btnScrape.setEnabled(false);
			btnScrape.setText("Scraping...");
		}
		btnScrape.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (list.getModel() == null) {
					return;
				}
				if (list.getModel().getSize() == 0)
					return;
				
				//jdialog to confirm
				Object[] options = {"Yes",
	                    "Cancel"};
				int confirm = JOptionPane.showOptionDialog(frame,
				    "Are you sure you want to scrape with these informations ?",
				    "Scrape confirm",
				    JOptionPane.YES_NO_OPTION,
				    JOptionPane.PLAIN_MESSAGE,
				    null,
				    options,
				    options[1]);
				
				if (confirm == JOptionPane.YES_OPTION) {
					if (!Listener.selected_users.isEmpty()) {
						Listener.program_limit = Float.parseFloat(textField_2.getText());
						new Scrape().startScraping();
						btnScrape.setEnabled(false);
						btnScrape.setText("Scraping...");
						frame.dispose();
						frame = null;
						ScrapeProgress.lblState.setText("Starting scraping...");
						ScrapeProgress.frame.requestFocus();
					}
				}
			}
		});
		btnScrape.setBackground(Color.WHITE);
		btnScrape.setFont(new Font("Yu Gothic UI", Font.PLAIN, 13));
		btnScrape.setBounds(281, 397, 95, 34);
		getContentPane().add(btnScrape);
		
		panel_1 = new ImagePanel(null);
		panel_1.setBackground(Color.WHITE);
		panel_1.setBounds(10, 128, 150, 150);
		getContentPane().add(panel_1);
		panel_1.setLayout(null);
		
		JLabel lblScrapeInterval = new JLabel("Scrape interval (min)");
		lblScrapeInterval.setFont(new Font("Yu Gothic UI", Font.PLAIN, 13));
		lblScrapeInterval.setBounds(10, 288, 150, 25);
		getContentPane().add(lblScrapeInterval);
		
		textField_1 = new JTextField();
		textField_1.addFocusListener(new java.awt.event.FocusAdapter() {
		    public void focusGained(java.awt.event.FocusEvent evt) {
		        SwingUtilities.invokeLater(new Runnable() {
		            @Override
		            public void run() {
		            	textField_1.selectAll();
		            }
		        });
		    }
		});
		//on value changed getuserscrapeinfo to save new data to save into listener to get them when scraping (multiple accounts) by username reference
		textField_1.setFont(new Font("Yu Gothic UI", Font.PLAIN, 12));
		textField_1.setText("30");
		textField_1.getDocument().addDocumentListener(new DocumentListener() {
			private void correct() {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						float nb = -1;
						try {
							nb = Float.parseFloat(textField_1.getText());
						} catch (NumberFormatException exception) {
							textField_1.setText("30");
							return;
						}
//						if ((Float.parseFloat(textField_2.getText()) != 0) && (nb > Float.parseFloat(textField_2.getText()))) {
//							textField_2.setText(String.valueOf((nb * 2 + 5)));
//						}
						
						if (nb < 30) {
							textField_1.setText("30");
						}
					}
				});
			}
			@Override
			public void insertUpdate(DocumentEvent e) {
				correct();
				updateScrapeUser2(textField_1.getText());
			}
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				correct();
				updateScrapeUser2(textField_1.getText());
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				correct();
				updateScrapeUser2(textField_1.getText());
			}
		});
		textField_1.setBounds(10, 315, 150, 25);
		getContentPane().add(textField_1);
		textField_1.setColumns(10);
		
		txtFileName = new JTextField();
		txtFileName.setEditable(false);
		txtFileName.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
		txtFileName.setText(Listener.SAVE_LOCATION_PATH);
		txtFileName.setBounds(10, 350, 366, 25);
		txtFileName.setColumns(10);
		getContentPane().add(txtFileName);
		
		JButton btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
//						new ScrapeProgress(frame);
						if (SelectProfile.frame != null) {
							SelectProfile.frame.dispose();
							SelectProfile.frame = null;
						}
						frame.dispose();
						frame = null;
					}
				});
			}
		});
		btnBack.setBackground(Color.WHITE);
		btnBack.setFont(new Font("Yu Gothic UI", Font.PLAIN, 13));
		btnBack.setBounds(10, 397, 105, 34);
		getContentPane().add(btnBack);
		
		JLabel lblSelectedProfiles = new JLabel("Selected Profile(s) :");
		lblSelectedProfiles.setFont(new Font("Yu Gothic UI", Font.PLAIN, 13));
		lblSelectedProfiles.setBounds(170, 64, 206, 25);
		getContentPane().add(lblSelectedProfiles);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnOptions = new JMenu("Options");
		menuBar.add(mnOptions);
		
		JMenu mnFiles = new JMenu("Files");
		mnOptions.add(mnFiles);
		
		JMenuItem mntmSaveLocation = new JMenuItem("Save location");
		mntmSaveLocation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser filechooser = new JFileChooser(new File(Listener.SAVE_LOCATION_PATH));
				filechooser.setDialogTitle("Choose a save location path");
				filechooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				filechooser.setAcceptAllFileFilterUsed(false);
				
				if (filechooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
					File dir = filechooser.getSelectedFile();
					Listener.SAVE_LOCATION_PATH = dir.getPath() + File.separator;
					try {
						Saver.saveLocationFile();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					list.clearSelection();
					txtFileName.setText(Listener.SAVE_LOCATION_PATH);
				}
			}
		});
		mnFiles.add(mntmSaveLocation);
		
		JMenuItem mntmNewMenuItem = new JMenuItem("Change account");
		mntmNewMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ChangeAccount ca = new ChangeAccount(frame, "Change Account");
				ca.setVisible(true);
			}
		});
		mntmNewMenuItem.setFont(new Font("Yu Gothic UI", Font.PLAIN, 13));
		mnOptions.add(mntmNewMenuItem);
		
		JMenuItem mntmReconnect = new JMenuItem("Reconnect");
		mntmReconnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//jdialog sure
				dispose();
				frame = null;
				IGConnection.frame = new IGConnection(null);
			}
		});
		mnOptions.add(mntmReconnect);
		
		list = new JList<String>(Utils.arrayToModel(Listener.selected_users));
		list.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				//new selection
				if (!e.getValueIsAdjusting() && list.getSelectedValue() != null) {
					String username = list.getSelectedValue();
					BufferedImage picture = Listener.users_pic.get(username);
					lblProfile.setText("Profile : " + username);
					txtFileName.setText(username + ".txt");
					panel_1.setImage(picture);
					getContentPane().repaint();
					
					int min_count = Listener.saved_users.get(username).getMin_count();
					int interval = Listener.saved_users.get(username).getInterval();
					
					textField.setText(String.valueOf(min_count));
					textField_1.setText(String.valueOf(interval));
					txtFileName.setText(Listener.SAVE_LOCATION_PATH + username + File.separator);
				}
			}
		});
		list.setFont(new Font("Yu Gothic UI", Font.PLAIN, 13));
		list.setToolTipText("");
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		list.setVisibleRowCount(-1);
		((DefaultListCellRenderer) list.getCellRenderer()).setHorizontalAlignment(JLabel.CENTER);
		getContentPane().add(list);
		
		JScrollPane listScroller = new JScrollPane(list);
		listScroller.setToolTipText("Hold CTRL to select many profiles");
		listScroller.setSize(206, 180);
		listScroller.setLocation(170, 98);
		listScroller.setPreferredSize(new Dimension(216, 279));
		getContentPane().add(listScroller);
		
		textField_2 = new JTextField();
		textField_2.setFont(new Font("Yu Gothic UI", Font.PLAIN, 13));
		textField_2.setText("0");
		textField_2.setBounds(125, 410, 94, 19);
		textField_2.setColumns(10);
		textField_2.getDocument().addDocumentListener(new DocumentListener() {
			private void correct() {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						float nb = -1;
						try {
							nb = Float.parseFloat(textField_2.getText());
						} catch (NumberFormatException exception) {
							textField_2.setText("0");
							return;
						}
						if (nb > 300) { //5hoursmax
							textField_2.setText("300");
						} else if (nb < 0) {
							textField_2.setText("0");
						}
					}
				});
			}
			@Override
			public void insertUpdate(DocumentEvent e) {
				correct();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				correct();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				correct();
			}
		});
		getContentPane().add(textField_2);
		
		JLabel lblScrapeLimit = new JLabel("Scrape limit (0 = infinite)");
		lblScrapeLimit.setFont(new Font("Yu Gothic UI", Font.PLAIN, 12));
		lblScrapeLimit.setBounds(125, 395, 146, 13);
		getContentPane().add(lblScrapeLimit);
		
		JLabel lblHours = new JLabel("min(s)");
		lblHours.setFont(new Font("Yu Gothic UI", Font.PLAIN, 13));
		lblHours.setBounds(229, 410, 42, 21);
		getContentPane().add(lblHours);
		
		lblLoggedIn = new JLabel("Logged : wait...");
		lblLoggedIn.setFont(new Font("Yu Gothic UI", Font.PLAIN, 13));
		lblLoggedIn.setForeground(Color.LIGHT_GRAY);
		menuBar.add(lblLoggedIn);
		
		if (IGUtils.isLoggedIn()) {
			if (IGUtils.isLoggedIn()) {
				Program.lblLoggedIn.setText("Logged In");
				Program.lblLoggedIn.setForeground(Color.GREEN);
			} else {
				Program.lblLoggedIn.setText("Not logged in, reopen parameters to retry or change account.");
				Program.lblLoggedIn.setForeground(Color.RED);
			}
		}
		
		setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/igclogo.png")));
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}
	
	public static float getProgramLimit() {
		return Listener.program_limit;
	}
	
	private void updateScrapeUser(String min_count) {
		int mincount = -1;
		try {
			mincount = Integer.parseInt(min_count);
		} catch (NumberFormatException e) {
			return;
		}
		if (list.getSelectedValue() != null)
			Listener.saved_users.get(list.getSelectedValue()).setMin_count(mincount);
	}
	
	private void updateScrapeUser2(String interval) {
		int interval_ = -1;
		try {
			interval_ = Integer.parseInt(interval);
		} catch (NumberFormatException e) {
			return;
		}
		if (list.getSelectedValue() != null)
			Listener.saved_users.get(list.getSelectedValue()).setInterval(interval_);
	}
}
