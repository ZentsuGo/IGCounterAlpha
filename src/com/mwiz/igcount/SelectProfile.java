package com.mwiz.igcount;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.mwiz.utils.IGUtils;
import com.mwiz.utils.Saver;
import com.mwiz.utils.Utils;

public class SelectProfile extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static SelectProfile frame = null;
	private JTextField txtUsername;
	
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		
		new SelectProfile(null);
	}

	public SelectProfile(Program program) {
		setTitle("Select profile");
		setSize(250, 450);
		setLocationRelativeTo(program);
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		getContentPane().setBackground(Color.WHITE);
		getContentPane().setLayout(null);
		
		JList<String> list = new JList<String>(Utils.hashToList(Listener.saved_users));
		list.setBounds(1, 1, 225, 245);
		list.setFont(new Font("Yu Gothic UI", Font.PLAIN, 13));
		list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		list.setVisibleRowCount(-1);
		list.setSelectionModel(new DefaultListSelectionModel() {
		    /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
		    public void setSelectionInterval(int index0, int index1) {
		        if(super.isSelectedIndex(index0)) {
		            super.removeSelectionInterval(index0, index1);
		        }
		        else {
		            super.addSelectionInterval(index0, index1);
		        }
		    }
		});
		((DefaultListCellRenderer) list.getCellRenderer()).setHorizontalAlignment(JLabel.CENTER);
		getContentPane().add(list);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				save(list);
				frame = null;
			}
		});
		
		JScrollPane listScroller = new JScrollPane(list);
		listScroller.setToolTipText("Hold CTRL to select many profiles");
		listScroller.setSize(216, 247);
		listScroller.setLocation(10, 156);
		listScroller.setPreferredSize(new Dimension(216, 279));
		getContentPane().add(listScroller);
		
		txtUsername = new JTextField();
		txtUsername.setFont(new Font("Yu Gothic UI", Font.PLAIN, 13));
		txtUsername.setBounds(10, 50, 216, 19);
		getContentPane().add(txtUsername);
		txtUsername.setColumns(10);
		
		JLabel lblSelectProfile = new JLabel("Select Profile");
		lblSelectProfile.setBounds(10, 10, 216, 30);
		getContentPane().add(lblSelectProfile);
		lblSelectProfile.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 14));
		
		JButton btnAddProfile = new JButton("Add profile");
		btnAddProfile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!txtUsername.getText().trim().isEmpty() && !txtUsername.getText().contains("-")) {
					String user = txtUsername.getText().toLowerCase();
					if (!((DefaultListModel<String>) list.getModel()).contains(user)) {
						if (IGUtils.isUser(user)) {
							lblSelectProfile.setText("Select Profile");
							((DefaultListModel<String>) list.getModel()).addElement(user);
							//save user info into listener to use it later when scraping the different users
							new ScrapeUserInfo(txtUsername.getText(), IGUtils.getUserId(txtUsername.getText()), 0, 5).save();
							//save user pic for better performances on load
							Listener.users_pic.put(txtUsername.getText(), IGUtils.getProfilePicture(txtUsername.getText()));
							
							txtUsername.setText("");
						} else {
							lblSelectProfile.setText("Select Profile - User not found");
						}
					}
				}
			}
		});
		btnAddProfile.setFont(new Font("Yu Gothic UI", Font.PLAIN, 13));
		btnAddProfile.setBackground(Color.WHITE);
		btnAddProfile.setBounds(10, 79, 216, 21);
		getContentPane().add(btnAddProfile);
		
		JButton btnDeleteProfile = new JButton("Delete profile(s)");
		btnDeleteProfile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DefaultListModel<String> listmodel = ((DefaultListModel<String>) list.getModel());
				int length = list.getSelectedIndices().length;
				for (int i = 0; i < length; i++) {
					//remove user info listener
					ScrapeUserInfo.remove(listmodel.getElementAt(list.getSelectedIndices()[0]));
					Listener.users_pic.remove(listmodel.getElementAt(list.getSelectedIndices()[0]));
					listmodel.removeElementAt(list.getSelectedIndices()[0]);
				}
			}
		});
		btnDeleteProfile.setFont(new Font("Yu Gothic UI", Font.PLAIN, 13));
		btnDeleteProfile.setBackground(Color.WHITE);
		btnDeleteProfile.setBounds(10, 103, 216, 19);
		getContentPane().add(btnDeleteProfile);
		
		JButton btnSelectTheseProfiles = new JButton("Select profile(s)");
		btnSelectTheseProfiles.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Listener.selected_users.clear();
				for (String username : list.getSelectedValuesList()) {
					Listener.selected_users.add(username);
				}
				if (Program.list != null) {
					Program.list.setModel(Utils.arrayToModel(Listener.selected_users));
				}
				frame = null;
				save(list);
				dispose();
			}
		});
		btnSelectTheseProfiles.setFont(new Font("Yu Gothic UI", Font.PLAIN, 13));
		btnSelectTheseProfiles.setBackground(Color.WHITE);
		btnSelectTheseProfiles.setBounds(10, 127, 216, 19);
		getContentPane().add(btnSelectTheseProfiles);
		
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/igclogo.png")));
		
//		JButton btnSaveProfiles = new JButton("Save profile(s)");
//		btnSaveProfiles.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				
//			}
//		});
//		btnSaveProfiles.setFont(new Font("Yu Gothic UI", Font.PLAIN, 13));
//		btnSaveProfiles.setBackground(Color.WHITE);
//		btnSaveProfiles.setBounds(10, 127, 226, 21);
//		getContentPane().add(btnSaveProfiles);
		
		setVisible(true);
	}
	
	private void save(JList<String> list) {
		//save
		try {
			Saver.saveProfilesFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
