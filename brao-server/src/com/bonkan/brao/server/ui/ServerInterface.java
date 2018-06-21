package com.bonkan.brao.server.ui;

import java.time.LocalDateTime;

import javax.swing.JFrame;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ServerInterface {

	private static JFrame mainWindow;
	private static JTextArea mainTextArea;
	private static JScrollPane verticalScrollPane;
	
	/**
	 * <p>Initializes the widgets</p>
	 */
	public static void init()
	{
		mainWindow = new JFrame("Kryo Server");
		mainWindow.setSize(400, 201);
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainWindow.setLocationRelativeTo(null);
		mainWindow.setResizable(false);
		mainWindow.getContentPane().setLayout(null);
		
		mainTextArea = new JTextArea();
		mainTextArea.setEditable(false);
		mainTextArea.setBounds(0, 0, 384, 150);
		
		verticalScrollPane = new JScrollPane(mainTextArea);
		verticalScrollPane.setBounds(0, 0, 394, 172);
		mainWindow.getContentPane().add(verticalScrollPane);

		mainWindow.setVisible(true);
	}
	
	/**
	 * <p>Adds a message to the JTextArea</p>
	 * @param m		&emsp;(<b>String</b>) The message to add
	 */
	public static void addMessage(String m)
	{
		LocalDateTime now = LocalDateTime.now();
		StringBuilder hourString = new StringBuilder();
		
		// para que quede facha
		if(now.getHour() < 10)
			hourString.append("0" + now.getHour());
		else
			hourString.append(now.getHour());
		
		hourString.append(":");
		
		// para que quede facha
		if(now.getMinute() < 10)
			hourString.append("0" + now.getMinute());
		else
			hourString.append(now.getMinute());
		
		hourString.append(":");
		
		// para que quede facha
		if(now.getSecond() < 10)
			hourString.append("0" + now.getSecond());
		else
			hourString.append(now.getSecond());

		String newString = "[" + hourString + "] " + m;
		
		mainTextArea.append(newString + "\n");
		
		// scrolleamos
		JScrollBar vertical = verticalScrollPane.getVerticalScrollBar();
		vertical.setValue(vertical.getMaximum());
	}
}
