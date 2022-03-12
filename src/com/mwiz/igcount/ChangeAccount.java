package com.mwiz.igcount;

import javax.swing.JDialog;
import javax.swing.JFrame;
import java.awt.Color;
import javax.swing.JTextField;

import com.mwiz.utils.Saver;

import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.event.ActionEvent;

public class ChangeAccount extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField textField;
	private JTextField textField_1;
	
	public ChangeAccount(JFrame parent, String title) {
		super(parent, title);
		getContentPane().setBackground(Color.WHITE);
		setSize(400, 220);
		setLocationRelativeTo(parent);
		setResizable(false);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(null);
		
		textField = new JTextField(Listener.USERNAME);
		textField.setFont(new Font("Yu Gothic UI", Font.PLAIN, 13));
		textField.setBounds(10, 35, 376, 27);
		getContentPane().add(textField);
		textField.setColumns(10);
		
		textField_1 = new JTextField(Listener.PASSWORD);
		textField_1.setFont(new Font("Yu Gothic UI", Font.PLAIN, 13));
		textField_1.setBounds(10, 97, 376, 27);
		getContentPane().add(textField_1);
		textField_1.setColumns(10);
		
		JLabel lblUsername = new JLabel("Username");
		lblUsername.setFont(new Font("Yu Gothic UI", Font.PLAIN, 13));
		lblUsername.setBounds(10, 10, 376, 21);
		getContentPane().add(lblUsername);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setFont(new Font("Yu Gothic UI", Font.PLAIN, 13));
		lblPassword.setBounds(10, 72, 376, 21);
		getContentPane().add(lblPassword);
		
		JButton btnNewButton = new JButton("OK");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!textField.getText().equals(Listener.USERNAME) || !textField_1.getText().equals(Listener.PASSWORD)) {
					Listener.USERNAME = textField.getText();
					Listener.PASSWORD = textField_1.getText();
					Program.lblLoggedIn.setText("< Use Reconnect to connect with the new account");
					Program.lblLoggedIn.setForeground(Color.RED);
					try {
						Saver.saveAccountFile();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
				dispose();
			}
		});
		btnNewButton.setBackground(Color.WHITE);
		btnNewButton.setFont(new Font("Yu Gothic UI", Font.PLAIN, 13));
		btnNewButton.setBounds(68, 134, 243, 32);
		getContentPane().add(btnNewButton);
	}
}
